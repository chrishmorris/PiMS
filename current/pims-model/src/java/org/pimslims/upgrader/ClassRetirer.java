/**
 * pims-model org.pimslims.upgrader ClassRetirer.java
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.search.Paging;

/**
 * ClassRetirer Utility to copy data from an obsolete table
 */
public abstract class ClassRetirer {

    protected final WritableVersion version;

    protected final MetaClass metaClass;

    /**
     * Constructor for ClassRetirer
     * 
     * @param version2
     * @param metaClass2
     */
    public ClassRetirer(WritableVersion version2, MetaClass metaClass2) {
        this.version = version2;
        this.metaClass = metaClass2;
    }

    /**
     * ClassRetirer.retire
     * 
     * @param location
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public abstract ModelObject retire(ModelObject old) throws ConstraintException, AccessException;

    protected void copyPageAttributes(LabBookEntry from, LabBookEntry to) throws AccessException,
        ConstraintException {
        // copy all LabNotebook properties
        this.copyAttribute(from, LabBookEntry.PROP_DETAILS, to, LabBookEntry.PROP_DETAILS);
        this.copyAttribute(from, LabBookEntry.PROP_PAGE_NUMBER, to, LabBookEntry.PROP_PAGE_NUMBER);
        this.copyAttribute(from, LabBookEntry.PROP_CREATIONDATE, to, LabBookEntry.PROP_CREATIONDATE);
        this.copyAttribute(from, LabBookEntry.PROP_CREATOR, to, LabBookEntry.PROP_CREATOR);
        this.copyAttribute(from, LabBookEntry.PROP_LASTEDITEDDATE, to, LabBookEntry.PROP_LASTEDITEDDATE);
        this.copyAttribute(from, LabBookEntry.PROP_LASTEDITOR, to, LabBookEntry.PROP_LASTEDITOR);

        Set<Attachment> annotations = from.getAttachments();
        for (Iterator iterator = annotations.iterator(); iterator.hasNext();) {
            Attachment annotation = (Attachment) iterator.next();
            annotation.setParentEntry(to);
        }
    }

    /**
     * LocationRetirer.copyAttribute
     * 
     * @param location
     * @param propDetails
     * @param replacement
     * @param propDetails2
     * @throws ConstraintException
     * @throws AccessException
     */
    protected void copyAttribute(ModelObject old, String attr1, ModelObject replacement, String attr2)
        throws AccessException, ConstraintException {
        replacement.set_Value(attr2, old.get_Value(attr1));
    }

    protected void appendAttribute(ModelObject old, String attr1, ModelObject replacement, String attr2)
        throws AccessException, ConstraintException {
        Object value = old.get_Value(attr1);
        if (null != value && !"".equals(value)) {
            replacement.set_Value(attr2, replacement.get_Value(attr2) + " " + attr1 + ": " + value);
        }
    }

    /**
     * LocationRetirer.retireAll TODO we may need a commit within the loop, to avoid OutOfMemoryError
     * 
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public void retireAll() throws ConstraintException, AccessException {
        int i = 0;
        while (true) {
            Collection locations =
                this.version.findAll(this.metaClass.getJavaClass(), java.util.Collections.EMPTY_MAP,
                    new Paging(i, 100));
            if (locations.isEmpty()) {
                break;
            }
            for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
                ModelObject location = (ModelObject) iterator.next();
                this.retire(location);
                i++;
            }
            this.version.flush();
        }
    }

    /**
     * CrystalSampleRetirer.copyAttributesAndRoles
     * 
     * @param from
     * @param to
     * @param toMetaClass
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    protected Set<String> copyAttributesAndRoles(ModelObject from, ModelObject to) throws AccessException,
        ConstraintException {
            MetaClass toMetaClass = to.get_MetaClass();
            Set<String> attributes = toMetaClass.getAttributes().keySet();
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                String attributeName = (String) iterator.next();
                if (!"name".equals(attributeName)) {
                    this.copyAttribute(from, attributeName, to, attributeName);
                }
            }
            //process the roles
            Set<String> roles = toMetaClass.getMetaRoles().keySet();
            for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
                String attributeName = (String) iterator.next();
                this.copyAttribute(from, attributeName, to, attributeName);
            }
            return attributes;
        }

}
