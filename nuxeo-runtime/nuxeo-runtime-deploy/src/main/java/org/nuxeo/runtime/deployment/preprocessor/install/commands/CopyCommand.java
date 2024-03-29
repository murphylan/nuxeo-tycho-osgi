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

package org.nuxeo.runtime.deployment.preprocessor.install.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.common.utils.Path;
import org.nuxeo.common.utils.PathFilter;
import org.nuxeo.runtime.deployment.preprocessor.install.Command;
import org.nuxeo.runtime.deployment.preprocessor.install.CommandContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class CopyCommand implements Command {

    protected final Path src;
    protected final Path dst;
    protected final PathFilter filter;

    /**
     * Constructor for copy command.
     *
     * @param src
     *            the path relative to the root container. The path will be made
     *            absolute if not already
     * @param dst
     *            the path relative to teh root container of the destination. If
     *            it is ending with a slash '/' the destination path is treated
     *            as a directory
     */
    public CopyCommand(Path src, Path dst) {
        this(src, dst, null);
    }

    public CopyCommand(Path src, Path dst, PathFilter filter) {
        this.src = src;
        this.dst = dst;
        this.filter = filter;
    }

    @Override
    public void exec(CommandContext ctx) throws IOException {
        File baseDir = ctx.getBaseDir();
        File srcFile = new File(baseDir, ctx.expandVars(src.toString()));
        File dstFile = new File(baseDir, ctx.expandVars(dst.toString()));

        if (!srcFile.exists()) {
            throw new FileNotFoundException("Could not find the file "
                    + srcFile.getAbsolutePath() + " to copy.");
        }

        if (!dstFile.exists()) {
            if (dst.hasTrailingSeparator()) {
                dstFile.mkdirs();
            } else {
                // make sure parent dirs exists
                File parent = dstFile.getParentFile();
                if (!parent.isDirectory()) {
                    parent.mkdirs();
                }
            }
        }
        if (filter == null) {
            if (srcFile.isDirectory() && src.hasTrailingSeparator()) {
                FileUtils.copy(srcFile.listFiles(), dstFile);
            } else {
                FileUtils.copy(srcFile, dstFile);
            }
        } else {
            if (srcFile.isDirectory() && src.hasTrailingSeparator()) {
                FileUtils.copyTree(srcFile, dstFile, filter);
            } else {
                FileUtils.copy(srcFile, dstFile);
            }
        }
    }

    @Override
    public String toString() {
        return "copy " + src.toString() + " > " + dst.toString();
    }

    @Override
    public String toString(CommandContext ctx) {
        return "copy " + ctx.expandVars(src.toString()) + " > " +
                ctx.expandVars(dst.toString());
    }

}
