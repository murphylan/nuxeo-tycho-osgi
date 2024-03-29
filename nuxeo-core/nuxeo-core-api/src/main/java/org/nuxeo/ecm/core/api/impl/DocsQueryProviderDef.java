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
 * $Id: JOOoConvertPluginImpl.java 18651 2007-05-13 20:28:53Z sfermigier $
 */

package org.nuxeo.ecm.core.api.impl;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.DocumentRef;

/**
 *
 * @author <a href="mailto:dm@nuxeo.com>Dragos Mihalache</a>
 */
public class DocsQueryProviderDef implements Serializable {

    private static final long serialVersionUID = 6296251214655196508L;

    public enum DefType {
        TYPE_CHILDREN, TYPE_CHILDREN_NON_FOLDER, TYPE_CHILDREN_FOLDERS, TYPE_QUERY, TYPE_QUERY_FTS
    }

    private final DefType type;

    private DocumentRef parentRef;

    private String query;

    private String startingPath;

    // Private constructor, there are no other defs than those defined inhere.
    public DocsQueryProviderDef(DefType type) {
        this.type = type;
    }

    public DefType getType() {
        return type;
    }

    public void setParent(DocumentRef parentRef) {
        this.parentRef = parentRef;
    }

    public DocumentRef getParent() {
        return parentRef;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * The document models path to search within.
     */
    public String getStartingPath() {
        return startingPath;
    }

    public void setStartingPath(String startingPath) {
        this.startingPath = startingPath;
    }

}
