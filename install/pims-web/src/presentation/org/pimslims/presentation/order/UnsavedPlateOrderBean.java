package org.pimslims.presentation.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pimslims.util.File;

public class UnsavedPlateOrderBean implements IPlateOrderBean {

    @Deprecated
    // should this be holderName?
    String locationName;

    String protocolName;

    String holderName;

    String plateTypeName;

    List<UnsavedOrderBean> unsavedOrderBeans;

    Map<File, String> annotations; // hook->name

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IPlateOrderBean#getLocation()
     */
    @Deprecated
    // should this be holderName?
    public String getLocationName() {
        return this.locationName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IPlateOrderBean#getUnsavedOrderBeans()
     */
    public List<IOrderBean> getOrderBeans() {
        return new ArrayList<IOrderBean>(this.unsavedOrderBeans);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IPlateOrderBean#setUnsavedOrderBeans(java.util.Collection)
     */
    public void setOrderBeans(final List<IOrderBean> unsavedOrderBeans) {
        this.unsavedOrderBeans = new ArrayList<UnsavedOrderBean>();
        for (final IOrderBean unsaved : unsavedOrderBeans) {
            this.unsavedOrderBeans.add((UnsavedOrderBean) unsaved);
        }
    }

    public String getHolderName() {
        return this.holderName;
    }

    public void setHolderName(final String orderName) {
        this.holderName = orderName;
    }

    public String getPlateTypeName() {
        return this.plateTypeName;
    }

    public void setPlateTypeName(final String plateTypeName) {
        this.plateTypeName = plateTypeName;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setProtocolName(final String protocolName) {
        this.protocolName = protocolName;
    }

    public UnsavedOrderBean getOrderByPosition(final int row, final int col) {
        for (final UnsavedOrderBean unsaved : this.unsavedOrderBeans) {
            if (unsaved.getColPosition() == col && unsaved.getRowPosition() == row) {
                return unsaved;
            }
        }
        return null;
    }

    /**
     * @return the annotations
     */
    public Map<org.pimslims.util.File, String> getAnnotations() {
        return this.annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(final Map<org.pimslims.util.File, String> annotations) {
        this.annotations = annotations;
    }

}
