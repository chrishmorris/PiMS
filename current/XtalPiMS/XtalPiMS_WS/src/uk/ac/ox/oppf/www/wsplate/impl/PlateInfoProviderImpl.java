/**
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl PlateInfoProviderImpl.java
 * 
 * @author jon
 * @date 23 Aug 2010
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2010 jon The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
// FIXME VMXi Bodge - no UserDB
//import org.pimslims.user.userdb.dto.User;
//import org.pimslims.user.userdb.util.UserInfoProvider;

import uk.ac.ox.oppf.www.wsplate.ArrayPlateType;
import uk.ac.ox.oppf.www.wsplate.GetPlateID;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDError;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfo;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoError;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateType;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypes;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponse;
import uk.ac.ox.oppf.www.wsplate.PlateInfo;
import uk.ac.ox.oppf.www.wsplate.PlateType;

/**
 * PlateInfoProviderImpl
 * 
 */
public class PlateInfoProviderImpl implements PlateInfoProvider {

    private static Logger log = LogManager.getLogger(PlateInfoProvider.class);

    /**
     * Abstraction of the provision of a DataStorageFactoy and username to assist in testing.
     */
    final DataStorageFactoryProvider dsfp;

    static Map<String, String> plateTypeNameToIdMap = null;

    /**
     * 
     * Constructor for PlateInfoProviderImpl
     */
    public PlateInfoProviderImpl() {
        this(new AxisDataStorageFactoryProvider());
    }

    /**
     * 
     * Constructor for PlateInfoProviderImpl
     * 
     * @param dsfp
     */
    public PlateInfoProviderImpl(DataStorageFactoryProvider dsfp) {
        this.dsfp = dsfp;
    }

    /**
     * WSPlateImpl.getPlateID
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateIDElement)
     */
    @Override
    public GetPlateIDResponse getPlateID(GetPlateID request) throws GetPlateIDError {

        // Get a GetPlateIDResponse wrapper and populate it
        GetPlateIDResponse response = new GetPlateIDResponse();
        response.setGetPlateIDReturn(request.getBarcode());

        // Return the response
        return response;

    }

    /**
     * WSPlateImpl.getPlateInfo
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateInfo(uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement)
     */
    @Override
    public GetPlateInfoResponse getPlateInfo(GetPlateInfo request) throws GetPlateInfoError {
        log.debug("getting data storage");
        DataStorage dataStorage = null;
        try {
            log.debug("opening resources");
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());

            log.debug("getting plateexperimentservice");
            PlateExperimentService plateExperimentService = dataStorage.getPlateExperimentService();
            log.debug("calling findByPlatebarcode()");
            Collection<PlateExperimentView> plateExperiments =
                plateExperimentService.findByPlateBarcode(request.getPlateID());
            log.debug("back from findByPlatebarcode()");

            // Plate not in db - throw exception
            if (plateExperiments.isEmpty()) {
                throw new BusinessException(request.getPlateID() + " not in database");
            }
            PlateExperimentView plateExperiment = plateExperiments.iterator().next();

            PlateInfo plateInfo = new PlateInfo();
            plateInfo.setDateDispensed(plateExperiment.getCreateDate());
            plateInfo.setExperimentName(generateExperimentName(request, plateExperiment));
            plateInfo.setPlateNumber(extractNumber(request.getPlateID()));
            //plateInfo.setPlateTypeID(plateExperiment.getPlateType());
            // TODO plateTypeID requires mapping?
            // plateInfo.setPlateTypeID(mapPlateTypeNameToID(plateExperiment.getPlateType()));
            // Try mapping it at the other end
            plateInfo.setPlateTypeID(plateExperiment.getPlateType());
            plateInfo.setProjectName(plateExperiment.getGroup());
            plateInfo.setUserEmail(getEmailForOwner(plateExperiment.getOwner()));
            plateInfo.setUserName(plateExperiment.getOwner());

            // Get a GetPlateInfoResponse wrapper and populate it
            GetPlateInfoResponse response = new GetPlateInfoResponse();
            response.setGetPlateInfoReturn(plateInfo);

            dataStorage.commit();

            // Return the response
            return response;
        } catch (Throwable t) {
            log.error("Error in getPlateInfo", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new GetPlateInfoError(t.getMessage(), t);
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                } catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * WSPlateImpl.getPlateType
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateType(uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement)
     */
    @Override
    public GetPlateTypeResponse getPlateType(GetPlateType request) throws GetPlateTypeError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());

            TrialService trialService = dataStorage.getTrialService();

            org.pimslims.business.crystallization.model.PlateType pimsPlateType =
                trialService.findPlateType(Long.parseLong(request.getPlateTypeID()));

            // PlateType not in db - throw exception
            if (null != pimsPlateType) {
                throw new BusinessException("Plate type not found: " + request.getPlateTypeID());
            }

            // TODO Id mapping required?
            //String plateTypeName = mapPlateTypeIDToName(request.getPlateTypeID());
            //org.pimslims.business.crystallization.model.PlateType pimsPlateType = trialService.findPlateType(plateTypeName);

            PlateType plateType = pimsPlateTypeToWs(pimsPlateType);
            plateType.setID(request.getPlateTypeID());

