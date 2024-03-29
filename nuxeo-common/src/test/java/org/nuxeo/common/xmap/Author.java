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

package org.nuxeo.common.xmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.nuxeo.common.xmap.annotation.XContent;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.w3c.dom.DocumentFragment;

/**
 * @author  <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@XObject(value = "author", order = { "item1", "item2" })
public class Author {

    @XNode("metadata/title")
    String title;

    @XNode("metadata/description")
    String description;

    @XNode("metadata/name")
    Name name;

    @XNode(value = "content", trim = false)
    String content;

    @XNode("metadata/name@type")
    String nameType;

    @XNodeList(value = "list/item", type = ArrayList.class, componentType = String.class)
    List<String> items;

    @XNodeList(value = "list/item@id", type = ArrayList.class, componentType = String.class)
    List<String> itemIds;

    @XNodeList(value = "friends/friend", type = ArrayList.class, componentType = Name.class)
    List<Name> friends;



    @XNodeList(value = "metadata/double", type = Double[].class, componentType = Double.class)
    Double[] doubleArray;

    @XNodeList(value = "metadata/double", type = double[].class, componentType = double.class)
    double[] doubleArray2;

    @XNodeMap(value = "properties/property", key = "@name", type = HashMap.class, componentType = String.class)
    Map<String, String> properties;


    int age;


    @XContent("testContent")
    String testContent;

    @XContent("testContent")
    DocumentFragment testContent2;

    boolean item1;


    boolean item2;
    boolean item3;

    @XNode("@id")
    private String id;

    public String getId() {
        return id;
    }

    @XNode("metadata/age")
    public void setAge(int age) {
        this.age = age;
    }

    @XNode("item2")
    void setField2(String val) {
        if (!item1) {
            Assert.fail("Field item1 was not set before me");
        }
        if (item3) {
            Assert.fail("Field item3 was set before me");
        }
        item2 = true;
    }

    @XNode("item1")
    void setField1(String val) {
        if (item2) {
            Assert.fail("Field item2 was set before item1");
        }
        if (item3) {
            Assert.fail("Field item3 was set before item1");
        }
        item1 = true;
    }

    @XNode("item3")
    void setField3(String val) {
        if (!item1) {
            Assert.fail("Field item1 was not set before item3");
        }
        if (!item2) {
            Assert.fail("Field item2 was not set before item3");
        }
        item3 = true;
    }

    @XNodeList(value = "testNullByDefaultForList", type = ArrayList.class,
            componentType = String.class, nullByDefault = true)
    List<String> testNullByDefaultForList;

    @XNodeMap(value = "testNullByDefaultForMap", key = "@name",
            type = HashMap.class, componentType = String.class, nullByDefault = true)
    Map<String, String> testNullByDefaultForMap;

    @Override
    public String toString() {
        return "Author {\n"
                + "  title: " + title + '\n'
                + "  description: " + description + '\n'
                + "  id: " + id + '\n'
                + "  nameType: " + nameType + '\n'
                + "  name: " + name + '\n'
                + "  age: " + age + '\n'
                + "  items: " + items + '\n'
                + "  itemIds: " + itemIds + '\n'
                + "  content: <" + content + '>' + '\n'
                + "  friends: " + friends + '\n'
                + "  properties: " + properties + '\n'
                + '}';
    }


    // getter used to reverse xmap
    public int getAge() {
        return age;
    }

    public String getField1() {
        return "" + item1;
    }

    public String getField2() {
        return "" +item2;
    }

    public String getField3() {
        return ""+ item3;
    }


    // map to load objects
    @XNodeMap(value = "persons/person", key = "firstName", type = HashMap.class, componentType = Name.class)
    Map<String, Name> persons;

}
