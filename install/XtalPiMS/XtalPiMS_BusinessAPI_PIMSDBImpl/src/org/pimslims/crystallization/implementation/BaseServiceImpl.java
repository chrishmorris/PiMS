/*
 * BaseServiceImpl.java Created on 10 August 2007, 12:50 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import org.pimslims.access.Access;
import org.pimslims.business.BaseService;
import org.pimslims.business.DataStorage;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;

/**
 * 
 * @author ian
 */
public class BaseServiceImpl implements BaseService {
    private static final String DUMMYNAME = "Dummy";

    private final DataStorageImpl dataStorage;

    protected final ReadableVersion version;

    /**
     * Creates a new instance of BaseServiceImpl
     * 
     * @param baseStorage
     */
    public BaseServiceImpl(DataStorage baseStorage) {
        dataStorage = (DataStorageImpl) baseStorage;
        this.version = dataStorage.getVersion();
    }

    /**
     * 
     * @return
     */
    public final DataStorageImpl getDataStorage() {
        return this.dataStorage;
    }

    /**
     * 
     * @return
     */
    public ReadableVersion getVersion() {
        return this.dataStorage.getVersion();
    }

    public WritableVersion getWritableVersion() {
        return (WritableVersion) getVersion();
    }

    protected User getDummyUser() throws ConstraintException {
        User user = getVersion().findFirst(User.class, User.PROP_NAME, DUMMYNAME);
        if (user == null)
            user = new User(getWritableVersion(), DUMMYNAME);
        return user;

    }

    protected void adminOnly() {
        if (!Access.ADMINISTRATOR.equals(version.getUsername())) {
            throw new AssertionError("Only the administrator can record new group");
        }
    }

    <T extends ModelObject> T findByName(Class<T> pimsClass, String name) {
        return (T) version.findFirst(pimsClass, org.pimslims.model.people.Organisation.PROP_NAME, name);
    }
}