            // Get a GetPlateIDResponse wrapper and populate it
            GetPlateTypeResponse response = new GetPlateTypeResponse();
            response.setGetPlateTypeReturn(plateType);

            dataStorage.commit();

            // Return the response
            return response;

        } catch (Throwable t) {
            log.error("Error in getPlateType", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new GetPlateTypeError(t.getMessage(), t);
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                } catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * WSPlateImpl.getPlateTypes
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateTypes(uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement)
     */
    @Override
    public GetPlateTypesResponse getPlateTypes(GetPlateTypes request) throws GetPlateTypesError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());

            TrialService trialService = dataStorage.getTrialService();
            Collection<org.pimslims.business.crystallization.model.PlateType> pimsPlateTypes =
                trialService.findAllPlateTypes();

            PlateType[] plateTypes = new PlateType[pimsPlateTypes.size()];
            int i = 0;
            Map<String, String> nameToIdMap = new HashMap<String, String>();
            for (org.pimslims.business.crystallization.model.PlateType pimsPlateType : pimsPlateTypes) {
                plateTypes[i] = pimsPlateTypeToWs(pimsPlateType);
                nameToIdMap.put(pimsPlateType.getName(), pimsPlateType.getId().toString());
                // TODO Id mapping required?
                // plateTypes[i].setID(mapPlateTypeNameToID(pimsPlateType.getName()));
                i++;
            }
            plateTypeNameToIdMap = nameToIdMap;

            // Get an ArrayPlateType and populate it
            ArrayPlateType arrayPlateType = new ArrayPlateType();
            arrayPlateType.setItem(plateTypes);

            // Get a GetPlateIDResponse wrapper and populate it
            GetPlateTypesResponse response = new GetPlateTypesResponse();
            response.setWrapper(arrayPlateType);

            dataStorage.commit();

            // Return the response
            return response;
        } catch (Throwable t) {
            log.error("Error in getPlateTypes", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new GetPlateTypesError(t.getMessage(), t);
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                } catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * PlateInfoProviderImpl.getEmailForOwner
     * 
     * @param owner
     * @return
     */
    private String getEmailForOwner(String owner) {
        return owner + "@vmxi.diamond.ac.uk";
        // FIXME VMXi Bodge - no UserDB
        //UserInfoProvider uip = new UserInfoProvider();
        //User u = uip.getUserByUsername(owner);
        //return u.getEmail();
    }

    /**
     * PlateInfoProviderImpl.pimsPlateTypeToWs -Convert an xtalPiMS business model PlateType to a webservice
     * PlateType. NB This method does not set the ID!
     * 
     * @param pimsPlateType - the xtalPiMS business model PlateType
     * @return The equivalent webservice PlateType
     */
    private PlateType pimsPlateTypeToWs(org.pimslims.business.crystallization.model.PlateType pimsPlateType) {
        PlateType plateType = new PlateType();
        //plateType.setID(mapPlateTypeNameToID(pimsPlateType.getName())); // TODO Mapping?
        plateType.setID(pimsPlateType.getId().toString());
        plateType.setName(pimsPlateType.getName());
        plateType.setNumRows(pimsPlateType.getRows());
        plateType.setNumColumns(pimsPlateType.getColumns());
        plateType.setNumDrops(pimsPlateType.getSubPositions() - 1); // -1 as count includes reservoir
        // TODO Check that getSubPositions() returns the number of (used) trial drop positions
        // Eg must have separate record for three-shelf greiner plate for central shelf and all shelves
        // NB can work round this with correct use of capture profiles
        return plateType;
    }

    /**
     * PlateInfoProviderImpl.mapPlateTypeNameToID
     * 
     * @param plateTypeName
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unused")
    private String mapPlateTypeNameToID(String plateTypeName) throws BusinessException {
        try {
            if ((null != plateTypeNameToIdMap) && (plateTypeNameToIdMap.containsKey(plateTypeName))) {
                return plateTypeNameToIdMap.get(plateTypeName);
            }
            this.getPlateTypes(new GetPlateTypes());
            if (plateTypeNameToIdMap.containsKey(plateTypeName)) {
                return plateTypeNameToIdMap.get(plateTypeName);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        throw new BusinessException("Unable to map ID for plateType " + plateTypeName);
    }

    /**
     * PlateInfoProviderImpl.mapPlateTypeIDToName
     * 
     * @param plateTypeID
     * @return
     */
    @SuppressWarnings("unused")
    private String mapPlateTypeIDToName(String plateTypeID) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * PlateInfoProviderImpl.extractNumber
     * 
     * @param plateID
     * @return
     */
    private int extractNumber(String plateID) {
        if (plateID.matches("\\d+")) {
            if (plateID.length() <= 9) {
                return Integer.parseInt(plateID);
            } else {
                return Integer.parseInt(plateID.substring(plateID.length() - 8));
            }
        }
        return 0;
    }

    /**
     * PlateInfoProviderImpl.generateExperimentName
     * 
     * @param request
     * @param plateExperiment
     * @return
     */
    private String generateExperimentName(GetPlateInfo request, PlateExperimentView plateExperiment) {
        return ((null == plateExperiment.getSampleName()) ? request.getPlateID() : plateExperiment
            .getSampleName())
            + " in " + plateExperiment.getScreen();
    }

}
