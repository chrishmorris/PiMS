/**
 * DataStorageBinder.java
 * 
 * Created on 27 September 2007, 12:17
 * 
 * xtalPIMS org.pimslims.crystallization.datastorage DataStorageBinder
 * 
 * @author Ian Berry
 * @date $
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Ian Berry, University of Oxford This library is free software; you can
 *           redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 *           published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 *           any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */

package org.pimslims.crystallization.datastorage;

import java.io.FileNotFoundException;

import javax.naming.Context;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;

/**
 * 
 * @author Bill Lin
 */
public class DataStorageFactory {
    private static DataStorageFactory dsFactory;

    private final AbstractModel model;

    /**
     * this class provides static methods only
     */
    private DataStorageFactory() {
        super();
        model = ModelImpl.getModel();
    }

    private DataStorageFactory(final Context properties) throws FileNotFoundException {
        super();
        model = ModelImpl.getModel(properties);
    }

    public synchronized static DataStorageFactory getDataStorageFactory() {
        if (dsFactory == null) {
            dsFactory = new DataStorageFactory();
        }
        return dsFactory;
    }

    public synchronized static DataStorageFactory getDataStorageFactory(final Context properties)
        throws FileNotFoundException {
        if (dsFactory == null) {
            dsFactory = new DataStorageFactory(properties);
        }
        return dsFactory;
    }

    public synchronized DataStorageImpl getDataStorage() {
        return new DataStorageImpl(model);
    }

    public AbstractModel getModel() {
        return this.model;
    }
}
