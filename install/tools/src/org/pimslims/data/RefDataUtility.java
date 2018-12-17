package org.pimslims.data;

import java.util.Collection;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;

public class RefDataUtility {

    public static void removeRecord(final WritableVersion wv, final Class refClass,
        final Map<String, Object> AttrMap) throws AccessException, ConstraintException {
        final Collection<ModelObject> mos = wv.findAll(refClass, AttrMap);
        for (final ModelObject mo : mos) {
            mo.delete();
        }
    }

    static <T extends ModelObject> T findOrCreate(final WritableVersion wv, final Class refClass,
        final Map<String, Object> AttrMap) throws AccessException, ConstraintException {
        T mo = (T) wv.findFirst(refClass, AttrMap);
        if (mo == null) {
            mo = (T) wv.create(refClass, AttrMap);
        }
        return mo;
    }

}
