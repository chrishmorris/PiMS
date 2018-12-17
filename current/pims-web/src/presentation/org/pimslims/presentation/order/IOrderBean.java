package org.pimslims.presentation.order;

public interface IOrderBean {

    public abstract IPlateOrderBean getPoBean();

    public abstract String getTargetName();

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getColPosition()
     */
    public abstract int getColPosition();

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getIsForwardDirection()
     */
    public abstract Boolean getIsForwardDirection();

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getRowPosition()
     */
    public abstract int getRowPosition();

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getSequence()
     */
    public abstract String getSequence();

    public abstract String getSampleName();

    public abstract String getComments();

}
