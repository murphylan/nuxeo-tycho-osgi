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

package org.nuxeo.runtime;

import java.io.Serializable;

/**
 * A version consists of three fields, denoting the major version,
 * the minor version, and the update version. Example: 3.1.2.
 *
 * @author  <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class Version implements Serializable, Comparable<Version> {

    public static final Version ZERO = new Version(0, 0, 0);
    public static final Version MIN = ZERO;
    public static final Version MAX = new Version(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private static final long serialVersionUID = 4287621413157054503L;

    private final int major;
    private final int minor;
    private final int update;

    /**
     * Creates a new version object given the major, minor and update
     * version numbers.
     *
     * @param major the major version
     * @param minor the minor version
     * @param update the update version
     */
    public Version(int major, int minor, int update) {
        this.major = major;
        this.minor = minor;
        this.update = update;
    }

    /**
     * Creates a new version object given a string representation of the version.
     *
     * @param version the version string
     * @return the version object
     * @throws NumberFormatException if the version string is invalid
     */
    public static Version parseString(String version) {
        int p = version.indexOf('.', 0);
        if (p > -1) {
            int major = Integer.parseInt(version.substring(0, p));
            int q = version.indexOf('.', p + 1);
            if (q > -1) {
                int minor = Integer.parseInt(version.substring(p + 1, q));
                int update = Integer.parseInt(version.substring(q + 1));
                return new Version(major, minor, update);
            } else {
                return new Version(major,
                        Integer.parseInt(version.substring(p + 1)), 0);
            }
        } else {
            return new Version(Integer.parseInt(version), 0, 0);
        }
    }

    /**
     * Tests if the current version is greater than the given one.
     *
     * @param version the version to compare to the current one
     * @return true if the current version is greater, false otherwise
     */
    public boolean isGreaterThan(Version version) {
        if (major > version.major) {
            return true;
        }
        if (major < version.major) {
            return false;
        }
        if (minor > version.minor) {
            return true;
        }
        if (minor < version.minor) {
            return false;
        }
        return update > version.update;
    }

    /**
     * Tests if the current version is greater or equal to the given one.
     *
     * @param version the version to compare to the current one
     * @return if the current version is greater or equal, false otherwise
     */
    public boolean isGreaterOrEqualThan(Version version) {
        if (major > version.major) {
            return true;
        }
        if (major < version.major) {
            return false;
        }
        if (minor > version.minor) {
            return true;
        }
        if (minor < version.minor) {
            return false;
        }
        return update >= version.update;
    }

    /**
     * Tests if the current version is equal to the given one.
     *
     * @param version the version to compare to the current one
     * @return if the current version is equal to the given one, false otherwise
     */
    public boolean isEqualTo(Version version) {
        return major == version.major
            && minor == version.minor
            && update == version.update;
    }

    /**
     * Gets the minor version field.
     *
     * @return the minor
     */
    public int getMinorVersion() {
        return minor;
    }

    /**
     * Gets the major version field.
     *
     * @return the major
     */
    public int getMajorVersion() {
        return major;
    }

    /**
     * Gets the update version field.
     *
     * @return the update
     */
    public int getUpdateVersion() {
        return update;
    }

    /**
     * Gets the string representation of this version.
     * <p>
     * The string representation can be used in {@link Version#parseString(String)}
     * to create a version object.
     */
    @Override
    public String toString() {
        return new StringBuffer().append(major)
            .append('.').append(minor).append('.')
            .append(update).toString();
    }

    @Override
    public int hashCode() {
        return major ^ (minor << 8) ^ (update << 16);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Version) {
            return isEqualTo((Version) object);
        }
        return false;
    }

    @Override
    public int compareTo(Version v) {
        if (v == null) {
            return -1;
        }
        if (isEqualTo(v)) {
            return 0;
        } else {
            return isGreaterThan(v) ? 1 : 0;
        }
    }

}
