/*
 * (C) Copyright 2010 Nuxeo SA (http://nuxeo.com/) and contributors.
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
package org.nuxeo.ecm.core.schema;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Facet Descriptor.
 */
@XObject("facet")
public class FacetDescriptor {

    @XNode("@name")
    public String name;

    @XNodeList(value = "schema", type = SchemaDescriptor[].class, componentType = SchemaDescriptor.class)
    public SchemaDescriptor[] schemas;

    /* empty constructor needed by XMap */
    public FacetDescriptor() {
    }

    public FacetDescriptor(String name, SchemaDescriptor[] schemas) {
        this.name = name;
        this.schemas = schemas == null ? new SchemaDescriptor[0] : schemas;
    }

    @Override
    public String toString() {
        return "Facet(" + name + ',' + SchemaDescriptor.getSchemaNames(schemas)
                + ')';
    }

}
