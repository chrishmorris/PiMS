/**
 * current-pims-web org.pimslims.utils.sequenator SequencingOrderWriter.java
 * 
 * @author pvt43
 * @date 19 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.management.InvalidAttributeValueException;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.utils.experiment.ExperimentWizardCreator;
import org.pimslims.utils.experiment.ExperimentWizardFieldsDecoder;
import org.pimslims.utils.experiment.Utils;

/**
 * SequencingOrderWriter
 * 
 * Class for recording new sequencing orders.
 * 
 */
public class SequencingOrderWriter {

    ArrayList<SequencingOrderBean> orders;

    public SequencingOrderWriter(final ArrayList<SequencingOrderBean> sorders) {
        this.orders = sorders;
    }

    public SequencingOrderWriter(final SequencingOrderBean sorder) {
        this.orders = new ArrayList<SequencingOrderBean>();
        this.orders.add(sorder);
    }

    public static class SequencingOrderBean {

        String PIName;

        String accountNumber;

        String department;

        String userName;

        String userPhone;

        String userEmail;

        String tName;

        String pName;

        String description;

        /*
         * These are numbers but will be converted later
        */
        String tConc;

        String tVolume;

        String tLength;

        String tReqReadLength;

        String pVolume;

        String pConc;

        String returnSample;

        String cdProvided;

        public boolean validateData() throws InvalidAttributeValueException {

            if (Util.isEmpty(this.PIName)) {
                throw new InvalidAttributeValueException("PI name cannot be empty");
            }
            if (Util.isEmpty(this.accountNumber)) {
                throw new InvalidAttributeValueException("Account number cannot be empty");
            }
            if (Util.isEmpty(this.userName)) {
                throw new InvalidAttributeValueException("User name cannot be empty");
            }
            if (Util.isEmpty(this.userEmail)) {
                throw new InvalidAttributeValueException("User email cannot be empty");
            }
            if (Util.isEmpty(this.tName)) {
                throw new InvalidAttributeValueException("Template name cannot be empty");
            }
            if (Util.isEmpty(this.tConc)) {
                throw new InvalidAttributeValueException("Template concentration cannot be empty");
            }
            if (Util.isEmpty(this.pName)) {
                throw new InvalidAttributeValueException("Primer name cannot be empty");
            }
            if (Util.isEmpty(this.pConc)) {
                throw new InvalidAttributeValueException("Primer concentration cannot be empty");
            }
            // should Validate that pname+tname+usersurname is unique and has not be recored as experiment already

            return true;
        }

        public Experiment record(final String orderId, final WritableVersion rw)
            throws InvalidAttributeValueException, AccessException, ConstraintException {
            // Call for data validation prior writing
            this.validateData();
            final Protocol protocol = Utils.getSOProtocol(rw);

            final String prefix = ExperimentWizardFieldsDecoder.getPropertyPrefix("E1", protocol.get_Hook());

            final HashMap<String, String> expParam = new HashMap<String, String>();

            expParam.put(prefix + "P.Template Name", this.tName);
            expParam.put(prefix + "P.Template Concentration", this.tConc);
            expParam.put(prefix + "P.Template Volume", this.tVolume);
            expParam.put(prefix + "P.Template Length", this.tLength);
            expParam.put(prefix + "P.Required Read Length", this.tReqReadLength);

            expParam.put(prefix + "P.Primer Name", this.pName);
            expParam.put(prefix + "P.Primer Concentration", this.pConc);
            expParam.put(prefix + "P.Primer Volume", this.pVolume);
            expParam.put(prefix + "P.Return Samples?", (this.returnSample != null && (this.returnSample
                .trim().equalsIgnoreCase("Yes") || this.returnSample.trim().equalsIgnoreCase("Y"))) ? "Yes"
                : "No");
            expParam.put(prefix + "P.CD Provided?", (this.cdProvided != null && (this.cdProvided.trim()
                .equalsIgnoreCase("Yes") || this.cdProvided.trim().equalsIgnoreCase("Y"))) ? "Yes" : "No");

            /* User details have to go here due to the DM changes in 3.0 */
            expParam.put(prefix + "P.User", this.userName);
            expParam.put(prefix + "P.User Email", this.userEmail);
            expParam.put(prefix + "P.User Phone", this.userPhone);
            expParam.put(prefix + "P.Department", this.department);
            /* PI details have to go here due to the DM changes in 3.0 */
            expParam.put(prefix + "P.Principal Investigator", this.PIName);
            expParam.put(prefix + "P.Account Number", this.accountNumber);

            // This is a linking output sample -> link this exp to sequencing prep (setup) exp
            // Its name is to be changed upon plate completion
            expParam.put(prefix + "S.DNA," + AbstractSample.PROP_NAME,
                this.tName + "_" + System.currentTimeMillis());

            /* This can only be done once! not for each sample in the order as the plate name cannot be preserved

            final HolderType holderType =
                rw.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");
            assert null != holderType : "No '96 deep well' holder type! ";
            final HolderCategory holderCategory =
                rw.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, "96 deep well plate");
            assert null != holderCategory : "No '96 deep well plate' holder category found! ";
            Holder holder = Creator.recordHolder(rw, holderCategory, "Sequencing order plate", holderType);
            expParam.put(prefix + "S.DNA" + Sample.PROP_HOLDER, holder.get_Hook());
            */

            expParam.put(prefix + LabBookEntry.PROP_DETAILS, this.description);

            // If admin
            /*
             if (user != null) {
                 expParam.put(prefix + LabBookEntry.PROP_CREATOR, user.get_Hook());
             }
             */
            expParam.put(prefix + "P.Order ID", orderId);
            expParam.put(
                prefix + Experiment.PROP_NAME,
                SequencingOrderWriter.makeExperimentName(this.pName, this.tName, this.userName,
                    rw.getUsername()));
            expParam.put(prefix + Experiment.PROP_STATUS, "To_be_run");

            final ExperimentWizardFieldsDecoder ed = new ExperimentWizardFieldsDecoder(expParam);
            final ExperimentWizardCreator eWriter = new ExperimentWizardCreator(rw, protocol, ed);

            return eWriter.getExperiment();
        }

        // To be called from servlet if all parameters are in the map already
        public static Experiment record(final ExperimentWizardFieldsDecoder dec, final WritableVersion rw)
            throws FileNotFoundException, AccessException, ConstraintException {
            final Protocol protocol = Utils.getSOProtocol(rw);

            return new ExperimentWizardCreator(rw, protocol, dec).getExperiment();
        }

        protected SequencingOrderBean getPartCopy() {
            final SequencingOrderBean sb = new SequencingOrderBean();
            sb.accountNumber = this.accountNumber;
            sb.PIName = this.PIName;
            sb.department = this.department;
            sb.userEmail = this.userEmail;
            sb.userName = this.userName;
            sb.cdProvided = this.cdProvided;
            sb.returnSample = this.returnSample;
            sb.userPhone = this.userPhone;
            return sb;
        }

    }

    public String record(final WritableVersion rw) throws FileNotFoundException, AccessException,
        ConstraintException, InvalidAttributeValueException {

        final String orderId = Utils.getOrderId(rw);
        for (final SequencingOrderBean sob : this.orders) {
            sob.record(orderId, rw);
        }
        return orderId;
    }

    public static String makeExperimentName(final String primerName, final String templateName,
        final String userSurname, final String userLogin) {
        return primerName + "_" + templateName + "_" + userSurname + "_" + userLogin;
    }
}
