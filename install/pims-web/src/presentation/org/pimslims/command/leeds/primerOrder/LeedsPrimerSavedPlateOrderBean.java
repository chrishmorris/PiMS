package org.pimslims.command.leeds.primerOrder;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.presentation.order.IOrderBean;
import org.pimslims.presentation.order.SavedOrderBean;
import org.pimslims.presentation.order.SavedPlateOrderBean;

public class LeedsPrimerSavedPlateOrderBean extends SavedPlateOrderBean {

    public LeedsPrimerSavedPlateOrderBean(final Holder holder) {
        super(holder);

    }

    public List<LeedsPrimerSavedOrderBean> getLeedsPrimerOrderBeans() {
        final List<LeedsPrimerSavedOrderBean> beans = new ArrayList<LeedsPrimerSavedOrderBean>();
        for (final IOrderBean bean : super.getOrderBeans()) {
            beans.add((LeedsPrimerSavedOrderBean) bean);
        }

        return beans;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.presentation.order.SavedPlateOrderBean#getNewSavedOrderBean(org.pimslims.model.experiment.Experiment)
     */
    @Override
    protected SavedOrderBean getNewSavedOrderBean(final Experiment exp) {
        return new LeedsPrimerSavedOrderBean(exp);

    }

}
