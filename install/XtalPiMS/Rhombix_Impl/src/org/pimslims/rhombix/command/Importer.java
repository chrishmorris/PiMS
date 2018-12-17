/**
 * Rhombix_Impl org.pimslims.rhombix.command Importer.java
 * 
 * @author cm65
 * @date 12 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.command;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;
import org.pimslims.rhombix.implementation.PlateInspectionServiceImpl;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * Importer
 * 
 * This imports data from a Rhombix database to a PiMS database. It uses the following concepts;
 * 
 * Rhombix "Macromolecule", and equivalently the PiMS "Construct"
 * 
 * "Most recent update" - the most recent import of images to PiMS
 * 
 * "Start time" - the date and time of the earliest imported data
 * 
 * The different methods available preserve the following invariant:
 * 
 * EITHER
 * 
 * There are not yet any images in PiMS
 * 
 * OR BOTH
 * 
 * Every image that is not linked to a specific construct (macromolecule) is in PiMS, if it was made after the
 * "start time" and before the latest update time.
 * 
 * AND
 * 
 * Every image that is linked to a construct (macromolecule) known to PiMS is in PiMS, if it was made before
 * the latest update time.
 * 
 */
public class Importer {

    /**
     * MAX_TRANSACTION_SIZE int
     */
    private static final int MAX_TRANSACTION_SIZE = 5;

    static final String INSTRUMENT_NAME = "Rhombix";

    private final DataStorage pims;

    private final DataStorage rhombix;

    private Location pimsImager;

    /**
     * Constructor for Importer
     * 
     * @param pims
     * @param rhombix
     * @throws BusinessException
     */
    Importer(RhombixDataStorageImpl rhombix, DataStorageImpl pims) throws BusinessException {
        super();
        this.pims = pims;
        this.rhombix = rhombix;
        this.pimsImager = this.pims.getLocationService().findByName(INSTRUMENT_NAME);
        if (null == this.pimsImager) {
            this.pimsImager = Importer.createInstrument((DataStorageImpl) this.pims);
        }
    }

    /**
     * Importer.getLatestUpdateTime
     * 
     * @return the datetime of the most recent plate inspection in PiMS Returns null if neither importAfter
     *         nor recordConstruct has been run
     * 
     * @throws BusinessException
     */
    Calendar getLatestUpdateTime() throws BusinessException {
        PlateInspectionService pis = this.pims.getPlateInspectionService();
        BusinessCriteria criteria = new BusinessCriteria(pis);
        criteria.setMaxResults(1);
        Collection<InspectionView> found = pis.findLatest(criteria);
        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next().getDate();
    }

    /**
     * Importer.update
     * 
     * imports all images taken after the latest update time, if their construct is known to PiMS, or
     * undefined.
     * 
     * @throws BusinessException
     */
    public void update() throws BusinessException {
        importAfter(getLatestUpdateTime());
    }

    /**
     * Importer.recordConstruct
     * 
     * @param name the name of the Macromolecule to import
     * 
     *            Record a macromolecule in PiMS. If there are already images in PiMS, this also imports all
     *            images for the construct that are before the most recent update. In that way, the invariant
     *            is preserved.
     * 
     * @throws BusinessException
     */
    public void recordConstruct(String name) throws BusinessException {
        Construct construct = null;
        //TODO record the construct, user, lab notebook
        if (null != this.getLatestUpdateTime()) {
            importBefore(construct, this.getLatestUpdateTime());
        }

    }

    /**
     * Importer.importBefore
     * 
     * @param name
     * @param latestUpdateTime
     * @throws BusinessException
     */
    private void importBefore(Construct construct, Calendar latestUpdateTime) throws BusinessException {
        Collection<Long> inspections = inspectionsBefore(construct, latestUpdateTime);
        for (Iterator<Long> iterator = inspections.iterator(); iterator.hasNext();) {
            importInspection(iterator.next());
        }
    }

    /**
     * Importer.importImage
     * 
     * @param inspectionId Import the inspection, if its construct is undefined or known to PiMS
     * @throws BusinessException
     */
    void importInspection(long inspectionId) throws BusinessException {

        PlateInspection inspection = this.rhombix.getPlateInspectionService().find(inspectionId);
        Long pimsPlateId = null;
        TrialPlate pimsPlate = this.pims.getTrialService().findTrialPlate(inspection.getPlate().getBarcode());
        if (null == pimsPlate) {
            pimsPlateId = this.importPlate(inspection.getPlate().getBarcode());
        } else {
            pimsPlateId = pimsPlate.getId();
        }
        inspection.getPlate().setId(pimsPlateId); // overwrite value from exporting DB
        inspection.setLocation(this.pimsImager);
        PlateInspectionService pis = this.pims.getPlateInspectionService();
        pis.create(inspection);

    }

