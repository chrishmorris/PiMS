/**
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl WSPlateImpl.java
 * 
 * @author jon
 * @date 12 Jul 2010
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2010 jon The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.context.OperationContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.SimpleSampleView;

import uk.ac.ox.oppf.www.wsplate.*;

/**
 * WSPlateImpl
 * 
 */
public class WSPlateImpl extends WSPlateSkeleton {

    /**
     * Logger for this class
     */
    private static Logger log = LogManager.getLogger(WSPlateImpl.class);
    
    /**
     * How to get this opened and closed?
     */
    private final DataStorageFactoryProvider dsfp;

    /**
     * The PlateInfoProvider
     */
    private final PlateInfoProvider pip;

    /**
     * The ImagingTaskProvider
     */
    private final ImagingTaskProvider itp;

    /**
     * Constructor for WSPlateImpl
     */
    public WSPlateImpl() {
        dsfp = new AxisDataStorageFactoryProvider();
        pip = new PlateInfoProviderImpl(dsfp);
        itp = new ImagingTaskProviderImpl(dsfp);
    }

    /**
     * Constructor for testing WSPlateImpl
     */
    protected WSPlateImpl(DataStorageFactoryProvider dsfp) {
        this.dsfp = dsfp;
        pip = new PlateInfoProviderImpl(dsfp);
        itp = new ImagingTaskProviderImpl(dsfp);
    }

    public void setOperationContext(OperationContext opContext) {

    }

    /* ********************************************************************************************************* */
    /* *****************************           PlateInfoProvider Methods           ***************************** */
    /* ********************************************************************************************************* */

