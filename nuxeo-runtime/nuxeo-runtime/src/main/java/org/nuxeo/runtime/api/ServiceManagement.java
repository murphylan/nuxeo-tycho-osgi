/*
 * (C) Copyright 2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package org.nuxeo.runtime.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ServiceManagement extends DefaultComponent {

    private static final Log log = LogFactory.getLog(ServiceManagement.class);

    public static final ComponentName NAME = new ComponentName("org.nuxeo.runtime.api.ServiceManagement");

    private ServiceManager manager;

    @Override
    public void activate(ComponentContext context) throws Exception {
        manager = ServiceManager.getInstance();
    }

    @Override
    public void deactivate(ComponentContext context) throws Exception {
        manager = null;
    }

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor) {
        if (extensionPoint.equals("servers")) {
            try {
                manager.registerServer((ServiceHost) contribution);
            } catch (Exception e) {
                log.error(e, e);
            }
        } else if (extensionPoint.equals("services")) {
            manager.registerService((ServiceDescriptor) contribution);
        }
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor) {
        if (extensionPoint.equals("servers")) {
            manager.unregisterServer((ServiceHost) contribution);
        } else if (extensionPoint.equals("services")) {
            manager.unregisterService((ServiceDescriptor) contribution);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Class<T> adapter) {
        if (ServiceManager.class.isAssignableFrom(adapter)) {
            return (T) manager;
        }
        return null;
    }

}
