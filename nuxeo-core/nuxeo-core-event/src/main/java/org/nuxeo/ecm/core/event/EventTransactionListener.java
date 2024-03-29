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
 */
package org.nuxeo.ecm.core.event;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 * @deprecated was put in place in 5.1 version and maintained for compatibility only.
 */
public interface EventTransactionListener {

    /**
     * Invoked multiple time each time a session is created
     * inside a transaction.
     */
    void transactionStarted();

    /**
     * Invoked multiple time when the transaction is roll-backing, one for each
     * session supported by the transaction.
     */
    void transactionRollbacked();

    /**
     * Invoked multiple time when the transaction is committing, one for
     * each session supported by the transaction.
     */
    void transactionCommitted();

}
