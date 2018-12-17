/**
 * pims-model org.pimslims.upgrader ImageTypeRetirer.java
 * 
 * @author cm65
 * @date 23 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.crystallization.WellImageType;
import org.pimslims.model.reference.ImageType;

/**
 * ImageTypeRetirer Copy data from ImageType table to new WellImageType. ImageType was reference data, but
 * contains some information that should be private.
 */
@SuppressWarnings("deprecation")
public class ImageTypeRetirer extends ClassRetirer {

    public ImageTypeRetirer(WritableVersion version2) {
        super(version2, version2.getMetaClass(ImageType.class));
    }

    @Override
    public ModelObject retire(ModelObject old) throws ConstraintException, AccessException {
        WellImageType copy = new WellImageType(this.version, old.get_Name());
        this.copyAttributesAndRoles(old, copy);
        // don't copy LabNoteBookEntry attributes, since ImageType is reference data
        return copy;
    }

}
