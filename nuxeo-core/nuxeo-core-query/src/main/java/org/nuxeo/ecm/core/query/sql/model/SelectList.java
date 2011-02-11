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

package org.nuxeo.ecm.core.query.sql.model;

import org.nuxeo.common.collections.SerializableArrayMap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class SelectList extends SerializableArrayMap<String, Operand> {

    private static final long serialVersionUID = 548479243558815176L;

    @Override
    public String toString() {
        if (count == 0) {
            return "*";
        }
        StringBuilder result = new StringBuilder();

        result.append(get(0));

        for (int i = 1; i < count; i++) {
            Operand op = get(i);
            result.append(", ");
            result.append(op);
        }
        return result.toString();
    }

}
