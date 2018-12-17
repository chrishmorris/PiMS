/**
 * ispybToPims org.pimslims.ispyb DiffractionExperimentFactory.java
 * 
 * @author cm65
 * @date 1 Aug 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ispyb;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.FlushMode;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.util.FileImpl;

import uk.ac.diamond.ispyb.client.IspybServiceStub.BLSample;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollection;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollectionInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Image;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Session;

/**
 * DiffractionExperimentFactory
 * 
 */
public class DiffractionExperimentFactory {

    private final WritableVersion version;

    private final ExperimentType experimentType;

    //TODO may need several for MAD etc
    private final Protocol protocol;

    private final SampleCategory category;

	private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

	private final  Map<String, Integer> runs = new HashMap<String, Integer>();

    /**
     * Constructor for DiffractionExperimentFactory
     * 
     * @param version
     */
    public DiffractionExperimentFactory(WritableVersion version) {
        super();
        this.version = version;
        experimentType = version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Diffraction");
        protocol = version.findFirst(Protocol.class, Protocol.PROP_NAME, "ISPyB Diffraction");
        this.category = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Mounted Crystal");
        assert null!=this.experimentType && null!=this.protocol && null!=this.category 
        		: "Must load reference data for diffraction" ;
    }

    /**
     * DiffractionExperimentFactory.getExperiment
     * 
     * @param dci
     * @param session TODO
     * @throws ConstraintException
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     */
    public Experiment getExperiment(DataCollectionInformation dci, Session session) throws IOException, ConstraintException {
        DataCollection dc = dci.getDataCollection();
        assert null != dc;
        BLSample blSample = dci.getBlSample();
        if (!this.runs.containsKey(blSample.getName())) {
        	this.runs.put(blSample.getName(), new Integer(1));
        }
        Integer run = this.runs.get(blSample.getName());
		String name = "ISPyB Diffraction: " + blSample.getName() +" "+run;
        this.runs.put(blSample.getName(), 1+run);
        Experiment experiment =
            new Experiment(version, name, dc.getStartTime(), dc.getEndTime(), experimentType);
        experiment.setProtocol(this.protocol);
        processBean(dc, experiment, "Data Collection: ");
        

        // process the session
        if (null != session) {
            processBean(session, experiment, "Session: ");
        }

        // process the crystal form
        if (null != dci.getCrystal()) {
            processBean(dci.getCrystal(), experiment, "Crystal Form: ");
        }
        
        // process the energy scan
        if (null != dci.getEnergyScan()) {
            processBean(dci.getEnergyScan(), experiment, "Energy Scan: ");
        }

        // process the details of the images
        Image[] images = dci.getImage();
        if (null != images) {
            processImages(experiment, images);
        }

        // process the details of the sample
        if (null != blSample) {
            InputSample is = new InputSample(version, experiment);
            is.setName("Mounted Crystal");
			// TODO Speed this up
            RefInputSample ris = this.protocol.findFirst(Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME, is.getName());
			is.setRefInputSample(ris );
            Sample sample = findOrCreateSample(blSample);
            is.setSample(sample);
        }
        return experiment;
    }

	private final Map<String, Sample> samples = new HashMap();

	private Sample findOrCreateSample(BLSample blSample)
			throws ConstraintException {
		if (samples.containsKey(blSample.getName())) {
			return samples.get(blSample.getName());
		}
		Sample sample = this.version.findFirst(Sample.class, Sample.PROP_NAME, blSample.getName());
		if (null==sample) {
		    sample = new Sample(version, blSample.getName());
		    sample.setSampleCategories(Collections.singleton(this.category));
		}
        Holder loop = this.version.findFirst(Holder.class, Holder.PROP_NAME, blSample.getCode());
        if (null==loop) {
        	loop = new Holder(version, blSample.getCode());
            //TODO holdertype
        }
        sample.setHolder(loop);
		samples.put(blSample.getName(), sample);
		return sample;
	}

