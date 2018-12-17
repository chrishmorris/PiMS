package org.pimslims.presentation.protocol;

import java.text.DecimalFormat;

import org.pimslims.lab.Measurement;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * Represents a RefOutputSample used in a protocol.
 * 
 * @author ejd53
 * 
 */
public class RefOutputSampleBean extends ModelObjectBean {

    private final Measurement amount;

    final Protocol protocol;

    public RefOutputSampleBean(final RefOutputSample refOutputSample) {
        super(refOutputSample);
        this.removeValue(RefOutputSample.PROP_UNIT);
        this.removeValue(RefOutputSample.PROP_DISPLAYUNIT);
        if (null == refOutputSample.getAmount()) {
            this.amount = null;
        } else {
            this.amount =
                Measurement.getMeasurement(RefOutputSample.PROP_AMOUNT, refOutputSample,
                    RefOutputSample.PROP_UNIT, RefOutputSample.PROP_DISPLAYUNIT);
            this.putValue(RefOutputSample.PROP_AMOUNT, this.amount.toString());
        }
        this.protocol = refOutputSample.getProtocol();
    }

    /**
     * @return Returns the name of the RefInputSample.
     */
    @Override
    public String getName() {
        return (String) this.getValues().get(RefOutputSample.PROP_NAME);
    }

    /**
     * @return Returns the amount.
     */
    public Measurement getAmount() {
        return this.amount;
    }

    public ModelObjectShortBean getSampleCategory() {
        return (ModelObjectShortBean) this.getValues().get(RefOutputSample.PROP_SAMPLECATEGORY);
    }

    /**
     * @return Returns the amount.
     */
    public String getDisplayUnit() {
        if (null != this.amount) {
            return this.amount.getDisplayUnit();
        }
        return "";
    }

    /**
     * @return Returns the amount.
     */
    public String getDisplayValue() {
        if (null != this.amount) {
            return new DecimalFormat("#####0.0#####").format(this.amount.getDisplayValue());
        }
        return "";
    }

    public ModelObjectBean getProtocol() {
        if (null == this.protocol) {
            return null;
        }
        return new ModelObjectBean(this.protocol);
    }

    /**
     * @return Returns the display unit, formatted for HTML display - C as &deg;C, uL as &#181;L, etc.
     * 
     *         public String getHtmlDisplayUnit() { return htmlDisplayUnit; }
     */

}
