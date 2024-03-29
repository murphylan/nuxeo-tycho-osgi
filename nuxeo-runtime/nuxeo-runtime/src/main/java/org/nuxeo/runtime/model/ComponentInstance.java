/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 * $Id$
 */

package org.nuxeo.runtime.model;


/**
 * A component instance is a proxy to the component implementation object.
 * <p>
 * Component instance objects are created each time a component is
 * activated, and destroyed at component deactivation.
 *
 * @author  <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public interface ComponentInstance extends ComponentContext, Extensible, Adaptable {

    /**
     * Gets the actual component implementation instance.
     *
     * @return the component implementation instance
     */
    Object getInstance();

    /**
     * Gets the name of the component.
     *
     * @return the component name
     */
    ComponentName getName();

    /**
     * Gets the runtime context attached to this instance.
     *
     * @return the runtime context
     */
    RuntimeContext getContext();

    /**
     * Activates the implementation instance.
     *
     * @throws Exception if any error occurs during activation
     */
    void activate() throws Exception;

    /**
     * Deactivates the implementation instance.
     *
     * @throws Exception if any error occurs during deactivation
     */
    void deactivate() throws Exception;

    /**
     * Destroys this instance.
     *
     * @throws Exception if any error occurs
     */
    void destroy() throws Exception;

    /**
     * Reload the component. All the extensions and registries are reloaded.
     * @throws Exception
     */
    void reload() throws Exception;

    /**
     * Gets the list of provided services, or null if no service is provided.
     *
     * @return an array containing the service class names or null if no service is provided
     */
    String[] getProvidedServiceNames();

}
