package org.pimslims.presentation.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

public class SavedPlateOrderBean implements IPlateOrderBean {

    private final String holderName;

    private final String holdHook;

    private final String plateTypeName;

    private final String plateTypeHook;

    private final String locationName;

    private final String locationHook;

    private final String protocolName;

    private final String protocolHook;

    private final String expGrouphook;

    private final String expGroupName;

    private final Map<String, Object> specifications;

    private final List<SavedOrderBean> savedOrderBeans;

    private Map<File, String> annotations;

    public SavedPlateOrderBean(final Holder holder) {

        final ExperimentGroup expGroup = HolderFactory.getExperimentGroup(holder);
        this.expGrouphook = expGroup.get_Hook();
        this.expGroupName = expGroup.getName();

        final Collection<Annotation> attachedFiles = expGroup.getAnnotations();

        this.annotations = new HashMap<org.pimslims.util.File, String>();

        for (final Annotation file : attachedFiles) {
            // PiMS 1578
            //annotations.put(file.get_Hook(), file.getFilename());
            this.annotations.put(FileImpl.getFile(file), file.getDetails());
        }

        // specifications
        this.specifications = new HashMap<String, Object>();
        for (final Experiment exp : expGroup.getExperiments()) {
            // verify it is an order
            for (final Parameter p : exp.getParameters()) {
                this.specifications.put(p.getName(), p.getValue());
            }
            break;
        }

        // savedOrderBeans
        this.savedOrderBeans = new ArrayList<SavedOrderBean>();
        for (final Experiment exp : expGroup.getExperiments()) {
            final SavedOrderBean savedOrderBean = this.getNewSavedOrderBean(exp);
            savedOrderBean.setPoBean(this);
            this.savedOrderBeans.add(savedOrderBean);

        }
        Collections.sort(this.savedOrderBeans);

        // holder info
        this.holdHook = holder.get_Hook();
        this.holderName = holder.getName();
        if (null == holder.getHolderType()) {
            this.plateTypeHook = "";
            this.plateTypeName = "";
        } else {
            this.plateTypeName = holder.getHolderType().getName();
            this.plateTypeHook = holder.getHolderType().get_Hook();
        }

        // protocol
        if (HolderFactory.getProtocol(holder) != null) {
            final Protocol protocol = HolderFactory.getProtocol(holder);
            this.protocolName = protocol.getName();
            this.protocolHook = protocol.get_Hook();
        } else {
            this.protocolName = null;
            this.protocolHook = null;
        }
        // location & org info
        if (holder.getHolderLocations() == null || holder.getHolderLocations().size() == 0) {
            this.locationName = null;
            this.locationHook = null;
        } else {
            final Location location = ContainerUtility.getCurrentLocation(holder);

            if (location != null) {
                this.locationHook = location.get_Hook();
                this.locationName = location.getName();
            } else {
                this.locationName = null;
                this.locationHook = null;
            }

        }

    }

    protected SavedOrderBean getNewSavedOrderBean(final Experiment exp) {

        return new SavedOrderBean(exp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IPlateOrderBean#getSpecifications()
     */
    public Map<String, Object> getSpecifications() {
        return this.specifications;
    }

    /**
     * @return Returns the holderName.
     */
    public String getHolderName() {
        return this.holderName;
    }

    /**
     * @return Returns the holdHook.
     */
    public String getHoldHook() {
        return this.holdHook;
    }

    /**
     * @return Returns the locationHook.
     */
    public String getLocationHook() {
        return this.locationHook;
    }

    /**
     * @return Returns the locationName.
     */
    public String getLocationName() {
        return this.locationName;
    }

    /**
     * @return Returns the savedOrderBeans.
     */
    public List<SavedOrderBean> getSavedOrderBeans() {
        return this.savedOrderBeans;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IPlateOrderBean#getUnsavedOrderBeans()
     */
    public List<IOrderBean> getOrderBeans() {
        return new ArrayList<IOrderBean>(this.savedOrderBeans);
    }

    public String getProtocolName() {

        return this.protocolName;
    }

    public String getProtocolHook() {
        return this.protocolHook;
    }

    public String getPlateTypeName() {

        return this.plateTypeName;
    }

    public String getPlateTypeHook() {
        return this.plateTypeHook;
    }

    public String getExpGrouphook() {
        return this.expGrouphook;
    }

    public String getExpGroupName() {
        return this.expGroupName;
    }

    /**
     * @return the annotations
     */
    public Map<File, String> getAnnotations() {
        return this.annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(final Map<org.pimslims.util.File, String> annotations) {
        this.annotations = annotations;
    }

}
