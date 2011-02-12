/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.runtime.jetty;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.xml.XmlConfiguration;
import org.nuxeo.common.Environment;
import org.nuxeo.common.server.WebApplication;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.deployment.preprocessor.DeploymentPreprocessor;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.model.DefaultComponent;
import org.osgi.framework.Bundle;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class JettyComponent extends DefaultComponent {

    public static final ComponentName NAME = new ComponentName("org.nuxeo.runtime.server");
    public static final String XP_WEB_APP = "webapp";
    public static final String XP_DATA_SOURCE = "datasource";

    protected Server server;
    protected ContextHandlerCollection contexts = new ContextHandlerCollection();
    protected File config;
    protected File log;

    private static final Log logger = LogFactory.getLog(JettyComponent.class);

    public Server getServer() {
        return server;
    }

    /**
     * 
     * @param context
     * @throws Exception
     */
    @Override
    public void activate(ComponentContext context) throws Exception {
        // apply bundled configuration
        URL cfg = null;

        String cfgName = Framework.getProperty("org.nuxeo.jetty.config");
        if (cfgName != null) {
            if (cfgName.contains(":/")) {
                cfg = new URL(cfgName);
            } else { // assume a file
                File file = new File(cfgName);
                if (file.isFile()) {
                    cfg = file.toURI().toURL();
                }
            }
        } else {
            File file = new File(Environment.getDefault().getConfig(), "jetty.xml");
            if (file.isFile()) {
                cfg = file.toURI().toURL();
            } else {
            	//[Hugues] look for jetty.xml directly inside the bundle:
            	//currently not necessary. avoid for now.
            	try {
//            		cfg = context.getRuntimeContext().getBundle().getEntry("jetty-home/etc/jetty.xml");
//            		if (cfg.openConnection() == null) {
//            			cfg = null;//does not exist
//            		} else {
//            			File installLocation = getBundleInstallLocation(
//            					context.getRuntimeContext().getBundle());
//            			
//            			String jettyHome = System.getProperty("jetty.home");
//            			if (jettyHome == null || jettyHome.length() == 0) {
//            				System.setProperty("jetty.home", installLocation.getAbsolutePath() + File.separatorChar + "jetty-home");
//            			}
//            			String jettyLogs = System.getProperty("jetty.logs");
//            			if (jettyLogs == null || jettyLogs.length() == 0) {
//            				System.setProperty("jetty.logs", System.getProperty("jetty.home") + "/logs");
//            			}
//            		}
            	} catch (Throwable t) {
            		t.printStackTrace();
            	}
            }
            
        }
        if (cfg != null) {
            XmlConfiguration configuration = new XmlConfiguration(cfg);
            server = (Server)configuration.configure();
        } else {
            int p = 8080;
            String port = Environment.getDefault().getProperty("http_port");
            if (port != null) {
                try {
                p = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    // do noting
                }
            }
            server = new Server(p);
        }
        Handler[] handlers = server.getHandlers();
        if (handlers != null && handlers.length > 0 && handlers[0] instanceof ContextHandlerCollection) {
            contexts = (ContextHandlerCollection)handlers[0];
        } else if (handlers == null || handlers.length == 0) {
            // dynamic configuration
            contexts = new ContextHandlerCollection();
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            File logDir = Environment.getDefault().getLog();
            logDir.mkdirs();
            File logFile = new File(logDir, "jetty.log");
            NCSARequestLog requestLog = new NCSARequestLog(logFile.getAbsolutePath());
            requestLogHandler.setRequestLog(requestLog);
            //handlers = new Handler[] {contexts, new DefaultHandler(), requestLogHandler};
            handlers = new Handler[] {contexts, requestLogHandler};
            server.setHandlers(handlers);
            server.setSendServerVersion(true);
            server.setStopAtShutdown(true);
        } else { // add only the contexts handler
            contexts = new ContextHandlerCollection();
            Handler[] newHandlers = new Handler[handlers.length+1];
            newHandlers[0] = contexts;
            System.arraycopy(handlers, 0, newHandlers, 0, handlers.length);
            server.setHandlers(newHandlers);
        }
        server.start();
    }

    @Override
    public void deactivate(ComponentContext context) throws Exception {
        server.stop();
        server = null;
    }

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
    	if (XP_WEB_APP.equals(extensionPoint)) {
        //[Hugues] if no jndi is setup let's do it:
    		if (System.getProperty("java.naming.factory.initial") == null) {
    			System.setProperty("java.naming.factory.initial", "org.mortbay.naming.InitialContextFactory");
    		}
    		if (System.getProperty("java.naming.factory.url.pkgs") == null) {
    			System.setProperty("java.naming.factory.url.pkgs", "org.mortbay.naming");
    		}
    		
    		File home = Environment.getDefault().getHome();
            WebApplication app = (WebApplication)contribution;
            
            if (app.needsWarPreprocessing()) {
                logger.info("Starting deployment preprocessing");
                DeploymentPreprocessor dp = new DeploymentPreprocessor(home);
                dp.init();
                dp.predeploy();
                logger.info("Deployment preprocessing terminated");
            }

            WebAppContext ctx = new WebAppContext();
            ctx.setContextPath(app.getContextPath());
            String root = app.getWebRoot();
            if (root != null) {
                File file = null;
                //[Hugues] look for pathes that start with ./
                //and in that case consider that it is inside the bundle itself.
                if (root.startsWith("./")) {
                	File bundle = getBundleInstallLocation(
                			contributor.getContext().getBundle());
                	file = new File(bundle, root.substring(2));
                } else {
                	file = new File(home, root);
                }
                ctx.setWar(file.getAbsolutePath());
            }
            String webXml = app.getConfigurationFile();
            if (webXml != null) {
                File file = new File(home, root);
                ctx.setDescriptor(file.getAbsolutePath());
            }
            File defWebXml = new File(Environment.getDefault().getConfig(), "default-web.xml");
            if (defWebXml.isFile()) {
              ctx.setDefaultsDescriptor(defWebXml.getAbsolutePath());
            }

            contexts.addHandler(ctx);
            org.mortbay.log.Log.setLog(new Log4JLogger(logger));
            
            //[Hugues] if we want the webapp to be able to load classes inside osgi
            //we must get a hold of the bundle's classloader.
            //I have not found a way to do this directly from the bundle object unfortunately.
            //As a workaround, we require the developer to declare the class name of
            //an object that is defined inside the bundle.
            //TODO: better (?)
            String bundleClassName = (String) contributor.getContext().getBundle()
            	.getHeaders().get("Webapp-InternalClassName");
            if (bundleClassName == null) {
            	bundleClassName = (String) contributor.getContext().getBundle()
            		.getHeaders().get("Bundle-Activator");
            }
            if (bundleClassName == null) {
            	//parse the web.xml and look for a class name there ?
            }
            if (bundleClassName != null) {
	            ClassLoader osgiCl = contributor.getContext().getBundle()
	            		.loadClass(bundleClassName).getClassLoader();
	            ClassLoader composite = new TwinClassLoaders(server.getClass().getClassLoader(), osgiCl);
	            WebAppClassLoader wcl = new WebAppClassLoader(composite, ctx);
	    		ctx.setClassLoader(wcl);
            }
            
            ctx.start();
            //HandlerWrapper wrapper = (HandlerWrapper)ctx.getHandler();
            //wrapper = (HandlerWrapper)wrapper.getHandler();
            //wrapper.setHandler(new NuxeoServletHandler());

            if (ctx.isFailed()) {
                logger.error("Error in war deployment");
            }

        } else if (XP_DATA_SOURCE.equals(extensionPoint)) {

        }
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (XP_WEB_APP.equals(extensionPoint)) {

        } else if (XP_DATA_SOURCE.equals(extensionPoint)) {

        }
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        if (adapter == org.mortbay.jetty.Server.class) {
            return adapter.cast(server);
        }
        return null;
    }

