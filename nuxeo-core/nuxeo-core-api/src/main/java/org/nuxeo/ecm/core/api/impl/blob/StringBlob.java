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

package org.nuxeo.ecm.core.api.impl.blob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;

import org.nuxeo.ecm.core.api.Blob;

/**
 * @author  <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class StringBlob extends DefaultBlob implements Serializable {

    private static final long serialVersionUID = -1369527636846459436L;

    protected final String content;

    public StringBlob(String content) {
        this(content, "text/plain", "UTF-8");
    }

    public StringBlob(String content, String mimeType) {
        this(content, mimeType, "UTF-8");
    }

    public StringBlob(String content, String mimeType, String encoding) {
        this.content = content;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }


    @Override
    public long getLength() {
        if (content == null) {
            return 0;
        }
        return content.length();
    }

    @Override
    public InputStream getStream() throws IOException {
        if (content == null || content.length() == 0) {
            return EMPTY_INPUT_STREAM;
        }
        return new ByteArrayInputStream(getByteArray());
    }

    @Override
    public byte[] getByteArray() throws IOException {
        if (content == null || content.length() == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        return content.getBytes(encoding == null ? "UTF-8" : encoding);
    }

    @Override
    public String getString() throws IOException {
        if (content == null || content.length() == 0) {
            return EMPTY_STRING;
        }
        return content;
    }

    @Override
    public Reader getReader() throws IOException {
        if (content == null || content.length() == 0) {
            return EMPTY_READER;
        }
        return new StringReader(content);
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public Blob persist() throws IOException {
        return this;
    }

}
