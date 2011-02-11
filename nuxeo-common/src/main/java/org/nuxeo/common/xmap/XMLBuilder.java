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

package org.nuxeo.common.xmap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLBuilder {

    private static final Log log = LogFactory.getLog(XMLBuilder.class);

    private XMLBuilder() {
    }

    public static String saveToXML(Object object, Element root,
            XAnnotatedObject xao) {
        try {
            toXML(object, root, xao);
            return DOMSerializer.toString(root);
        } catch (Exception e) {
            log.error(e, e);
        }
        return null;
    }

    public static void toXML(Object o, Element parent, XAnnotatedObject xao)
            throws Exception {
        // XPath xpath = XPathFactory.newInstance().newXPath();
        Element currentNode = parent;
        String path = xao.getPath().toString();
        if (path.length() > 0) {
            currentNode = parent.getOwnerDocument().createElement(path);
            parent.appendChild(currentNode);
        }
        // process annotated members
        for (XAnnotatedMember m : xao.members) {
            if (m instanceof XAnnotatedMap) {
                m.toXML(o, currentNode);
            } else if (m instanceof XAnnotatedList) {
                m.toXML(o, currentNode);
            } else if (m instanceof XAnnotatedContent) {
                m.toXML(o, currentNode);
            } else if (m instanceof XAnnotatedParent) {

            } else {
                m.toXML(o, currentNode);
            }
        }
    }

    // TODO use xpath for that ?
    public static Element getOrCreateElement(Element root, Path path) {
        Element e = root;
        for (String segment : path.segments) {
            e = getOrCreateElement(e, segment);
        }
        return e;
    }

    public static Element addElement(Element root, Path path) {
        Element e = root;
        int len = path.segments.length - 1;
        for (int i = 0; i < len; i++) {
            e = getOrCreateElement(e, path.segments[i]);
        }
        return addElement(e, path.segments[len]);
    }

    private static Element getOrCreateElement(Element parent, String segment) {
        NodeList list = parent.getChildNodes();
        for (int i = 0, len = list.getLength(); i < len; i++) {
            Element e = (Element) list.item(i);
            if (segment.equals(e.getNodeName())) {
                return e;
            }
        }
        // node not found, create one
        return addElement(parent, segment);
    }

    public static Element addElement(Element parent, String segment) {
        Element e = parent.getOwnerDocument().createElement(segment);
        parent.appendChild(e);
        return e;
    }

    public static void fillField(Element element, String value, String attribute) {
        if (attribute != null) {
            element.setAttribute(attribute, value);
        } else {
            element.setTextContent(value);
        }
    }

}
