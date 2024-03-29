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
 *     slacoin
 *
 * $Id$
 */

package org.nuxeo.ecm.core.api.repository.cache;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;

import org.nuxeo.common.DirtyUpdateInvokeBridge;
import org.nuxeo.common.DirtyUpdateInvokeBridge.ThreadContext;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.api.operation.ModificationSet;

public class DirtyUpdateChecker {

    private DirtyUpdateChecker() {
    }

    public static void check(DocumentModel doc) {
        ThreadContext ctx = DirtyUpdateInvokeBridge.getThreadContext();
        if (ctx == null) {
            return; // invoked on server, no cache
        }
        long modified;
        try {
            Property modifiedProp = doc.getProperty("dc:modified");
            if (modifiedProp == null) {
                return;
            }
            Date modifiedDate = modifiedProp.getValue(Date.class);
            if (modifiedDate == null) {
                return;
            }
            modified = modifiedDate.getTime();
        } catch (Exception e) {
            throw new ClientRuntimeException("cannot fetch dc modified for doc " + doc, e);
        }
        long tag = ctx.tag.longValue();
        if (tag >= modified) {
            return; // client cache is freshest than doc
        }
        long invoked = ctx.invoked.longValue();
        if (invoked <= modified) {
            return; // modified by self user
        }
        String message = String.format(
                "%s is outdated : cache %s - op start %s - doc %s",
                doc.getId(), new Date(tag), new Date(
                        invoked), new Date(modified));
        throw new ConcurrentModificationException(message);
    }

    public static Object earliestTag(Object tag1, Object tag2) {
        return ((Long) tag1).longValue() > ((Long) tag2).longValue() ? tag1
                : tag2;
    }

    public static Object computeTag(
            @SuppressWarnings("unused") String sessionId,
            @SuppressWarnings("unused") ModificationSet modifs) {
        // TODO compute a more precise time stamp than current date
        return Long.valueOf(Calendar.getInstance().getTimeInMillis());
    }

}
