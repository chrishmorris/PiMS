/**
 * pims-web org.pimslims.xmlio RefSampleXmlBean.java
 * 
 * @author pajanne
 * @date Jan 14, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;

/**
 * RefSampleXmlBean
 * 
 */
@XmlType(name = "", propOrder = { "sampleCategory", "name", "amount", "unit", "displayUnit" })
public class RefSampleXmlBean {

    private String sampleCategory;

    private String name;

    private Float amount;

    private String unit;

    private String displayUnit;

    /**
     * Constructor for RefSampleXmlBean
     * 
     * @param org.pimslims.model.protocol.RefInputSample
     */
    public RefSampleXmlBean(final RefInputSample refInputSample) {
        this.sampleCategory = refInputSample.getSampleCategory().getName();
        this.name = refInputSample.getName();
        this.amount = refInputSample.getAmount();
        this.unit = refInputSample.getUnit();
        this.displayUnit = refInputSample.getDisplayUnit();
    }

    /**
     * Constructor for RefSampleXmlBean
     * 
     * @param org.pimslims.model.protocol.RefOutputSample
     */
    public RefSampleXmlBean(final RefOutputSample refOutputSample) {
        this.sampleCategory = refOutputSample.getSampleCategory().getName();
        this.name = refOutputSample.getName();
        this.amount = refOutputSample.getAmount();
        this.unit = refOutputSample.getUnit();
        this.displayUnit = refOutputSample.getDisplayUnit();
    }

    // Required by jaxb
    private RefSampleXmlBean() {
        // Required by jaxb
    }

    /**
     * @return Returns the sampleCategory.
     */
    public String getSampleCategory() {
        return this.sampleCategory;
    }

    /**
     * @param sampleCategory The sampleCategory to set.
     */
    public void setSampleCategory(final String sampleCategory) {
        this.sampleCategory = sampleCategory;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the amount.
     */
    public Float getAmount() {
        return this.amount;
    }

    /**
     * @param amount The amount to set.
     */
    public void setAmount(final Float amount) {
        this.amount = amount;
    }

    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @param unit The unit to set.
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

    /**
     * @return Returns the displayUnit.
     */
    public String getDisplayUnit() {
        return this.displayUnit;
    }

    /**
     * @param displayUnit The displayUnit to set.
     */
    public void setDisplayUnit(final String displayUnit) {
        this.displayUnit = displayUnit;
    }

    /**
     * Returns RefInput/OutputSample attribute map for creating associated model object.
     * 
     * @param version
     * @return attribute map
     */
    public Map<String, Object> getAttributeMap(final WritableVersion version) {
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put(RefOutputSample.PROP_SAMPLECATEGORY, this.getSampleCategory(version));
        attributeMap.put(RefOutputSample.PROP_NAME, this.name);
        attributeMap.put(RefOutputSample.PROP_AMOUNT, this.amount);
        attributeMap.put(RefOutputSample.PROP_UNIT, this.unit);
        attributeMap.put(RefOutputSample.PROP_DISPLAYUNIT, this.displayUnit);
        return attributeMap;
    }

    /**
     * Returns the associated sample category model object from a string. It should already exist within the
     * db, and be unique otherwise throws an error.
     * 
     * @param version
     * @return the associated sample category
     */
    private SampleCategory getSampleCategory(final WritableVersion version) {
        final Collection<SampleCategory> sampleCategoryFound =
            version.findAll(SampleCategory.class, SampleCategory.PROP_NAME, this.sampleCategory);
        if (0 == sampleCategoryFound.size()) {
            throw new RuntimeException("ERROR: SampleCategory [" + this.sampleCategory
                + "] must exist (loaded as reference)");
        } else if (1 == sampleCategoryFound.size()) {
            return sampleCategoryFound.iterator().next();
        } else {
            throw new RuntimeException("ERROR: SampleCategory [" + this.sampleCategory
                + "] is duplicated (reference must be unique)");
        }
    }
}
