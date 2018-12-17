package org.pimslims.diamond.plateimporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.crystallization.model.PlateExperimentInfo;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleVolumeMap;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.VolumeMap;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.implementation.TrialServiceImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiamondPlateImporter {

    private static Logger log = LoggerFactory.getLogger(DiamondPlateImporter.class);
    

    /**
     * <p>Create a TrialPlate with the specified volume of the specified Screen in
     * the reservoir of each well and specified mix of protein and reservoir in each drop.</p>
     * 
     * @param dataStorage - The connection through the Business API
     * @param barcode - The barcode of the trial plate
     * @param plateTypeName - The name of the plate type
     * @param proteinName - The name of the protein
     * @param screenName - The name of the screen
     * @param reservoirVolume - The volume of mother liquor in the reservoir of each well in L
     * @param reservoirVolumeUsed - The volume of mother liquor added to each drop in L
     * @param sampleVolumeUsed - The volume of sample added to each drop in L
     * @param setupDate - The date the trial was set up
     * @param owner - The person who owns the plate
     * @param group - The name of the LabNotebook everything should be in
     */
    public TrialPlate createdFilledTrialPlate (
            DataStorageImpl dataStorage,   // The connection through the Business API
            String barcode,                // The barcode of the trial plate
            String plateTypeName,          // The name of the plate type
            String proteinName,            // The name of the protein
            String screenName,             // The name of the screen
            Double reservoirVolume,        // The volume of mother liquor in the reservoir of each well in L
            Double reservoirVolumeUsed,    // The volume of mother liquor added to each drop in L
            Double sampleVolumeUsed,       // The volume of sample added to each drop in L
            Calendar setupDate,            // The date the trial was set up
            String owner,                  // The person who owns the plate
            String group                   // The name of the LabNotebook everything should be in
        ) throws BusinessException {
        
        log.info("creating filled trial plate \"" + barcode + "\" from screen \"" + screenName + "\"");

        // All volumes are in litres
        String volumeUnit = "L";
    
        // Get a WritableVersion
        WritableVersion wv = dataStorage.getWritableVersion();
        wv.setDefaultOwner(group);
        
        // Get a TrialService instance
        TrialService trialService = dataStorage.getTrialService();
    
        // Check the holder doesn't already exist
        Holder h = wv.findFirst(Holder.class, Holder.PROP_NAME, barcode);
        if (null != h) {
            log.error("Holder exists: " + barcode);
            throw new BusinessException("Holder exists: " + barcode);
        }
        
        // Get the appropriate PlateType
        PlateType plateType = trialService.findPlateType(plateTypeName);
        if (null == plateType) {
            log.error("No PlateType for name \"" + plateTypeName + "\"");
            throw new BusinessException("No PlateType for name \"" + plateTypeName + "\"");
        }

        // Default screenName to 'Blank'
        String _screenName = screenName;
        if ( (null == screenName) || ("".equals(screenName)) ) {
            _screenName="Blank";
        }
    
        // Get Screen
        log.debug("Getting Screen");
        Screen screen = dataStorage.getScreenService().findByName(_screenName);
        if (null == screen) {
            log.error("No Screen named \"" + screenName + "\"");
            throw new BusinessException("No Screen named \"" + screenName + "\"");
        }
        log.debug("Got Screen");
    
        // Build volume map - remap reservoir subposition now that we know it
        // TODO As the importer gets more complicated, this should be built externally and passed in
        VolumeMap vm = new VolumeMap(volumeUnit);
        for (int row = 1; row <= plateType.getRows(); row++) {
            for (int col = 1; col <= plateType.getColumns(); col++) {
                vm.setVolume(new WellPosition(row, col, plateType.getReservoir()), reservoirVolume);
            }
        }
        
        // Find operator
        log.debug("Getting Person: " + owner);
        Person op = dataStorage.getPersonService().findByUsername(owner);
        if (null == op) {
            log.error("No person for owner \"" + owner + "\"");
            throw new BusinessException("No person for owner \"" + owner + "\"");
        }
        log.debug("Got Person");

        // Create a PlateExperimentInfo
        PlateExperimentInfo ei = new PlateExperimentInfo();
        ei.setRunAt(setupDate); // TODO Should really be the date the reservoir was put in the plate
        ei.setOperator(op); // Who put the reservoir in the plate
        ei.setProtocolName("Fill Reservoir");
        //ei.setInstrument(instrument); // the instrument that put the reservoir in the plate

        // Create PiMS TrialPlate
        final TrialPlate plate = new TrialPlate(plateType);
        plate.setBarcode(barcode);
        plate.setOwner(op); // Who owns the plate
        plate.setOperator(op); // Who set the plate up
        plate.setScreen(screen);
        plate.setAdditiveScreen(false); // TODO Again, set appropriately when complexity increases
    
        // Fill TrialPlate from DeepWellPlate
        log.debug("Calling fillTrialPlate");
        try {
            ((TrialServiceImpl) trialService).fillTrialPlate(plate, screen, vm, ei, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fillTrialPlate failed: " + e.getMessage());
            throw new BusinessException("fillTrialPlate failed: " + e.getMessage(), e);
        }
        log.debug("Returned from fillTrialPlate");
    
        log.debug("flushing");
        try {
            wv.flush();
        }
        catch (ConstraintException e) {
            //e.printStackTrace();
            log.error("flush failed: " + e.getMessage());
            throw new BusinessException("flush failed: " + e.getMessage(), e);
        }
        log.debug("flushed");

        log.info("done creating filled trial plate \"" + barcode + "\" from screen \"" + screenName + "\"");
        
        log.info("creating setup trial plate \"" + barcode + "\" using protein \"" + proteinName + "\"");
        
        // It may be necessary to retrieve the TrialPlate from the DB here as doing so may populate necessary fields
        //plate = trialService.findTrialPlate(cartesianBean.getTrialPlateBarcode());

        // Find sample
        log.info("Looking for sample " + proteinName);
        Sample sample = dataStorage.getSampleService().findByName(proteinName);
        if (null == sample) {
            throw new BusinessException("No sample found for TrialPlate \"" + barcode + "\"");
        }
        log.debug("Found sample " + proteinName);
        
        // Build volume maps for setting up the TrialDrops
        // TODO As the importer gets more complicated, these should be built externally and passed in
        SampleVolumeMap svm = new SampleVolumeMap("Purified protein", sample, volumeUnit);
        VolumeMap rvm = new VolumeMap(volumeUnit);
        for (int row = 1; row <= plateType.getRows(); row++) {
            for (int col = 1; col <= plateType.getColumns(); col++) {
                for (int sub = 1; sub <= plateType.getSubPositions(); sub++) {
                    if (sub != plateType.getReservoir()) {
                        svm.setVolume(new WellPosition(row, col, sub), sampleVolumeUsed);
                        rvm.setVolume(new WellPosition(row, col, sub), reservoirVolumeUsed);
                    }
                }
            }
        }
        List<SampleVolumeMap> svms = new ArrayList<SampleVolumeMap>();
        svms.add(svm);
        
        // TODO Use correct protocol name for sample/additive/seed combination
        // - matters because of the number and names of the InputSamples
        String protocolName = "CrystalTrial P+R";
        
        // Create a PlateExperimentInfo
        ei = new PlateExperimentInfo();
        ei.setRunAt(setupDate);
        ei.setOperator(op); // Who set the plate up
        ei.setProtocolName(protocolName);
        //ei.setParameter("Script", scriptName);
        //ei.setInstrument(instrument);
        if (null != sample.getConstruct()) {
            ei.setExpBlueprintName(sample.getConstruct().getName());
        }

        log.debug("Calling setupTrialPlate");
        try {
            ((TrialServiceImpl) trialService).setupTrialPlate(plate, svms, rvm, ei);
        } catch (BusinessException e) {
            //e.printStackTrace();
            log.error("setupTrialPlate failed: " + e.getMessage());
            throw new BusinessException("setupTrialPlate failed: " + e.getMessage(), e);
        }
        log.debug("Returned from setupTrialPlate");
        
        log.debug("flushing");
        try {
            wv.flush();
        }
        catch (ConstraintException e) {
            //e.printStackTrace();
            log.error("flush failed: " + e.getMessage());
            throw new BusinessException("flush failed: " + e.getMessage(), e);
        }
        log.debug("flushed");

        log.info("done creating setup trial plate \"" + barcode + "\" using protein \"" + proteinName + "\"");
        
        //No, we do that in the class that supplied the DataStorage.
        //dataStorage.commit();
        //dataStorage.closeResources();
        
        return plate;
    
    }

}