//hack to locate the file-system directly from the bundle.
//support equinox, felix and nuxeo's osgi implementations.
//not tested on nuxeo and felix just yet.
//The url nuxeo and felix return is created directly from the File so it should work.
	private static Field BUNDLE_ENTRY_FIELD = null;
	private static Field FILE_FIELD = null;
	public static File getBundleInstallLocation(Bundle bundle) throws Exception {
		//String installedBundles = System.getProperty("osgi.bundles");
		//grab the MANIFEST.MF's url
		//and then do what it takes.
		URL url = bundle.getEntry("/META-INF/MANIFEST.MF");
//		System.err.println(url.toString() + " " + url.toURI() + " " + url.getProtocol());
		if (url.getProtocol().equals("file")) {
			//this is the case with Felix and maybe other OSGI frameworks
			//should make sure it is not a jar.
			return new File(url.toURI()).getParentFile().getParentFile();
		} else if (url.getProtocol().equals("bundleentry")) {
			//say hello to equinox who has its own protocol.
			//we use introspection like there is no tomorrow to get access to the File
			URLConnection con = url.openConnection();
			if (BUNDLE_ENTRY_FIELD == null) {
				BUNDLE_ENTRY_FIELD = con.getClass().getDeclaredField("bundleEntry");
				BUNDLE_ENTRY_FIELD.setAccessible(true);
			}
			Object bundleEntry = BUNDLE_ENTRY_FIELD.get(con);
			if (FILE_FIELD == null) {
				FILE_FIELD = bundleEntry.getClass().getDeclaredField("file");
				FILE_FIELD.setAccessible(true);
			}
			File f = (File)FILE_FIELD.get(bundleEntry);
			return f.getParentFile().getParentFile();
		}
		return null;
	}
}
class TwinClassLoaders extends ClassLoader {
	private ClassLoader _cl2;
	public TwinClassLoaders(ClassLoader cl1, ClassLoader cl2) {
		super(cl1);
		_cl2 = cl2;
	}
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException cne) {
			if (_cl2 != null) {
				return _cl2.loadClass(name);
			} else {
				throw cne;
			}
		}
	}

}

