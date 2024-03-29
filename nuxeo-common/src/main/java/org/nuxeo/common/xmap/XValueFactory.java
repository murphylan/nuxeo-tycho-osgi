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

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

/**
 * Value factories are used to decode values from XML strings.
 * <p>
 * To register a new factory for a given XMap instance use the method
 * {@link XMap#setValueFactory(Class, XValueFactory)}.
 *
 * @author  <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public abstract class XValueFactory {

    private static final Log log = LogFactory.getLog(XValueFactory.class);

    static final Map<Class, XValueFactory> defaultFactories = new Hashtable<Class, XValueFactory>();


    public abstract Object deserialize(Context context, String value);
    public abstract String serialize(Context context, Object value);

    public final Object getElementValue(Context context, Node element, boolean trim) {
        String text = element.getTextContent();
        return deserialize(context, trim ? text.trim() : text);
    }

    public final Object getAttributeValue(Context context, Node element, String name) {
        Node at = element.getAttributes().getNamedItem(name);
        return at != null ? deserialize(context, at.getNodeValue()) : null;
    }

    public static void addFactory(Class klass, XValueFactory factory) {
        defaultFactories.put(klass, factory);
    }

    public static XValueFactory getFactory(Class type) {
        return defaultFactories.get(type);
    }

    public static Object getValue(Context context, Class klass, String value) {
        XValueFactory factory = defaultFactories.get(klass);
        if (factory == null) {
            return null;
        }
        return factory.deserialize(context, value);
    }

    public static final XValueFactory STRING = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return value;
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory INTEGER = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return Integer.valueOf(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory LONG = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return Long.valueOf(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory DOUBLE = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return Double.valueOf(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory FLOAT = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return Float.valueOf(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory BOOLEAN = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return Boolean.valueOf(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory DATE = new XValueFactory() {
        private final DateFormat df = DateFormat.getDateInstance();

        @Override
        public Object deserialize(Context context, String value) {
            try {
                return df.parse(value);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String serialize(Context context, Object value) {
            Date date = (Date) value;
            return df.format(date);
        }
    };

    public static final XValueFactory FILE = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            return new File(value);
        }

        @Override
        public String serialize(Context context, Object value) {
            File file = (File) value;
            return file.getName();
        }
    };

    public static final XValueFactory URL = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            try {
                return new URL(value);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    public static final XValueFactory CLASS = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            try {
                return context.loadClass(value);
            } catch (Exception e) {
                log.error("Cannot load class: " + e);
                return null;
            }
        }

        @Override
        public String serialize(Context context, Object value) {
            Class<?> clazz = (Class<?>) value;
            return clazz.getName();
        }
    };

    public static final XValueFactory RESOURCE = new XValueFactory() {
        @Override
        public Object deserialize(Context context, String value) {
            try {
                return new Resource(context.getResource(value));
            } catch (Exception e) {
                log.error("Cannot load resource: " + e);
                return null;
            }
        }

        @Override
        public String serialize(Context context, Object value) {
            return value.toString();
        }
    };

    static {
        addFactory(String.class, STRING);
        addFactory(Integer.class, INTEGER);
        addFactory(Long.class, LONG);
        addFactory(Double.class, DOUBLE);
        addFactory(Date.class, DATE);
        addFactory(Boolean.class, BOOLEAN);
        addFactory(File.class, FILE);
        addFactory(URL.class, URL);

        addFactory(int.class, INTEGER);
        addFactory(long.class, LONG);
        addFactory(double.class, DOUBLE);
        addFactory(float.class, FLOAT);
        addFactory(boolean.class, BOOLEAN);

        addFactory(Class.class, CLASS);
        addFactory(Resource.class, RESOURCE);
    }

}
