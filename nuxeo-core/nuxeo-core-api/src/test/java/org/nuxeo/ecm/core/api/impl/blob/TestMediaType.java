/*
 * (C) Copyright 2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 */

package org.nuxeo.ecm.core.api.impl.blob;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.ecm.core.schema.SchemaManagerImpl;
import org.nuxeo.ecm.core.schema.TypeService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.NXRuntimeTestCase;

public class TestMediaType extends NXRuntimeTestCase{

    public static final String SCHEMA_NAME = "media";
    public static final String SCHEMA_PREFIX = "media";


    protected SchemaManager typeMgr;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        deployBundle("org.nuxeo.ecm.core.schema");
        deployContrib("org.nuxeo.ecm.core.api.tests", "OSGI-INF/test-media-types-contrib.xml");
        typeMgr = getTypeManager();
    }

     public static SchemaManagerImpl getTypeManager() {
            return (SchemaManagerImpl) getTypeService().getTypeManager();
     }

     public static TypeService getTypeService() {
            return (TypeService) Framework.getRuntime().getComponent(TypeService.NAME);
     }

    // shema name != prefix name
    public void testDifferentPrefix()  throws Exception {
        DocumentModel doc = new DocumentModelImpl("/", "mediaDoc", "Media");
        doc.setPropertyValue("media:title", "Media Title");
        Object o1 = doc.getProperty("media:title");
        Object o2 = doc.getProperty("media:/title");
        assertEquals(o1, o2);

        // using the prefix
        o2 = doc.getProperty("m:title");
        assertEquals(o1, o2);

        o2 = doc.getProperty("m:/title");
        assertEquals(o1, o2);
    }

    // shema name = prefix name
    public void testSamePrefix()  throws Exception {
        DocumentModel doc = new DocumentModelImpl("/", "mediaDoc", "SameMedia");
        doc.setPropertyValue("sameMedia:title", "Media Title");
        Object o1 = doc.getProperty("sameMedia:title");
        Object o2 = doc.getProperty("sameMedia:/title");
        assertEquals(o1, o2);
    }

}
