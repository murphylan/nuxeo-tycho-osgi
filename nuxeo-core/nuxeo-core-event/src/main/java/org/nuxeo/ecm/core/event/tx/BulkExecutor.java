/*
 * (C) Copyright 2006-2009 Nuxeo SAS (http://nuxeo.com/) and contributors.
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

package org.nuxeo.ecm.core.event.tx;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.runtime.api.Framework;

/**
 * @author Thierry Delprat
 */
public class BulkExecutor extends PostCommitSynchronousRunner {

    private static final Log log = LogFactory.getLog(BulkExecutor.class);

    protected static Integer timeout;

    protected static int getExecTimeOutInS() {
        if (timeout == null) {
            String strTimeout = Framework.getProperty("org.nuxeo.ecm.core.event.tx.BulkExecutor.timeout", "600");
            timeout = Integer.parseInt(strTimeout);
        }
        return timeout;
    }

    public BulkExecutor(List<EventListenerDescriptor> listeners,
            EventBundle event) {
        super(listeners, event, getExecTimeOutInS() * 1000);
    }

    @Override
    protected Runnable getExecutor() {
        return new MonoThreadBulkExecutor();
    }

    @Override
    protected void handleUnfinishedThread(Thread runner) {
        log.error("Bulk execution of event handlers is too long, exiting by killing thread");
        runner.interrupt();
    }

    protected class MonoThreadBulkExecutor implements Runnable, Thread.UncaughtExceptionHandler {

        protected final EventBundleTransactionHandler txh = new EventBundleTransactionHandler();

        @Override
        public void run() {
            long t0 = System.currentTimeMillis();
            log.debug("Start post commit sync execution in Thread "
                    + Thread.currentThread().getId());
            txh.beginNewTransaction(BulkExecutor.getExecTimeOutInS());
            try {
                for (EventListenerDescriptor listener : listeners) {
                    listener.asPostCommitListener().handleEvent(event);
                }
                event.disconnect();
                txh.commitOrRollbackTransaction();
            } catch (Throwable t) {
                log.error("Exception occured during Bulk Event Handler execution, rolling back transaction", t);
                log.error("Total execution time = " + (System.currentTimeMillis() - t0) + "ms");
                event.disconnect();
                txh.rollbackTransaction();
            }
            log.debug("End of all post commit sync executions : "
                    + (System.currentTimeMillis() - t0) + "ms");
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            event.disconnect();
            txh.rollbackTransaction();
        }

    }

}
