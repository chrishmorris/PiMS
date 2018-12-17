/** 
 * pims-model org.pimslims.model.core NoteComparator.java
 * @author bl67
 * @date 7 Nov 2008
 *
 * Protein Information Management System
 * @version: 1.3
 *
 * Copyright (c) 2008 bl67 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.model.core;

import java.util.Comparator;

/**
 * NoteComparator
 *
 */
public class NoteComparator<T extends Note> implements Comparator<Note> {


	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Note o1, Note o2) {
		if(o1.getDate().equals(o2.getDate()))
			return o1.getDbId().compareTo(o2.getDbId());
		return o1.getDate().compareTo(o2.getDate());
	}

}
