/**
 * 
 */
package org.pimslims.presentation.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hibernate.FlushMode;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.target.Target;

/**
 * @author bl67
 * 
 */
public abstract class PlateOrderCreator {

    // static final String expTypeName="Order";
    static final String generalPurpose = "Order a plate of samples";

    static final String generalDetails = "Order sample";

    static final String sampleCategoryName = "Primer";

    /**
     * 
     */
    public PlateOrderCreator() {
        super();

    }

    /**
     * create unsavedPlateOrderBean by using some abstract functions
     * 
     * @return
     */
    public UnsavedPlateOrderBean getUnsavedPlateOrderBean() {
        UnsavedPlateOrderBean unsavedPlateOrderBean = null;
        // else
        // create platebean
        unsavedPlateOrderBean = new UnsavedPlateOrderBean();
        // init platebean
        this.InitUnsavedPlateOrderBean(unsavedPlateOrderBean);

        // create order beans
        final List<UnsavedOrderBean> unsavedOrderBeans = this.getUnsavedOrderBeans();

        // link order beans and platebean
        unsavedPlateOrderBean.setOrderBeans(new ArrayList<IOrderBean>(unsavedOrderBeans));
        for (final UnsavedOrderBean unsavedOrderBean : unsavedOrderBeans) {
            unsavedOrderBean.setPoBean(unsavedPlateOrderBean);
        }
        return unsavedPlateOrderBean;
    }

    @Deprecated
    // not used
    public SavedPlateOrderBean SavePlateOrder(final WritableVersion wv,
        final UnsavedPlateOrderBean unsavedPlateOrderBean) throws ConstraintException, AccessException {
        // create hold
        final Holder holder = PlateOrderCreator.createHolder(wv, unsavedPlateOrderBean);
        holder.setName(holder.get_Name() + " " + holder.getDbId());

        // create experiment Group contains exps
        this.CreateExpGroup(wv, holder, unsavedPlateOrderBean);

        this.processSavedOrders(wv, holder, unsavedPlateOrderBean);

        return this.getSavedPlateOrderBean(holder);
    }

    /**
     * @param wv
     * @param holder
     * @param unsavedPlateOrderBean
     * @throws ConstraintException
     * @throws AccessException
     */
    abstract protected void processSavedOrders(WritableVersion wv, Holder holder,
        UnsavedPlateOrderBean unsavedPlateOrderBean) throws ConstraintException, AccessException;

    protected static void LinkExperimentWithTarget(final WritableVersion wv, final Holder holder,
        final UnsavedPlateOrderBean unsavedPlateOrderBean) throws ConstraintException {
        final FlushMode oldFlushMode = wv.getSession().getFlushMode(); //TODO version.setFlushModeCommit();
        wv.getSession().setFlushMode(FlushMode.COMMIT);
        final ExperimentGroup expGroup = HolderFactory.getExperimentGroup(holder);
        final Collection<Experiment> exps = expGroup.getExperiments();
        for (final Experiment exp : exps) {
            final int col = HolderFactory.getColumn(exp) + 1; // start from 1 not 0
            final int row = HolderFactory.getRow(exp) + 1; // start from 1 not 0
            // find the target
            final UnsavedOrderBean unsavedOrder = unsavedPlateOrderBean.getOrderByPosition(row, col);
            final String targetName = unsavedOrder.getTargetName();
            final Target t = TargetUtility.searchForTargetByNames(wv, targetName);
            assert t != null : "Can not find the target:" + targetName;
            // link exp with target
            assert t.getResearchObjectiveElements() != null : "this target does not have blueprintComponent, please create one for it:"
                + targetName;
            assert t.getResearchObjectiveElements().size() > 0 : "this target does not have blueprintComponent, please create one for it:"
                + targetName;
            assert t.getResearchObjectiveElements().iterator().next().getResearchObjective() != null : "this target's  blueprintComponent does not have ExpBlueprint, please create one for it:"
                + targetName;
            TargetUtility.setTarget(t, exp);

        }
        wv.getSession().setFlushMode(oldFlushMode);
    }

    private ExperimentGroup CreateExpGroup(final WritableVersion wv, final Holder holder,
        final UnsavedPlateOrderBean unsavedPlateOrderBean) throws ConstraintException, AccessException {
        // protocol
        final String protocolName = unsavedPlateOrderBean.getProtocolName();
        final Protocol protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        assert protocol != null : "Can not find the protocol:" + "" + protocolName;
        // expGroup details
        final String expGroupName = holder.getName();
        final String purpose = PlateOrderCreator.generalPurpose;

        // exp type
        final Collection<ExperimentType> types = new HashSet<ExperimentType>();
        final ExperimentType expType =
            wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, this.getExpTypeName());
        assert expType != null : "Can not find the ExperimentType which name is:" + this.getExpTypeName();
        types.add(expType);

        // exp dates
        final Calendar startDate = Calendar.getInstance();
        final Calendar endDate = Calendar.getInstance();

        // exp details
        final String details = PlateOrderCreator.generalDetails;

        return HolderFactory.createPlateExperiment(wv, null, holder, expGroupName, purpose, types, startDate,
            endDate, details, protocol, // SampleCategory will be set later
            Collections.EMPTY_MAP, null);

    }

    @Deprecated
    // not used
    private static Holder createHolder(final WritableVersion wv,
        final UnsavedPlateOrderBean unsavedPlateOrderBean) throws ConstraintException, AccessException {
        final String holderName = unsavedPlateOrderBean.getHolderName();
        final String holderTypeName = unsavedPlateOrderBean.getPlateTypeName();
        final HolderType ht = wv.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, holderTypeName);
        assert ht != null : "Can not find the holderType which name is:" + holderTypeName;
        // create hold
        final Holder holder = HolderFactory.createHolder(wv, null, holderName, ht, Collections.EMPTY_MAP);

        // create location
        Holder location =
            wv.findFirst(Holder.class, Location.PROP_NAME, unsavedPlateOrderBean.getLocationName());
        if (location == null) {
            location = new Holder(wv, unsavedPlateOrderBean.getLocationName());
        }
        holder.setParentHolder(location);

        return holder;
    }

    abstract protected String getExpTypeName();

    abstract protected List<UnsavedOrderBean> getUnsavedOrderBeans();

    abstract protected void InitUnsavedPlateOrderBean(UnsavedPlateOrderBean unsavedPOB);

    abstract protected SavedPlateOrderBean getSavedPlateOrderBean(Holder holder);

}
