/**
 * org.pimslims.upgrader RefDataTool.java
 * 
 * @date 12-Dec-2006 10:11:23
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.upgrader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;

/**
 * RefDataTool
 * 
 */
public class RefDataTool {
    private final AbstractModel model = ModelImpl.getModel();

    private final String defaultUserName = AbstractModel.SUPERUSER;

    private final String defaultNameing = "PIMS Ref 1.0";

    private final String refclass = "refClassName";

    private final String linkedRole = "linkedRoleName";

    private final List<Map> refDatas = new LinkedList<Map>();

    /**
     * 
     */
    public RefDataTool() {
        super();
        init();

    }

    private void init() {
        final Map<String, Object> refProteinComponentcategory = new HashMap<String, Object>();
        refProteinComponentcategory.put(this.refclass, ComponentCategory.class.getName());
        //refProteinComponentcategory.put(this.linkedRole, ComponentCategory.PROP_COMPONENTS);
        refProteinComponentcategory.put(ComponentCategory.PROP_NAME, "Protein");
        refProteinComponentcategory.put(PublicEntry.PROP_DETAILS, "PIMS default protein category");
        //refProteinComponentcategory.put(ComponentCategory.PROP_NAMINGSYSTEM, this.defaultNameing);
        this.refDatas.add(refProteinComponentcategory);

        final Map<String, Object> refNAComponentcategory = new HashMap<String, Object>();
        refNAComponentcategory.put(this.refclass, ComponentCategory.class.getName());
        //refNAComponentcategory.put(this.linkedRole, ComponentCategory.PROP_COMPONENTS);
        refNAComponentcategory.put(ComponentCategory.PROP_NAME, "Nucleic acid");
        refNAComponentcategory.put(PublicEntry.PROP_DETAILS, "PIMS default nucleic acid category");
        //refNAComponentcategory.put(ComponentCategory.PROP_NAMINGSYSTEM, this.defaultNameing);
        this.refDatas.add(refNAComponentcategory);

        final Map<String, Object> GenBankDBName = new HashMap<String, Object>();
        GenBankDBName.put(this.refclass, Database.class.getName());
        //GenBankDBName.put(this.linkedRole, DbName.PROP_DBREFS);
        GenBankDBName.put(Database.PROP_NAME, "GenBank");
        GenBankDBName.put(PublicEntry.PROP_DETAILS,
            "Entrez nucleotide sequences from several sources, including GenBank, RefSeq, and PDB");
        GenBankDBName.put(Database.PROP_FULLNAME, "Entrez Nucleotides database");
        GenBankDBName.put(Database.PROP_URL, "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Nucleotide");
        this.refDatas.add(GenBankDBName);

        final Map<String, Object> unspecifiedDBName = new HashMap<String, Object>();
        unspecifiedDBName.put(this.refclass, Database.class.getName());
        //unspecifiedDBName.put(this.linkedRole, DbName.PROP_DBREFS);
        unspecifiedDBName.put(Database.PROP_NAME, "unspecified");
        unspecifiedDBName.put(Database.PROP_FULLNAME, "unspecified");
        this.refDatas.add(unspecifiedDBName);
    }

    /**
     * return the ref datas exist and correct or not
     */
    public boolean isRefDatasValid() {
        boolean isValid = true;
        final ReadableVersion rv = this.model.getReadableVersion(AbstractModel.SUPERUSER);
        try {
            for (final Map<String, String> refDataMap : this.refDatas) {
                if (!isRefDataValid(rv, refDataMap)) {
                    isValid = false;
                }
            }
            rv.commit();
        } catch (final ModelException e) {
            log(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        if (!isValid) {
            log("Invalid ref data found!\n");
        }
        return isValid;
    }

    /**
     * @param wv
     * @param refDataMap
     * @return
     */
    private boolean isRefDataValid(final ReadableVersion rv, final Map<String, String> refDataMap) {
        //((ReadableVersionImpl) rv).setSearchCaseSensitive(false);
        boolean isValid = true;
        // Map<String,Object> attributeMap=getAttributeMap(refDataMap);
        try {
            final String className = refDataMap.get(this.refclass);
            final String refName = refDataMap.get(Database.PROP_NAME);
            final Collection<? extends ModelObject> refObjects =
                rv.findAll((Class.forName(className).asSubclass(ModelObject.class)), Database.PROP_NAME,
                    refName);
            // only one record should be found
            if (refObjects.size() != 1) {
                isValid = false;
            }
            if (refObjects.size() == 0) {
                log("No ref record found for " + className + ": name=" + refName);
            } else if (refObjects.size() > 1) {
                log("Too many(" + refObjects.size() + ") ref record found for " + className + ": name="
                    + refName);
            }

            for (final ModelObject refObject : refObjects) {
                // owner should be reference
                if (refObject.get_Owner() != null
                    && !refObject.get_Owner().equalsIgnoreCase(Access.REFERENCE)) {
                    log("The owner(" + refObject.get_Owner() + ") is not reference for " + className
                        + ": name=" + refName);
                    isValid = false;
                }

            }
        } catch (final ClassNotFoundException e) {
            log(e.getMessage());
            throw new RuntimeException(e);
        }

        return isValid;
    }

    /**
     * @param string
     */
    private void log(final String string) {
        System.out.println("RefDataTool: " + string);

    }

    private Map<String, Object> getAttributeMap(final Map<String, String> refDataMap) {
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.putAll(refDataMap);
        attributeMap.remove(this.refclass);
        attributeMap.remove(this.linkedRole);
        return attributeMap;
    }

    /**
     * RefDataTool.renameSampleCategories
     * 
     * @param version
     * @param string
     * @param string2
     */
    public static void renameSampleCategory(String oldName, String newName, WritableVersion version) {

        try {
            SampleCategory newCategory =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, newName);
            if (null == newCategory) {
                SampleCategory old =
                    version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, oldName);
                if (null == old) {
                    new SampleCategory(version, newName);
                    return;
                }
                old.setName(newName);
                return;
            }

            SampleCategory old = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, oldName);
            if (null != old) {
                // both categories exist, must merge
                Collection<Sample> samples =
                    version.findAll(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, old);
                for (Iterator iterator = samples.iterator(); iterator.hasNext();) {
                    Sample sample = (Sample) iterator.next();
                    sample.addSampleCategory(newCategory);
                    sample.removeSampleCategory(old);
                }
                old.delete();
            }
        } catch (ConstraintException e) {
            // should not happen
            throw new RuntimeException(e);
        } catch (AccessException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }
}
