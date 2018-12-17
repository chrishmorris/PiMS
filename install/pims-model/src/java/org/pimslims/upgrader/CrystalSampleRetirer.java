/**
 * pims-model org.pimslims.upgrader CrystalSampleRetirer.java
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.sample.CrystalSample;
import org.pimslims.model.sample.Sample;

/**
 * CrystalSampleRetirer
 * 
 */
@SuppressWarnings("deprecation")
public class CrystalSampleRetirer extends ClassRetirer {

    public CrystalSampleRetirer(WritableVersion version2) {
        super(version2, version2.getMetaClass(CrystalSample.class));
    }

    @Override
    public ModelObject retire(ModelObject old) throws ConstraintException, AccessException {
        CrystalSample cs = (CrystalSample) old;
        // prevent name clash
        String name = cs.getName();
        cs.setName(name + "_retired");
        cs.flush();
        Sample sample = new Sample(version, name);

        copyPageAttributes(cs, sample);

        // process the attributes of the target class
        MetaClass toMetaClass = this.version.getMetaClass(Sample.class);
        Set<String> attributes = copyAttributesAndRoles(cs, sample);
        // process the obsolete attributes
        Set<String> oldAttributes = new HashSet(this.metaClass.getAttributes().keySet());
        oldAttributes.removeAll(attributes);
        for (Iterator iterator = oldAttributes.iterator(); iterator.hasNext();) {
            String attributeName = (String) iterator.next();
            this.appendAttribute(cs, attributeName, sample, LabBookEntry.PROP_DETAILS);
        }
        return sample;
    }

}
