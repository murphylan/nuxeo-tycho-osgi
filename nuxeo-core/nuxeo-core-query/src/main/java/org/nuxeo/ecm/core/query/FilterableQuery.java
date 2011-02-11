/*
 * (C) Copyright 2008-2009 Nuxeo SA (http://nuxeo.com/) and contributors.
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

package org.nuxeo.ecm.core.query;

/**
 * A Query that can be executed with an additional {@link QueryFilter}.
 *
 * @author Florent Guillaume
 */
public interface FilterableQuery extends Query {

    /**
     * Makes a query to the backend with filtering on the BROWSE permission for
     * the principal, facets, and query transformers.
     * <p>
     * The total number of documents can also be retrieved, it is then stored in
     * the {@link DocumentModelList} returned by
     * {@link QueryResult#getDocumentModels}.
     *
     * @param queryFilter the query filter
     * @param countTotal if {@code true}, also count the total number of
     *            documents when no limit/offset is passed
     * @return a query result object describing the resulting documents
     * @throws QueryException
     */
    QueryResult execute(QueryFilter queryFilter, boolean countTotal)
            throws QueryException;

}
