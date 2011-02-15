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
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.osgi.boot.JettyBootstrapActivator;
import org.eclipse.jetty.osgi.boot.OSGiWebappConstants;
import org.eclipse.jetty.osgi.boot.utils.BundleFileLocatorHelper;
import org.nuxeo.common.Environment;
import org.nuxeo.common.server.WebApplication;
import org.nuxeo.runtime.deployment.preprocessor.DeploymentPreprocessor;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.model.DefaultComponent;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author hmalphettes
 *
 */
public class JettyComponent extends DefaultComponent {

    public static final ComponentName NAME = new ComponentName(
	    "org.nuxeo.runtime.server");
	
	public static final String XP_WEB_APP = "webapp";
	
	public static final String XP_SERVLET = "servlet";
	public static final String XP_FILTER = "filter";
	
	public static final String P_SCAN_WEBDIR = "org.nuxeo.runtime.jetty.scanWebDir";
	
//  protected Server server;
//  protected ContextHandlerCollection contexts = new ContextHandlerCollection();
	protected ContextManager ctxMgr;

    private static final Log logger = LogFactory.getLog(JettyComponent.class);

//    public Server getServer() {
    	//for now return null;
    	//we could access the default jetty server which is registered as an OSGi service.
        //return server;
//    }

    /**
     * The jetty server is started by making sure that the jetty.osgi.boot bundle is also started.
     * 
     * @param context
     * @throws Exception
     */
    @Override
    public void activate(ComponentContext context) throws Exception {
    	Bundle jettyBoot = FrameworkUtil.getBundle(JettyBootstrapActivator.class);
    	jettyBoot.start();//won't make a difference if it is already started
/*        // apply bundled configuration
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
        */
    }

    /**
     * Nothing to do. Unless we decide that we must be in charge of stopping the jetty server.
     */
    @Override
    public void deactivate(ComponentContext context) throws Exception {
//        server.stop();
//        server = null;
    }

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
    	if (XP_WEB_APP.equals(extensionPoint)) {
    		
    		File home = Environment.getDefault().getHome();
            WebApplication app = (WebApplication)contribution;
            
            if (app.needsWarPreprocessing()) {
                logger.info("Starting deployment preprocessing");
                DeploymentPreprocessor dp = new DeploymentPreprocessor(home);
                dp.init();
                dp.predeploy();
                logger.info("Deployment preprocessing terminated");
            }
            
            Dictionary<String, String> props = new Hashtable<String, String>();
            String root = app.getWebRoot();
            String webappFolderPath = null;
            if (root != null) {
                File file = null;
                //[Hugues] look for paths that start with ./
                //and in that case consider that it is inside the bundle itself.
                if (!root.startsWith("./")) {
                	file = new File(home, root);
                }
                if (file == null || !file.exists()) {
                	File bundle = BundleFileLocatorHelper.DEFAULT.getBundleInstallLocation(
                			contributor.getContext().getBundle());
                	file = new File(bundle, root.substring(2));
                }
                webappFolderPath = file.getAbsolutePath();
//                ctx.setWar(file.getAbsolutePath());
            }
            String webXml = app.getConfigurationFile();
            if (webXml != null) {
                File file = new File(home, webXml);
                File file1 = null;
                if (!file.exists()) {
                	if (webXml.startsWith("./")) {
                		webXml = webXml.substring(2);
                	}
                	File bundle = BundleFileLocatorHelper.DEFAULT.getBundleInstallLocation(
                			contributor.getContext().getBundle());
                	file1 = new File(bundle, webXml);
                    if (!file1.exists()) {
                    	throw new IllegalArgumentException("Unable to locate " + file.getAbsolutePath()
                    			+ " and unable to locate " + file1.getAbsolutePath());
                    }
                    props.put(OSGiWebappConstants.SERVICE_PROP_WEB_XML_PATH, file1.getAbsolutePath());
                } else {
                	props.put(OSGiWebappConstants.SERVICE_PROP_WEB_XML_PATH, file.getAbsolutePath());
                }
                
//                ctx.setDescriptor(file.getAbsolutePath());
            }
            File defWebXml = new File(Environment.getDefault().getConfig(), "default-web.xml");
            if (defWebXml.isFile()) {
//              ctx.setDefaultsDescriptor(defWebXml.getAbsolutePath());
                props.put(OSGiWebappConstants.SERVICE_PROP_DEFAULT_WEB_XML_PATH, defWebXml.getAbsolutePath());            	
            }
            contributor.getContext().getBundle().start();
            JettyBootstrapActivator.registerWebapplication(contributor.getContext().getBundle(),
            		webappFolderPath, app.getContextPath(), props);

//            org.eclipse.jetty.util.log.Log.setLog(new Log4JLogger(logger));
            

        } else if (XP_FILTER.equals(extensionPoint)) {
            ctxMgr.addFilter((FilterDescriptor)contribution);
        } else if (XP_SERVLET.equals(extensionPoint)) {
            ctxMgr.addServlet((ServletDescriptor)contribution);
        }
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (XP_WEB_APP.equals(extensionPoint)) {
        	JettyBootstrapActivator.unregister(((WebApplication)contribution).getContextPath());
        } else if (XP_FILTER.equals(extensionPoint)) {
            ctxMgr.removeFilter((FilterDescriptor)contribution);
        } else if (XP_SERVLET.equals(extensionPoint)) {
            ctxMgr.removeServlet((ServletDescriptor)contribution);
        }
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        if (adapter == org.eclipse.jetty.server.Server.class) {
            return null;
        	//return adapter.cast(server);
        }
        return null;
    }

}

