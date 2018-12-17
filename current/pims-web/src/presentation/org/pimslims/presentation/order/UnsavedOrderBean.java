package org.pimslims.presentation.order;

import org.pimslims.lab.experiment.HolderFactory;

public class UnsavedOrderBean implements IOrderBean {

    private String targetName;

    private String sampleName;

    private Boolean isForwardDirection;

    private String sequence;

    private int rowPosition;

    private int colPosition;

    private String comments;

    private IPlateOrderBean poBean;

    private UnsavedOrderBean pairedOrder;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getPoBean()
     */
    public IPlateOrderBean getPoBean() {
        return poBean;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setPoBean(org.pimslims.presentation.order.IPlateOrderBean)
     */
    public void setPoBean(IPlateOrderBean poBean) {
        this.poBean = poBean;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getTargetName()
     */
    public String getTargetName() {
        return targetName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setTargetHook(java.lang.String)
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getColPosition()
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getColPosition()
     */
    public int getColPosition() {
        return colPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setColPosition(int)
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setColPosition(int)
     */
    public void setColPosition(int colPosition) {
        this.colPosition = colPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getIsForwardDirection()
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getIsForwardDirection()
     */
    public Boolean getIsForwardDirection() {
        return isForwardDirection;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setIsForwardDirection(java.lang.Boolean)
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setIsForwardDirection(java.lang.Boolean)
     */
    public void setIsForwardDirection(Boolean isForwardDirection) {
        this.isForwardDirection = isForwardDirection;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getRowPosition()
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getRowPosition()
     */
    public int getRowPosition() {
        return rowPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setRowPosition(int)
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setRowPosition(int)
     */
    public void setRowPosition(int rowPosition) {
        this.rowPosition = rowPosition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getSequence()
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#getSequence()
     */
    public String getSequence() {
        return sequence;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setSequence(java.lang.String)
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.IOrderBean#setSequence(java.lang.String)
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the sampleName
     */
    public String getSampleName() {
        return sampleName;
    }

    /**
     * @param sampleName the sampleName to set
     */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getRowPositionInLetter() {
        return HolderFactory.getRow(rowPosition - 1);
    }

    /**
     * @return the pairedOrder
     */
    public UnsavedOrderBean getPairedOrder() {
        return pairedOrder;
    }

    /**
     * @param pairedOrder the pairedOrder to set
     */
    public void setPairedOrder(UnsavedOrderBean pairedOrder) {
        this.pairedOrder = pairedOrder;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

}
