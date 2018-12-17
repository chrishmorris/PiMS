/**
 * pims-web org.pimslims.presentation.vector RefSampleBean.java
 * 
 * @author pajanne
 * @date May 28, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.ModelObjectBean;

/**
 * RefSampleBean
 * 
 */
public class RefSampleBean extends ModelObjectBean {

    private String name;

    private String details;

    private Set<SampleCategory> sampleCategories;

    private List<RefSampleSourceBean> refSampleSources;

    private List<SampleComponentBean> sampleComponents;

    /**
     * 
     * Constructor for RefSampleBean
     * 
     * @param refSample
     */
    public RefSampleBean(final RefSample refSample) {
        super(refSample);
        this.name = refSample.getName();
        this.details = refSample.getDetails();
        this.sampleCategories = new HashSet<SampleCategory>(0);
        if (refSample.getSampleCategories().size() != 0) {
            this.sampleCategories.addAll(refSample.getSampleCategories());
        }
        this.refSampleSources = new ArrayList<RefSampleSourceBean>(0);
        if (refSample.getRefSampleSources().size() != 0) {
            for (final ReagentCatalogueEntry source : refSample.getRefSampleSources()) {
                this.refSampleSources.add(new RefSampleSourceBean(source));
            }
        }
        this.sampleComponents = new ArrayList<SampleComponentBean>(0);
        if (refSample.getSampleComponents().size() != 0) {
            for (final SampleComponent component : refSample.getSampleComponents()) {
                this.sampleComponents.add(new SampleComponentBean(component));
            }
        }

    }

    /**
     * @return Returns the name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * @return Returns the sampleCategories.
     */
    public Set<SampleCategory> getSampleCategories() {
        return this.sampleCategories;
    }

    /**
     * @param sampleCategories The sampleCategories to set.
     */
    public void setSampleCategories(final Set<SampleCategory> sampleCategories) {
        this.sampleCategories = sampleCategories;
    }

    /**
     * @return Returns the refSampleSources.
     */
    public List<RefSampleSourceBean> getRefSampleSources() {
        return this.refSampleSources;
    }

    /**
     * @param refSampleSources The refSampleSources to set.
     */
    public void setRefSampleSources(final List<RefSampleSourceBean> refSampleSources) {
        this.refSampleSources = refSampleSources;
    }

    /**
     * @return Returns the sampleComponents.
     */
    public List<SampleComponentBean> getSampleComponents() {
        return this.sampleComponents;
    }

    /**
     * @param sampleComponents The sampleComponents to set.
     */
    public void setSampleComponents(final List<SampleComponentBean> sampleComponents) {
        this.sampleComponents = sampleComponents;
    }

}
