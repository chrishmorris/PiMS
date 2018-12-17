/**
 * pims-web org.pimslims.xmlio RefSampleSourceXmlBean.java
 * 
 * @author pajanne
 * @date Jul 15, 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import javax.xml.bind.annotation.XmlAttribute;

import org.pimslims.model.sample.ReagentCatalogueEntry;

/**
 * RefSampleSourceXmlBean
 * 
 */
public class RefSampleSourceXmlBean {

    private String catalogNum;

    private String dataPageUrl;

    private String supplier;

    public RefSampleSourceXmlBean(final ReagentCatalogueEntry refSampleSource) {
        this.catalogNum = refSampleSource.getCatalogNum();
        this.dataPageUrl = refSampleSource.getDataPageUrl();
        this.supplier = refSampleSource.getSupplier().getName();
    }

    // Required by jaxb
    private RefSampleSourceXmlBean() {
        // required by jaxb
    }

    /**
     * @return Returns the catalogNum.
     */
    @XmlAttribute
    public String getCatalogNum() {
        if (null == this.catalogNum) {
            return "unknown";
        }
        return this.catalogNum;
    }

    /**
     * @param catalogNum The catalogNum to set.
     */
    public void setCatalogNum(final String catalogNum) {
        this.catalogNum = catalogNum;
    }

    /**
     * @return Returns the dataPageUrl.
     */
    @XmlAttribute
    public String getDataPageUrl() {
        return this.dataPageUrl;
    }

    /**
     * @param dataPageUrl The dataPageUrl to set.
     */
    public void setDataPageUrl(final String dataPageUrl) {
        this.dataPageUrl = dataPageUrl;
    }

    /**
     * @return Returns the supplier.
     */
    @XmlAttribute
    public String getSupplier() {
        return this.supplier;
    }

    /**
     * @param supplier The supplier to set.
     */
    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

}
