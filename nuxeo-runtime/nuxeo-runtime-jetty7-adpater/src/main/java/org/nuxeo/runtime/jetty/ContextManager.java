/*
 * (C) Copyright 2006-2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 */
package org.nuxeo.runtime.jetty;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 *
 * Hugues: TODO use the http osgi service. look into suppporting
 * filters directly on the jetty's http osgi service.
 */
public class ContextManager {

    protected Server server;
    protected Map<String, ContextHandler> contexts;

    public ContextManager(Server server) {
        this.server = server;
        this.contexts = new HashMap<String, ContextHandler>();
    }

    public Server getServer() {
        return server;
    }

    public synchronized void addFilter(FilterDescriptor descriptor) {
        String path = descriptor.getContext();
        ContextHandler ctx = contexts.get(path);
        if (ctx == null) {
            ctx = new ContextHandler(server, path);//, ContextHandler.SESSIONS | Context.NO_SECURITY);
            contexts.put(path, ctx);
        }
        FilterHolder holder = new FilterHolder(descriptor.getClazz());
        Map<String,String> params = descriptor.getInitParams();
        if (params != null) {
            holder.setInitParameters(params);
        }
        //TODO
        //ctx.addFilter(holder, descriptor.getPath(), org.mortbay.jetty.Handler.DEFAULT);
    }

    public synchronized void addServlet(ServletDescriptor descriptor) {
        String path = descriptor.getContext();
        ContextHandler ctx = contexts.get(path);
        if (ctx == null) {
            ctx = new ContextHandler(server, path);//, Context.SESSIONS | Context.NO_SECURITY);
            contexts.put(path, ctx);
        }
        ServletHolder holder = new ServletHolder(descriptor.getClazz());
        Map<String,String> params = descriptor.getInitParams();
        if (params != null) {
            holder.setInitParameters(params);
        }
        //TODO
        //ctx.addServlet(holder, descriptor.getPath());
    }

    public synchronized void removeFilter(FilterDescriptor descriptor) {
        //TODO not yet implemented
    }

    public synchronized void removeServlet(ServletDescriptor descriptor) {
        //TODO not yet implemented
    }

}

