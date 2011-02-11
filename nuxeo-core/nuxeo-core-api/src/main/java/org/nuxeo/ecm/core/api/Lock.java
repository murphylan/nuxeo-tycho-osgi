/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Florent Guillaume
 */
package org.nuxeo.ecm.core.api;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Information about a lock set on a document.
 * <p>
 * The lock information holds the owner, which is a user id, and the lock
 * creation time.
 */
public class Lock implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String owner;

    private final Calendar created;

    public Lock(String owner, Calendar created) {
        this.owner = owner;
        this.created = created;
    }

    /**
     * The owner of the lock.
     *
     * @return the owner, which is a user id
     */
    public String getOwner() {
        return owner;
    }

    /**
     * The creation time of the lock.
     *
     * @return the creation time
     */
    public Calendar getCreated() {
        return created;
    }

}
