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

package org.nuxeo.ecm.core.lifecycle.impl;

import org.nuxeo.ecm.core.lifecycle.LifeCycleTransition;

/**
 * Life cycle transition implementation.
 *
 * @see org.nuxeo.ecm.core.lifecycle.LifeCycleTransition
 *
 * @author <a href="mailto:ja@nuxeo.com">Julien Anguenot</a>
 */
public class LifeCycleTransitionImpl implements LifeCycleTransition {

    /** Name of the transition. */
    private final String name;

    /** Description of the transition. */
    private final String description;

    /** Destination state name for this transition. */
    private final String destinationStateName;


    public LifeCycleTransitionImpl(String name, String description, String destinationState) {
        this.name = name;
        this.description = description;
        destinationStateName = destinationState;
    }

    @Override
    public String getDestinationStateName() {
        return destinationStateName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

}
