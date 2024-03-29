/*
 * (C) Copyright 2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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

package org.nuxeo.runtime.services.streaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.nuxeo.common.utils.FileUtils;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractStreamSource implements StreamSource {

    @Override
    public long getLength() throws IOException {
        return -1L;
    }

    @Override
    public boolean canReopen() {
        return false;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileUtils.readBytes(getStream());
    }

    @Override
    public String getString() throws IOException {
        return new String(getBytes());
    }

    @Override
    public void copyTo(File file) throws IOException {
        copyTo(new FileOutputStream(file));
    }

    @Override
    public void copyTo(OutputStream out) throws IOException {
        FileUtils.copy(getStream(), out);
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
