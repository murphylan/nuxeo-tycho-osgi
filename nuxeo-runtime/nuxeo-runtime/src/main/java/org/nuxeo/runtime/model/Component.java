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
 *
 * $Id$
 */

package org.nuxeo.runtime.model;

/**
 * A Nuxeo Runtime component.
 * <p>
 * Components are extensible and adaptable objects and they provide methods to
 * respond to component life cycle events.
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public interface Component extends Extensible {

    /**
     * Activates the component.
     * <p>
     * This method is called by the runtime when a component is activated.
     *
     * @param context the runtime context
     * @throws Exception if an error occurs during activation
     */
    void activate(ComponentContext context) throws Exception;

    /**
     * Deactivates the component.
     * <p>
     * This method is called by the runtime when a component is deactivated.
     *
     * @param context the runtime context
     * @throws Exception if an error occurs during activation
     */
    void deactivate(ComponentContext context) throws Exception;

    /**
     * Notify the component that Nuxeo Framework finished starting all Nuxeo
     * bundles.
     *
     * @throws Exception
     */
    void applicationStarted(ComponentContext context) throws Exception;

}
