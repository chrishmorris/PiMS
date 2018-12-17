/**
 * IspybRest2 org.pimslims.ispyb IspbyBeanFactory.java
 * 
 * @author cm65
 * @date 28 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ispyb;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.soap.impl.dom.soap11.SOAP11Factory;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.apache.axis2.databinding.utils.writer.MTOMAwareOMBuilder;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

import uk.ac.diamond.ispyb.client.IspybServiceStub.CollectPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CollectPlanSequence_type0;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Container;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetailsSequence_type0;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalIdentifier;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalIdentifierSequence_type0;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DeliveryAgent;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Dewar;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlanSequence_type0;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Holder;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Protein;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ScreenPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Spacegroup_type1;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Spacegroup_type3;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SweepInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SweepInformationChoice_type0;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Type_type5;

/**
 * IspbyBeanFactory
 * 
 * This makes the beans needed by the ISPyB web services. It populates them with the minimum required
 * information. It is also possible to set extra information in them.
 * 
 * @see org.pimslims.ispyb.Client
 */
public class IspybBeanFactory {

    protected Protein makeProtein(String acronym, String uuid) {
        Protein protein = new Protein();
        protein.setName(acronym);
        protein.setProteinUUID(uuid);
        protein.setAcronym(protein.getName());
        return protein;
    }

    protected Crystal makeCrystal(Protein protein, String name, String uuid) {
        Crystal crystal = new Crystal();
        crystal.setName(name);
        crystal.setCrystalUUID(uuid);
        crystal.setProtein(protein);
        crystal.setSpacegroup(Spacegroup_type1.P1);
        return crystal;
    }

    protected DeliveryAgent makeDeliveryAgent(String name, Calendar shippingDate, Calendar deliveryDate) {
        DeliveryAgent deliveryAgent = new DeliveryAgent();
        deliveryAgent.setAgentName(name);
        deliveryAgent.setAgentCode(name);
        if (null!=deliveryDate) {
            deliveryAgent.setDeliveryDate(deliveryDate.getTime());
        }
        deliveryAgent.setShippingDate(shippingDate.getTime());
        return deliveryAgent;
    }

    protected Holder makeLoop(Crystal crystal, String position, String barcode) {
        Holder ret = new Holder();
        ret.setCode(barcode);
        ret.setPosition(position.toString());
        ret.setLoopType("unknown");
        ret.setCrystalUUID(crystal.getCrystalUUID());
        CrystalIdentifier id = new CrystalIdentifier();
        id.setCrystalUUID(crystal.getCrystalUUID());
        CrystalIdentifierSequence_type0 seq = new CrystalIdentifierSequence_type0();
        Protein protein = crystal.getProtein();
        if (null==protein) {
            seq.setProteinAcronym("");
        } else {
            seq.setProteinAcronym(protein.getAcronym());
        }
        String group = crystal.getSpacegroup().getValue();
        seq.setSpacegroup(Spacegroup_type3.Factory.fromValue(group));
        id.setCrystalIdentifierSequence_type0(seq);
        ret.setCrystalIdentifier(id);
        return ret;
    }

    protected Container makePuck(String barcode, Holder[] loops) {
        Container ret = new Container();
        ret.setCode(barcode);
        Type_type5 type = Type_type5.Puck;
        ret.setType(type);
        /* was int position = 0;
        for (Iterator iterator = crystals.iterator(); iterator.hasNext();) {
            Crystal crystal = (Crystal) iterator.next();
            ret.addHolder(makeLoop(crystal, ++position, UNIQUE + "h" + position));
        } */
        ret.setHolder(loops);
        return ret;
    }

    protected Dewar makeDewar(String barcode, Holder[] holders, Container puck) {
        Dewar ret = new Dewar();
        ret.setBarCode(barcode);
        ret.addContainer(puck);
        return ret;
    }

    protected CrystalDetails makeCrystalDetails(Collection<Crystal> crystals, String projectUUID) {
        CrystalDetails details = new CrystalDetails();
        details.setProjectUUID(projectUUID);
        for (Iterator iterator = crystals.iterator(); iterator.hasNext();) {
            Crystal crystal = (Crystal) iterator.next();
            CrystalDetailsSequence_type0 sequence = new CrystalDetailsSequence_type0();
            sequence.setCrystal(crystal);
            details.addCrystalDetailsSequence_type0(sequence);
        }
        return details;
    }

    protected CrystalShipping makeShipping(String name, Dewar[] dewars, String projectUUID,
        DeliveryAgent agent) {
        CrystalShipping shipping = new CrystalShipping();
        shipping.setDeliveryAgent(agent);
        shipping.setDewar(dewars);
        shipping.setName(name);
        shipping.setProjectUUID(projectUUID);
        return shipping;
    }

    /**
     * Used for testing
     * 
     * @param bean
     * @throws AssertionError if the bean does not match the XSDs
     */
    protected void validate(final org.apache.axis2.databinding.ADBBean bean) {
        QName qName = new QName("crystalShipping", "http://www.pims-lims.org/services/ispyb", "test");
        final OMFactory factory = new SOAP11Factory();

        MTOMAwareXMLStreamWriter xmlWriter = new MTOMAwareOMBuilder();
        try {
            bean.serialize(qName, factory, xmlWriter);
        } catch (ADBException e) {
            e.printStackTrace();
            throw new AssertionError(e.getMessage());
        } catch (XMLStreamException e) {
            throw new AssertionError(e.getMessage());
        }
    }

	protected DiffractionPlan makeDiffractionPlan(String projectUuid,
			String planUuid, String crystalUuid, CollectPlan collectionPlan, SweepInformation sweep, ScreenPlan screen) {
				DiffractionPlan ret;
				ret = new DiffractionPlan();
				ret.setProjectUUID(projectUuid);
				ret.setDiffractionPlanUUID(planUuid); 
				
				// It's probably an error that this method takes plural crystalUUIDs.
				// A diffraction plan can apply to several samples, but to only one crystal form.
				ret.setCrystalUUID(new String[] { crystalUuid });
				
			    DiffractionPlanSequence_type0 dps = new DiffractionPlanSequence_type0();
				dps.setCollectPlan(collectionPlan ); 
				CollectPlanSequence_type0 cp = new CollectPlanSequence_type0();
				cp.setSweepInformation(sweep );
				collectionPlan.setCollectPlanSequence_type0(new CollectPlanSequence_type0[] {cp } );
				dps.setScreenPlan(screen);
			    ret.setDiffractionPlanSequence_type0(dps);
				return ret;
			}

	/**
	 * The user often will not want to code sweep information
	 * @param resolution the intended resolution, in Angstrom
	 * @return a dummy sweep bean
	 */
	protected SweepInformation getDefaultSweep(double resolution) {
		SweepInformation ret = new SweepInformation();
		SweepInformationChoice_type0 choice = new  SweepInformationChoice_type0();
		choice.setResolution(resolution);
		ret.setSweepInformationChoice_type0(choice );
		return ret ;
	}

	protected static ShipmentInfo makeShipmentInfo(String projectUUID, String shipmentName) {
		ShipmentInfo info;
	
	    info = new ShipmentInfo();
	    info.setProjectUUID(projectUUID);
	    info.setShipmentName(shipmentName);
	    return info;
	}

}
