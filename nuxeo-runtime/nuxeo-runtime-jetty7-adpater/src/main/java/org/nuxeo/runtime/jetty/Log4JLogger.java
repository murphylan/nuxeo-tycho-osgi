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

package org.nuxeo.runtime.jetty;

import org.apache.commons.logging.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * Dumb logger to see what happens in Jetty.
 *
 * @author Thierry Delprat
 */
public class Log4JLogger implements Logger {

    protected final Log logger;

    public Log4JLogger(Log logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg, Throwable th) {
        logger.debug(msg, th);
    }

    public void debug(String msg, Object arg0, Object arg1) {
        logger.debug(String.format(msg, arg0, arg1));
    }

    @Override
    public Logger getLogger(String name) {
        return this;
    }

    public void info(String msg, Object arg0, Object arg1) {
        logger.info(String.format(msg, arg0, arg1));
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
    }

    @Override
    public void warn(String msg, Throwable th) {
        logger.warn(msg, th);
    }

   
    public void warn(String msg, Object arg0, Object arg1) {
        logger.warn(String.format(msg, arg0, arg1));
    }

	@Override
	public void debug(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void info(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

}
