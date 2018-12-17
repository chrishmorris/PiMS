/**
 * V3_1-pims-web org.pimslims.lab.experiment WellBean.java
 * 
 * @author cm65
 * @date 15 Jan 2009
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.experiment;

import java.util.Comparator;

/**
 * WellBean
 * 
 * A bean to represent a particular well. Most methods that use strings actually need this bean, to support
 * multiple plates
 * 
 * TODO bring the methods that calculate row and column here, from HolderFactory
 * 
 */
public class WellBean {

    public static final Comparator<? super WellBean> ROW_ORDER = new Comparator() {

        public int compare(Object arg0, Object arg1) {
            WellBean bean0 = (WellBean) arg0;
            WellBean bean1 = (WellBean) arg1;
            if (!bean0.getHolderName().equals(bean1.getHolderName())) {
                return bean0.getHolderName().compareTo(bean1.getHolderName());
            }
            return bean0.getWell().compareTo(bean1.getWell());
        }
    };

    public static final Comparator<? super WellBean> COLUMN_ORDER = new Comparator() {

        public int compare(Object arg0, Object arg1) {
            WellBean bean0 = (WellBean) arg0;
            WellBean bean1 = (WellBean) arg1;
            if (!bean0.getHolderName().equals(bean1.getHolderName())) {
                return bean0.getHolderName().compareTo(bean1.getHolderName());
            }
            if (bean0.getColumn() == bean1.getColumn()) {
                return bean0.getWell().compareTo(bean1.getWell());
            }
            return bean0.getColumn().compareTo(bean1.getColumn());
        }
    };

    /**
     * holderName String the bar code of the plate
     */
    private final String holderName;

    /**
     * well String e.g. A01
     */
    private final String well;

    public WellBean(final String holderName, final String well) {
        super();
        this.holderName = holderName;
        this.well = well;
    }

    /**
     * WellBean.getColumn
     * 
     * @return
     */
    protected Integer getColumn() {
        return 1 + HolderFactory.getColumn(this.well);
    }

    public String getHolderName() {
        return this.holderName;
    }

    public String getWell() {
        return this.well;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof WellBean)) {
            return false;
        }
        final WellBean other = (WellBean) obj;
        return this.holderName.equals(other.holderName) && this.well.equals(other.well);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 2334 + this.holderName.hashCode() + 97 * this.well.hashCode();
    }

}
