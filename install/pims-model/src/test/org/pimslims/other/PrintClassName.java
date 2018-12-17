/**
 * pims-model org.pimslims.test PrintClassName.java
 * 
 * @author bl67
 * @date 4 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.other;

import java.util.Collection;

import org.pimslims.test.AbstractTestCase;

/**
 * PrintClassName
 * 
 */
public class PrintClassName extends AbstractTestCase {
    public void testPrint() {
        final Collection<String> classNames = model.getClassNames();
        System.out.println("Found " + classNames.size() + " classes:");
        for (final String name : classNames) {
            System.out.println("<class>" + name + "</class>");
        }
    }
}
