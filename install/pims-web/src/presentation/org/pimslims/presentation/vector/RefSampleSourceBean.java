/**
 * pims-web org.pimslims.presentation.vector RefSampleSourceBean.java
 * 
 * @author pajanne
 * @date Jun 3, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.presentation.ModelObjectBean;

/**
 * RefSampleSourceBean
 * 
 */
public class RefSampleSourceBean extends ModelObjectBean {

    private String catalogNum;

    private String productName;

    private String dataPageUrl;

    private Organisation supplier;

    /**
     * 
     * Constructor for RefSampleSourceBean
     * 
     * @param source
     */
    public RefSampleSourceBean(final ReagentCatalogueEntry source) {
        super(source);
        this.catalogNum = source.getCatalogNum();
        this.productName = source.getProductName();
        this.dataPageUrl = source.getDataPageUrl();
        this.supplier = source.getSupplier();
    }

    /**
     * @return Returns the catalogNum.
     */
    public String getCatalogNum() {
        return this.catalogNum;
    }

    /**
     * @param catalogNum The catalogNum to set.
     */
    public void setCatalogNum(final String catalogNum) {
        this.catalogNum = catalogNum;
    }

    /**
     * @return Returns the productName.
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * @param productName The productName to set.
     */
    public void setProductName(final String productName) {
        this.productName = productName;
    }

    /**
     * @return Returns the dataPageUrl.
     */
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
    public Organisation getSupplier() {
        return this.supplier;
    }

    /**
     * @param supplier The supplier to set.
     */
    public void setSupplier(final Organisation supplier) {
        this.supplier = supplier;
    }

}