    /**
     * Importer.importPlate
     * 
     * @param barcode
     * @return
     * @throws BusinessException
     */
    Long importPlate(String barcode) throws BusinessException {

        TrialPlate plate = this.rhombix.getTrialService().findTrialPlate(barcode);
        plate.setId(null); // overwrite value from exporting DB

        // import screen
        Screen screen = plate.getScreen();
        ScreenService pss = this.pims.getScreenService();
        if (null != screen && null == pss.findByName(screen.getName())) {
            pss.create(screen);
        }

        // import construct
        Construct construct = plate.getConstruct();
        ConstructService pcs = this.pims.getConstructService();
        if (null != construct && null == pcs.findByName(construct.getName())) {
            pcs.create(construct);
        }

        this.pims.getTrialService().saveTrialPlate(plate);
        return plate.getId();
    }

    /**
     * Importer.importAfter
     * 
     * @param start the first ten after this date/time will be processed
     * 
     * 
     * @throws BusinessException
     */
    void importAfter(Calendar start) throws BusinessException {
        Collection<Long> inspections = inspectionsAfter(start);
        int count = 0;
        for (Iterator<Long> iterator = inspections.iterator(); iterator.hasNext();) {
            if (count++ > MAX_TRANSACTION_SIZE) {
                break;
            }
            Long view = iterator.next();
            importInspection(view);
        }
    }

    /**
     * Importer.imagesBefore
     * 
     * @param name
     * @param latestUpdateTime
     * @return
     * @throws BusinessException
     */
    private Collection<Long> inspectionsBefore(Construct construct, Calendar end) throws BusinessException {
        return findInspections(end, false);
    }

    /**
     * Importer.imagesAfter
     * 
     * @param start
     * @return Rhombix inspections recorded after the start date
     * @throws BusinessException
     */
    Collection<Long> inspectionsAfter(Calendar start) throws BusinessException {
        return findInspections(start, true);
    }

    private Collection<Long> findInspections(Calendar date, boolean after) throws BusinessException {

        PlateInspectionServiceImpl pis =
            (PlateInspectionServiceImpl) this.rhombix.getPlateInspectionService();

        return pis.findInspectionIds(date, after);
    }

    /**
     * Importer.setup
     * 
     * @param start Import all images of unspecified construct, after the start date. Also import all images
     *            of known constructs.
     * @throws BusinessException public void setup(Calendar start) throws BusinessException { Calendar now =
     *             Calendar.getInstance(); Collection<Construct> constructs = getAllConstructs(); for
     *             (Iterator<Construct> iterator = constructs.iterator(); iterator.hasNext();) { Construct
     *             construct = iterator.next(); importBefore(construct, now); }
     * 
     *             TrialService rts = this.rhombix.getTrialService(); Collection<Long> inspections =
     *             inspectionsAfter(start); for (Iterator<Long> iterator = inspections.iterator();
     *             iterator.hasNext();) { Long view = iterator.next(); TrialPlate plate =
     *             rts.findTrialPlate(view.getBarcode()); if (null == plate.getConstruct()) {
     *             importInspection(((InspectionViewImpl) view).getId()); } }
     * 
     *             }
     */

    /**
     * Importer.getAllConstructs
     * 
     * @return
     * @throws BusinessException
     */
    private Collection<Construct> getAllConstructs() throws BusinessException {
        ConstructService cs = this.pims.getConstructService();
        return cs.findAll(new BusinessCriteria(cs));
    }

    public static Location createInstrument(DataStorageImpl pims) throws BusinessException {
        WritableVersion version = pims.getWritableVersion();

        try {
            ImageType type = new ImageType(version, INSTRUMENT_NAME);
            type.setDetails("Rhombix images");
            type.setUrl("file:///RhombixImages/");
            Instrument instrument = new Instrument(version, INSTRUMENT_NAME);
            instrument.setDefaultImageType(type);
            instrument.setDetails("The Rhombix imager");
            version.flush();
            return pims.getLocationService().findByName(INSTRUMENT_NAME);
        } catch (ConstraintException e) {
            throw new BusinessException(e);
        }
    }

}