    /**
     * WSPlateImpl.getPlateID
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateIDElement)
     */
    @Override
    public GetPlateIDResponseElement getPlateID(GetPlateIDElement requestElement) throws GetPlateIDError {

        // Extract type from element
        GetPlateID request = requestElement.getGetPlateIDElement();

        // Get a GetPlateIDResponse wrapper and populate it
        GetPlateIDResponse response = null;
        
        try {
            
            // Populate the GetPlateIDResponse wrapper
            response = pip.getPlateID(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            GetPlateIDFault fault = new GetPlateIDFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            GetPlateIDError ex = new GetPlateIDError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }

        // Return the response wrapper
        GetPlateIDResponseElement responseElement = new GetPlateIDResponseElement();
        responseElement.setGetPlateIDResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.getPlateInfo
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateInfo(uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement)
     */
    @Override
    public GetPlateInfoResponseElement getPlateInfo(GetPlateInfoElement requestElement)
        throws GetPlateInfoError {

        // Extract type from element
        GetPlateInfo request = requestElement.getGetPlateInfoElement();

        // Get a GetPlateInfoResponse wrapper
        GetPlateInfoResponse response = null;
        
        try {
            
            // Populate the GetPlateInfoResponse wrapper
            response = pip.getPlateInfo(request);
        
        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            GetPlateInfoFault fault = new GetPlateInfoFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            GetPlateInfoError ex = new GetPlateInfoError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }

        // Return the response wrapper
        GetPlateInfoResponseElement responseElement = new GetPlateInfoResponseElement();
        responseElement.setGetPlateInfoResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.getPlateType
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateType(uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement)
     */
    @Override
    public GetPlateTypeResponseElement getPlateType(GetPlateTypeElement requestElement)
        throws GetPlateTypeError {

        // Extract type from element
        GetPlateType request = requestElement.getGetPlateTypeElement();

        // Get a GetPlateTypeResponse wrapper
        GetPlateTypeResponse response = null;
        
        try {
            
            // Populate the GetPlateTypeResponse wrapper
            response = pip.getPlateType(request);
            
        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            GetPlateTypeFault fault = new GetPlateTypeFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            GetPlateTypeError ex = new GetPlateTypeError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        GetPlateTypeResponseElement responseElement = new GetPlateTypeResponseElement();
        responseElement.setGetPlateTypeResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.getPlateTypes
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateTypes(uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement)
     */
    @Override
    public GetPlateTypesResponseElement getPlateTypes(GetPlateTypesElement requestElement)
        throws GetPlateTypesError {

        // Extract type from element
        GetPlateTypes request = requestElement.getGetPlateTypesElement();

        // Get a GetPlateTypesResponse wrapper
        GetPlateTypesResponse response = null;
        
        try {
            
            // Populate the GetPlateTypesResponse wrapper
            response = pip.getPlateTypes(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            GetPlateTypesFault fault = new GetPlateTypesFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            GetPlateTypesError ex = new GetPlateTypesError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        GetPlateTypesResponseElement responseElement = new GetPlateTypesResponseElement();
        responseElement.setGetPlateTypesResponseElement(response);
        return responseElement;

    }

    /* ********************************************************************************************************* */
    /* *****************************          ImagingTaskProvider Methods          ***************************** */
    /* ********************************************************************************************************* */

    /**
     * WSPlateImpl.getImagingTasks
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getImagingTasks(uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement)
     */
    @Override
    public GetImagingTasksResponseElement getImagingTasks(GetImagingTasksElement requestElement)
        throws GetImagingTasksError {

        // Extract type from element
        GetImagingTasks request = requestElement.getGetImagingTasksElement();

        // Get a GetImagingTasksResponse wrapper
        GetImagingTasksResponse response = null;
        
        try {
            
            // Get a GetImagingTasksResponse wrapper and populate it
            response = itp.getImagingTasks(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            GetImagingTasksFault fault = new GetImagingTasksFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            GetImagingTasksError ex = new GetImagingTasksError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        GetImagingTasksResponseElement responseElement = new GetImagingTasksResponseElement();
        responseElement.setGetImagingTasksResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.imagingPlate
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#imagingPlate(uk.ac.ox.oppf.www.wsplate.ImagingPlateElement)
     */
    @Override
    public ImagingPlateResponseElement imagingPlate(ImagingPlateElement requestElement)
        throws ImagingPlateError {

        // Extract type from element
        ImagingPlate request = requestElement.getImagingPlateElement();

        // Get an ImagingPlateResponse wrapper
        ImagingPlateResponse response = null;
        
        try {
            
            // Populate the ImagingPlateResponse wrapper
            response = itp.imagingPlate(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            ImagingPlateFault fault = new ImagingPlateFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            ImagingPlateError ex = new ImagingPlateError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        ImagingPlateResponseElement responseElement = new ImagingPlateResponseElement();
        responseElement.setImagingPlateResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.imagedPlate
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#imagedPlate(uk.ac.ox.oppf.www.wsplate.ImagedPlateElement)
     */
    @Override
    public ImagedPlateResponseElement imagedPlate(ImagedPlateElement requestElement) throws ImagedPlateError {

        // Extract type from element
        ImagedPlate request = requestElement.getImagedPlateElement();

        // Get an ImagedPlateResponse wrapper
        ImagedPlateResponse response = null;
        
        try {
            
            // Populate the ImagedPlateResponse wrapper
            response = itp.imagedPlate(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            ImagedPlateFault fault = new ImagedPlateFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            ImagedPlateError ex = new ImagedPlateError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        ImagedPlateResponseElement responseElement = new ImagedPlateResponseElement();
        responseElement.setImagedPlateResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.skippedImaging
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#skippedImaging(uk.ac.ox.oppf.www.wsplate.SkippedImagingElement)
     */
    @Override
    public SkippedImagingResponseElement skippedImaging(SkippedImagingElement requestElement)
        throws SkippedImagingError {

        // Extract type from element
        SkippedImaging request = requestElement.getSkippedImagingElement();

        // Get a SkippedImagingResponse wrapper
        SkippedImagingResponse response = null;
        
        try {
            
            // Populate the SkippedImagingResponse wrapper
            response = itp.skippedImaging(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            SkippedImagingFault fault = new SkippedImagingFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            SkippedImagingError ex = new SkippedImagingError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        SkippedImagingResponseElement responseElement = new SkippedImagingResponseElement();
        responseElement.setSkippedImagingResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.supportsPriority
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#supportsPriority(uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement)
     */
    @Override
    public SupportsPriorityResponseElement supportsPriority(SupportsPriorityElement requestElement)
        throws SupportsPriorityError {

        // Extract type from element
        SupportsPriority request = requestElement.getSupportsPriorityElement();

        // Get a SupportsPriorityResponse wrapper
        SupportsPriorityResponse response = null;
        
        try {
            
            // Populate the SupportsPriorityResponse wrapper
            response = itp.supportsPriority(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            SupportsPriorityFault fault = new SupportsPriorityFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            SupportsPriorityError ex = new SupportsPriorityError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        SupportsPriorityResponseElement responseElement = new SupportsPriorityResponseElement();
        responseElement.setSupportsPriorityResponseElement(response);
        return responseElement;

    }

    /**
     * WSPlateImpl.updatedPriority
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#updatedPriority(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement)
     */
    @Override
    public UpdatedPriorityResponseElement updatedPriority(UpdatedPriorityElement requestElement)
        throws UpdatedPriorityError {

        // Extract type from element
        UpdatedPriority request = requestElement.getUpdatedPriorityElement();

        // Get an UpdatedPriorityResponse wrapper
        UpdatedPriorityResponse response = null;
        
        try {
            
            // Populate the UpdatedPriorityResponse wrapper
            response = itp.updatedPriority(request);

        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            UpdatedPriorityFault fault = new UpdatedPriorityFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            UpdatedPriorityError ex = new UpdatedPriorityError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            // Not applicable
        }
        
        // Return the response wrapper
        UpdatedPriorityResponseElement responseElement = new UpdatedPriorityResponseElement();
        responseElement.setUpdatedPriorityResponseElement(response);
        return responseElement;

    }

    /* ********************************************************************************************************* */
    /* *****************************           CaptureProvider Methods             ***************************** */
    /* ********************************************************************************************************* */

    /**
     * WSPlateImpl.getCapturePoints
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getCapturePoints(uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement)
     */
    @Override
    public GetCapturePointsResponseElement getCapturePoints(GetCapturePointsElement requestElement)
        throws GetCapturePointsError {
        // TODO Implement
        log.error("Call to unimplemented method getCapturePoints");

        // TODO: Add error codes and messages
        WSPlateError err = new WSPlateError();
        err.setErrorCode(-1);
        err.setMessage("getCapturePoints has not been implemented");
        err.setDetails("See uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#getCapturePoints(GetCapturePointsElement)");

        GetCapturePointsFault fault = new GetCapturePointsFault();
        fault.setArguments(requestElement.getGetCapturePointsElement());
        fault.setWSPlateError(err);

        GetCapturePointsError ex = new GetCapturePointsError();
        ex.setFaultMessage(fault);

        throw ex;
    }

    /**
     * WSPlateImpl.getDefaultCaptureProfile
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getDefaultCaptureProfile(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement)
     */
    @Override
    public GetDefaultCaptureProfileResponseElement getDefaultCaptureProfile(
        GetDefaultCaptureProfileElement requestElement) throws GetDefaultCaptureProfileError {
        // TODO Implement
        log.error("Call to unimplemented method getDefaultCaptureProfile");

        // TODO: Add error codes and messages
        WSPlateError err = new WSPlateError();
        err.setErrorCode(-1);
        err.setMessage("getDefaultCaptureProfile has not been implemented");
        err.setDetails("See uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#getDefaultCaptureProfile(GetDefaultCaptureProfileElement)");

        GetDefaultCaptureProfileFault fault = new GetDefaultCaptureProfileFault();
        fault.setArguments(requestElement.getGetDefaultCaptureProfileElement());
        fault.setWSPlateError(err);

        GetDefaultCaptureProfileError ex = new GetDefaultCaptureProfileError();
        ex.setFaultMessage(fault);

        throw ex;
    }

    /**
     * WSPlateImpl.getFirstDrop
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getFirstDrop(uk.ac.ox.oppf.www.wsplate.GetFirstDropElement)
     */
    @Override
    public GetFirstDropResponseElement getFirstDrop(GetFirstDropElement requestElement)
        throws GetFirstDropError {
        // TODO Implement
        log.error("Call to unimplemented method getFirstDrop");

        // TODO: Add error codes and messages
        WSPlateError err = new WSPlateError();
        err.setErrorCode(-1);
        err.setMessage("getFirstDrop has not been implemented");
        err.setDetails("See uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#getFirstDrop(GetFirstDropElement)");

        GetFirstDropFault fault = new GetFirstDropFault();
        fault.setArguments(requestElement.getGetFirstDropElement());
        fault.setWSPlateError(err);

        GetFirstDropError ex = new GetFirstDropError();
        ex.setFaultMessage(fault);

        throw ex;
    }

    /* ********************************************************************************************************* */
    /* *****************************        ImageProcessorProvider Methods         ***************************** */
    /* ********************************************************************************************************* */
    /**
     * WSPlateImpl.getImageProcessor
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getImageProcessor(uk.ac.ox.oppf.www.wsplate.GetImageProcessorElement)
     */
    @Override
    public GetImageProcessorResponseElement getImageProcessor(
        GetImageProcessorElement requestElement) throws GetImageProcessorError {
        // TODO Implement - not actually sure it is meaningfully implementable
        log.error("Call to unimplemented method getImageProcessor");

        // TODO: Add error codes and messages
        WSPlateError err = new WSPlateError();
        err.setErrorCode(-1);
        err.setMessage("getImageProcessor has not been implemented");
        err.setDetails("See uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#getImageProcessor(GetImageProcessorElement)");

        GetImageProcessorFault fault = new GetImageProcessorFault();
        fault.setArguments(requestElement.getGetImageProcessorElement());
        fault.setWSPlateError(err);

        GetImageProcessorError ex = new GetImageProcessorError();
        ex.setFaultMessage(fault);

        throw ex;
    }

    /**
     * WSPlateImpl.uploadImages
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#uploadImages(uk.ac.ox.oppf.www.wsplate.UploadImagesElement)
     */
    @Override
    public UploadImagesResponseElement uploadImages(UploadImagesElement requestElement)
        throws UploadImagesError {

        UploadImages request = requestElement.getUploadImagesElement();
        ArrayUploadImageResponse responseWrapper = new ArrayUploadImageResponse();
        UploadImagesResponse response = new UploadImagesResponse();
        UploadImagesResponseElement responseElement = new UploadImagesResponseElement();

        DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();
        try {
            dataStorage.openResources(dsfp.getUsername());

            ImagerService imagerService = dataStorage.getImagerService();
            TrialService dropService = dataStorage.getTrialService();
            LocationService locationService = dataStorage.getLocationService();
            PlateInspectionService inspectionService = dataStorage.getPlateInspectionService();

            ArrayUploadImage arrayUploadImage = request.getWrapper();

            UploadImage[] uploadImage = arrayUploadImage.getItem();
            UploadImageResponse[] uploadImageResponses = new UploadImageResponse[uploadImage.length];

            Map<String, TrialDrop> dropCache = new HashMap<String, TrialDrop>();
            Map<String, Location> locationCache = new HashMap<String, Location>();
            Map<String, PlateInspection> plateInspectionCache = new HashMap<String, PlateInspection>();

            for (int i = 0; i < uploadImage.length; i++) {

                UploadImageResponse uploadImageResponse = new UploadImageResponse();
                uploadImageResponse.setOk(true);
                uploadImageResponse.setUrl(uploadImage[i].getUrl());
                uploadImageResponse.setReason("OK");

                // Find and cache drop
                String drop = uploadImage[i].getPlateID() + uploadImage[i].getWell();
                if (!dropCache.containsKey(drop)) {
                    WellPosition wellPosition = new WellPosition(uploadImage[i].getWell());
                    TrialDrop trialDrop =
                        dropService.findTrialDrop(uploadImage[i].getPlateID(), wellPosition);
                    if (null == trialDrop) {
                        uploadImageResponse.setOk(false);
                        uploadImageResponse.setReason("Failed to find record for drop: " + drop);
                        //throw new UploadImagesError("Failed to find record for drop: " + drop);
                    } else {
                        dropCache.put(drop, trialDrop);
                    }
                }

                // Find and cache location
                if (!locationCache.containsKey(uploadImage[i].getRobot().getName())) {
                    Location location = locationService.findByName(uploadImage[i].getRobot().getName());
                    if (null == location) {
                        uploadImageResponse.setOk(false);
                        uploadImageResponse.setReason("Failed to find record for robot: "
                            + uploadImage[i].getRobot().getName());
                        //throw new UploadImagesError("Failed to find record for robot: " + uploadImage[i].getRobot().getName());
                    } else {
                        locationCache.put(uploadImage[i].getRobot().getName(), location);
                    }
                }

                // Find and cache plateInspection
                if (!plateInspectionCache.containsKey(uploadImage[i].getImagingID())) {
                    PlateInspection plateInspection =
                        inspectionService.findByInspectionName(uploadImage[i].getImagingID());
                    if (null == plateInspection) {
                        uploadImageResponse.setOk(false);
                        uploadImageResponse.setReason("Failed to find record for inspection: "
                            + uploadImage[i].getImagingID());
                        //throw new UploadImagesError("Failed to find record for inspection: " + uploadImage[i].getImagingID());
                    } else {
                        plateInspectionCache.put(uploadImage[i].getImagingID(), plateInspection);
                    }
                }

                uploadImageResponses[i] = uploadImageResponse;

            }

            Collection<Image> images = new ArrayList<Image>();
            for (int i = 0; i < uploadImage.length; i++) {

                if (uploadImageResponses[i].getOk()) {

                    Image img = new Image();
                    img.setColourDepth(uploadImage[i].getColourDepth());
                    img.setDrop(dropCache.get(uploadImage[i].getPlateID() + uploadImage[i].getWell()));
                    img.setImageDate(uploadImage[i].getDateImaged());
                    img.setImagePath(uploadImage[i].getUrl());
                    if (ImageType.composite.equals(uploadImage[i].getType())) {
                        img.setImageType(org.pimslims.business.crystallization.model.ImageType.COMPOSITE);
                    } else if (ImageType.slice.equals(uploadImage[i].getType())) {
                        img.setImageType(org.pimslims.business.crystallization.model.ImageType.SLICE);
                    } else if (ImageType.zoomed.equals(uploadImage[i].getType())) {
                        img.setImageType(org.pimslims.business.crystallization.model.ImageType.ZOOMED);
                    }

                    img.setLocation(locationCache.get(uploadImage[i].getRobot().getName()));
                    img.setPlateInspection(plateInspectionCache.get(uploadImage[i].getImagingID()));
                    img.setSizeX(Math.round(uploadImage[i].getImage().getWidth()));
                    img.setSizeY(Math.round(uploadImage[i].getImage().getHeight()));
                    img.setXLengthPerPixel(uploadImage[i].getPixel().getWidth());
                    img.setYLengthPerPixel(uploadImage[i].getPixel().getHeight());

                    images.add(img);

                }

            }

            dataStorage.beginTransaction();
            // TODO ImageService#create is incomplete!
            imagerService.createAndLink(images);
            dataStorage.commit();

            responseWrapper.setItem(uploadImageResponses);
            response.setWrapper(responseWrapper);

        }

        catch (Throwable t) {
            log.error(t.getMessage(), t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Caught exception in datastorage.abort(): " + t1.getMessage(), t);
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(t.getLocalizedMessage());
            err.setDetails(t.getLocalizedMessage());

            UploadImagesFault fault = new UploadImagesFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            UploadImagesError ex = new UploadImagesError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                } catch (Throwable t1) {
                    log.error("Caught exception in datastorage.closeResources(): " + t1.getMessage(), t1);
                }
            }
        }

        responseElement.setUploadImagesResponseElement(response);
        return responseElement;
    }

    /* ********************************************************************************************************* */
    /* *****************************                Sample Methods                 ***************************** */
    /* ********************************************************************************************************* */

    /**
     * WSPlateImpl.listSamples
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#listSamples(uk.ac.ox.oppf.www.wsplate.ListSamplesElement)
     */
    @Override
    public ListSamplesResponseElement listSamples(ListSamplesElement requestElement) throws ListSamplesError {
        ListSamples request = requestElement.getListSamplesElement();
        ListSamplesResponse response = new ListSamplesResponse();
        ListSamplesResponseElement responseElement = new ListSamplesResponseElement();

        DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();
        try {
            dataStorage.openResources(dsfp.getUsername());
            ImagerService is = dataStorage.getImagerService();
            log.debug("   about to call is.findSimpleSampleViews()");
            Collection<SimpleSampleView> simpleSamples = is.findSimpleSampleViews();
            log.debug("   returned from is.findSimpleSampleViews() - found " + simpleSamples.size() + " samples");
            Sample[] samples = new Sample[simpleSamples.size()];
            int i = 0;
            for (SimpleSampleView ssv : simpleSamples) {
                // Populate samples[i]
                Sample s = new Sample();
                s.setSampleID((null != ssv.getSampleId()) ? ssv.getSampleId().toString() : "");
                s.setConstructID((null != ssv.getConstructId()) ? ssv.getConstructId().toString() : "");
                s.setProjectID((null != ssv.getProjectId()) ? ssv.getProjectId().toString() : "");
                s.setSampleName((null != ssv.getSampleName()) ? ssv.getSampleName() : "");
                s.setConstructName((null != ssv.getConstructName()) ? ssv.getConstructName() : "");
                s.setProjectName((null != ssv.getProjectName()) ? ssv.getProjectName() : "");
                s.setSampleDetails((null != ssv.getSampleDetails()) ? ssv.getSampleDetails() : "");
                s.setConstructDetails((null != ssv.getConstructDetails()) ? ssv.getConstructDetails() : "");
                s.setProjectDetails((null != ssv.getProjectDetails()) ? ssv.getProjectDetails() : "");
                s.setProjectLeader((null != ssv.getProjectLeader()) ? ssv.getProjectLeader() : "");
                samples[i] = s;
                i++;
            }
            ArraySample arraySample = new ArraySample();
            arraySample.setItem(samples);
            response.setWrapper(arraySample);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Caught exception in datastorage.abort(): " + t1.getMessage(), t);
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage("Failed to list samples");
            err.setDetails(t.getLocalizedMessage());

            ListSamplesFault fault = new ListSamplesFault();
            fault.setArguments(request);
            fault.setWSPlateError(err);

            ListSamplesError ex = new ListSamplesError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                } catch (Throwable t1) {
                    log.error("Caught exception in datastorage.closeResources(): " + t1.getMessage(), t1);
                }
            }
        }

        responseElement.setListSamplesResponseElement(response);
        return responseElement;
    }

    /**
     * WSPlateImpl.canListSamples
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#canListSamples()
     */
    @Override
    public CanListSamplesResponse canListSamples() throws CanListSamplesError {
        CanListSamplesResponse response = new CanListSamplesResponse();

        DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();
        try {
            dataStorage.openResources(dsfp.getUsername());
            response.setCanListSamplesResponse(true);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Caught exception in datastorage.abort(): " + t1.getMessage(), t);
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage("Failed to list samples");
            err.setDetails(t.getLocalizedMessage());

            CanListSamplesFault fault = new CanListSamplesFault();
            fault.setWSPlateError(err);

            CanListSamplesError ex = new CanListSamplesError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                } catch (Throwable t1) {
                    log.error("Caught exception in datastorage.closeResources(): " + t1.getMessage(), t1);
                }
            }
        }

        return response;
    }

    /**
     * WSPlateImpl.listProjects
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#listProjects()
     */
    @Override
    public ListProjectsResponseElement listProjects(ListProjectsElement listProjectsElement)
        throws ListProjectsError {
        ListProjectsResponseElement responseElement = new ListProjectsResponseElement();

        DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();
        try {
            dataStorage.openResources(dsfp.getUsername());

            // In the case of PiMS I need to know the name of the AccessObjects
            // for which this user has create permission. Neither GroupServiceImpl
            // nor ProjectServiceImpl actually provide this information. I am not
            // sure whether either of those are actually used or useful.
            ImagerService imagerService = dataStorage.getImagerService();
            Collection<org.pimslims.business.core.model.Project> xProjects = imagerService.findProjects();

            Project[] projects = new Project[xProjects.size()];
            int i = 0;
            for (org.pimslims.business.core.model.Project xProject : xProjects) {
                Project project = new Project();
                project.setProjectID(xProject.getId().toString());
                project.setProjectName(xProject.getName());
                project.setProjectDetails(xProject.getDescription());
                projects[i] = project;
                i++;
            }

            ArrayProject array = new ArrayProject();
            array.setItem(projects);

            ListProjectsResponse response = new ListProjectsResponse();
            response.setWrapper(array);

            responseElement.setListProjectsResponseElement(response);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Caught exception in datastorage.abort(): " + t1.getMessage(), t1);
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage("Failed to list projects");
            err.setDetails(t.getLocalizedMessage());

            ListProjectsFault fault = new ListProjectsFault();
            fault.setWSPlateError(err);

            ListProjectsError ex = new ListProjectsError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                } catch (Throwable t1) {
                    log.error("Caught exception in datastorage.closeResources(): " + t1.getMessage(), t1);
                }
            }
        }

        return responseElement;
    }

}
