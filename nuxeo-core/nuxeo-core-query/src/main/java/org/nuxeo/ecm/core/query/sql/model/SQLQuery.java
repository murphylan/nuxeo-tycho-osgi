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
 *     Bogdan Stefanescu
 *     Florent Guillaume
 */

package org.nuxeo.ecm.core.query.sql.model;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author Bogdan Stefanescu
 * @author Florent Guillaume
 */
public class SQLQuery implements ASTNode {

    private static final long serialVersionUID = 6383829486216039408L;

    private String queryString;

    public final SelectClause select;

    public final FromClause from;

    public final WhereClause where;

    public final OrderByClause orderBy;

    public final GroupByClause groupBy;

    public final HavingClause having;

    public long limit = 0;

    public long offset = 0;

    public SQLQuery() {
        this(new SelectClause(), new FromClause(), null, null, null, null);
    }

    public SQLQuery(SelectClause select, FromClause from) {
        this(select, from, null, null, null, null);
    }

    public SQLQuery(SelectClause select, FromClause from, WhereClause where) {
        this(select, from, where, null, null, null);
    }

    public SQLQuery(SelectClause select, FromClause from, WhereClause where,
            OrderByClause orderBy) {
        this(select, from, where, null, null, orderBy);
    }

    public SQLQuery(SelectClause select, FromClause from, WhereClause where,
            GroupByClause groupBy, HavingClause having, OrderByClause orderBy) {
        assert select != null && from != null;
        this.select = select;
        this.from = from;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
    }

    public SQLQuery(SelectClause select, FromClause from, WhereClause where,
            GroupByClause groupBy, HavingClause having, OrderByClause orderBy,
            long limit, long offset) {
        assert select != null && from != null;
        this.select = select;
        this.from = from;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * Copying constructor. Does not deep-copy the clauses though.
     */
    public SQLQuery(SQLQuery other) {
        select = other.select;
        from = other.from;
        where = other.where;
        orderBy = other.orderBy;
        groupBy = other.groupBy;
        having = other.having;
        limit = other.limit;
        offset = other.offset;
    }

    public SelectClause getSelectClause() {
        return select;
    }

    public FromClause getFromClause() {
        return from;
    }

    public WhereClause getWhereClause() {
        return where;
    }

    public OrderByClause getOrderByClause() {
        return orderBy;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitQuery(this);
    }

    @Override
    // FIXME: not finished
    public String toString() {
        if (queryString != null) {
            return queryString;
        }
        StringBuilder buf = new StringBuilder();
        buf.append("SELECT ").append(select).append(" FROM ").append(from);
        if (where != null) {
            buf.append(" WHERE ").append(where);
        }
        if (orderBy != null) {
            buf.append(" ORDER BY ").append(orderBy);
        }
        return buf.toString();
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLQuery other = (SQLQuery) obj;
        if (select == null) {
            if (other.select != null) {
                return false;
            }
        } else if (!select.equals(other.select)) {
            return false;
        }
        if (from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!from.equals(other.from)) {
            return false;
        }
        if (where == null) {
            if (other.where != null) {
                return false;
            }
        } else if (!where.equals(other.where)) {
            return false;
        }
        if (orderBy == null) {
            if (other.orderBy != null) {
                return false;
            }
        } else if (!orderBy.equals(other.orderBy)) {
            return false;
        }
        if (groupBy == null) {
            if (other.groupBy != null) {
                return false;
            }
        } else if (!groupBy.equals(other.groupBy)) {
            return false;
        }
        if (having == null) {
            if (other.having != null) {
                return false;
            }
        } else if (!having.equals(other.having)) {
            return false;
        }
        if (limit != other.limit) {
            return false;
        }
        if (offset != other.offset) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (select == null ? 0 : select.hashCode());
        result = prime * result + (from == null ? 0 : from.hashCode());
        result = prime * result + (where == null ? 0 : where.hashCode());
        result = prime * result + (orderBy == null ? 0 : orderBy.hashCode());
        result = prime * result + (groupBy == null ? 0 : groupBy.hashCode());
        result = prime * result + (having == null ? 0 : having.hashCode());
        result = prime * result + (int) (limit ^ (limit >>> 32));
        result = prime * result + (int) (offset ^ (offset >>> 32));
        return result;
    }

    /**
     * Interface for a class that can transform a {@link SQLQuery} into another.
     */
    public interface Transformer extends Serializable {

        Transformer IDENTITY = new IdentityTransformer();

        SQLQuery transform(Principal principal, SQLQuery query);
    }

    public static class IdentityTransformer implements Transformer {
        private static final long serialVersionUID = 1L;

        @Override
        public SQLQuery transform(Principal principal, SQLQuery query) {
            return query;
        }
    }

}