    private void processBean(Object dc, Experiment experiment, String prefix) throws ConstraintException {
        Method[] methods = dc.getClass().getMethods();
        Method method = null;
        try {
            for (int i = 0; i < methods.length; i++) {
                method = methods[i];
                if (0 != method.getParameterTypes().length) {
                    continue;
                }
                if (!method.getReturnType().isPrimitive() && !String.class.equals(method.getReturnType())) {
                    continue;
                }
                if (!method.getName().startsWith("get")) {
                    continue;
                }
                String name = prefix+getParameterName(method);
                Object value = method.invoke(dc);
                if (value instanceof Float && Float.isNaN((Float) value)) {
                	// web services beans return NaN instead of null
                	value = null;
                }
                setParameter(experiment, name, value);
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Exception invoking: " + method.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void processImages(Experiment experiment, Image[] images) throws 
        IOException, ConstraintException {
		/*
		 * TODO when DLS offers images try { for (int i = 0; i < images.length;
		 * i++) { Image image = images[i]; try { URL url = new
		 * URL(image.getFileLocation()); InputStream in = url.openStream();
		 * FileImpl.createFile(version, in, image.getFileName(), experiment); }
		 * catch (MalformedURLException e) {
		 * System.out.println("Cannot open image at: "+image.getFileLocation());
		 * } } } catch (AccessException e) { throw new RuntimeException(e); //
		 * should not happen }
		 */
    }

    private String getParameterName(Method method) {
        String name = method.getName().substring(3); // remove "get"
        String ret = name.replaceAll("(?=\\p{Upper})", " ");
        return ret.substring(1);
    }

    private Parameter setParameter(Experiment experiment, String name, Object value)
        throws ConstraintException {
        Protocol protocol = experiment.getProtocol();
		// TODO Speed this up
        ParameterDefinition pd =
            protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME, name);
        if (null == pd) {
        	this.logger.fine("No such parameter: "+name+"="+value);
            return null;
        }
        Parameter parm = new Parameter(version, experiment);
        parm.setName(name);
        if (null != value) {
            parm.setValue(value.toString());
        }
        parm.setParameterDefinition(pd);
        return parm;
    }

    /**
     * DiffractionExperimentFactory.getExperimentGroups
     * 
     * @param result
     * @throws IOException
     * @throws ConstraintException
     */
    public Collection<ExperimentGroup> getExperimentGroups(BeamlineExportedInformation result)
        throws IOException {
        try {

			FlushMode flushMode = ((ReadableVersionImpl) version)
					.getFlushMode();
			((ReadableVersionImpl) version).setFlushMode(FlushMode.fastMode());
            Map<String, ExperimentGroup> groups = new HashMap<String, ExperimentGroup>(); // crystalUUID => ExperimentGroup
            DataCollectionInformation[] dcis = result.getDataCollectionInformation();
            if (null == dcis) {
                return Collections.EMPTY_LIST;
            }
            for (int i = 0; i < dcis.length; i++) {
				// long start = System.currentTimeMillis();
                DataCollectionInformation dci = dcis[i];
                String crystalUUID = dci.getCrystal().getCrystalUUID();
                if (!groups.containsKey(crystalUUID)) {
                    ExperimentGroup group =
                        new ExperimentGroup(version, crystalUUID, "Diffraction results from ISPyB");
                    groups.put(crystalUUID, group);
                }
                ExperimentGroup group = groups.get(crystalUUID);
                Experiment experiment = this.getExperiment(dci, null);
                group.addExperiment(experiment);
				/*
				 * System.out.println("Time to add an experiment: " +
				 * ((System.currentTimeMillis() - start) / 1000) + "s");
				 */
            }
			version.flush();
			((ReadableVersionImpl) version).setFlushMode(flushMode);
            return groups.values();
        } catch (ConstraintException e) {
            throw new RuntimeException(e); // should not happen
        }
    }
}
