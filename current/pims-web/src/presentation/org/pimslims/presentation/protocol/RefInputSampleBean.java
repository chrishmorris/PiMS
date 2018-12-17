package org.pimslims.presentation.protocol;

import java.text.DecimalFormat;
import java.util.Comparator;

import org.pimslims.lab.Measurement;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * Represents a RefOutputSample used in a protocol.
 * 
 * @author ejd53
 * 
 */
public class RefInputSampleBean extends ModelObjectBean {

    private final Measurement amount;

    private String displayUnit;

    @Deprecated
    // no, close transaction before dispatching JSP
    final Protocol protocol;

    public RefInputSampleBean(final RefInputSample refInputSample) {
        super(refInputSample);
        this.removeValue(RefInputSample.PROP_UNIT);
        this.removeValue(RefInputSample.PROP_DISPLAYUNIT);
        if (null == refInputSample.getAmount()) {
            this.amount = null;
            this.displayUnit = refInputSample.getDisplayUnit();
            if (null == this.displayUnit) {
                this.displayUnit = "uL";
            }
        } else {
            this.amount =
                Measurement.getMeasurement(RefInputSample.PROP_AMOUNT, refInputSample,
                    RefInputSample.PROP_UNIT, RefInputSample.PROP_DISPLAYUNIT);
            this.putValue(RefInputSample.PROP_AMOUNT, this.amount.toString());
            this.displayUnit = this.amount.getDisplayUnit();
        }
        this.protocol = refInputSample.getProtocol();
    }

    public static final Comparator NUMBER_OF_EXPERIMENTS = new Comparator() {
        public int compare(final Object arg0, final Object arg1) {

            final RefInputSampleBean bean0 = (RefInputSampleBean) arg0;
            final RefInputSampleBean bean1 = (RefInputSampleBean) arg1;
            Integer bean0Size = null;
            Integer bean1Size = null;
            if (bean0 != null) {
                bean0Size = bean0.protocol.getExperiments().size();
            }
            if (bean1 != null) {
                bean1Size = bean1.protocol.getExperiments().size();
            }
            if (bean0Size == null) {
                return -1;
            }
            if (bean1Size == null) {
                return 1;
            }
            return bean1Size.compareTo(bean0Size);
        }
    };

    /**
     * @return Returns the name of the RefInputSample.
     */
    @Override
    public String getName() {
        return (String) this.getValues().get(RefInputSample.PROP_NAME);
    }

    /**
     * @return Returns the amount.
     */
    public Measurement getAmount() {
        return this.amount;
    }

    /**
     * @return Returns the amount.
     */
    public String getDisplayUnit() {
        return this.displayUnit;
    }

    /**
     * @return Returns the amount.
     */
    public String getDisplayValue() {
        if (null != this.amount) {
            //return new DecimalFormat("#####0.0#####").format(this.amount.getDisplayValue());
            return new DecimalFormat("#####0.0##").format(this.amount.getDisplayValue());
        }
        return "";
    }

    public ModelObjectShortBean getSampleCategory() {
        return (ModelObjectShortBean) this.getValues().get(RefInputSample.PROP_SAMPLECATEGORY);
    }

    public ModelObjectBean getProtocol() {
        if (null == this.protocol) {
            return null;
        }
        return new ModelObjectBean(this.protocol);
    }

    public ModelObjectBean getProtocolExperimentType() {
        return new ModelObjectBean(this.protocol.getExperimentType());
    }

    public int getProtocolExperiments() {
        return this.protocol.getExperiments().size();
    }

    /**
     * @return Returns the display unit, formatted for HTML display - C as &deg;C, uL as &#181;L, etc.
     * 
     *         public String getHtmlDisplayUnit() { return htmlDisplayUnit; }
     * 
     */

}
