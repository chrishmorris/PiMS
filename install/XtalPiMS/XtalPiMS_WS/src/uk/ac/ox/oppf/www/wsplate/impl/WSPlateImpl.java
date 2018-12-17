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
import org.pimslims.business.exception.BusinessException;

import uk.ac.ox.oppf.www.wsplate.ArrayProject;
import uk.ac.ox.oppf.www.wsplate.ArraySample;
import uk.ac.ox.oppf.www.wsplate.ArrayUploadImage;
import uk.ac.ox.oppf.www.wsplate.ArrayUploadImageResponse;
import uk.ac.ox.oppf.www.wsplate.CanListSamplesError;
import uk.ac.ox.oppf.www.wsplate.CanListSamplesFault;
import uk.ac.ox.oppf.www.wsplate.CanListSamplesResponse;
import uk.ac.ox.oppf.www.wsplate.GetCapturePointsElement;
import uk.ac.ox.oppf.www.wsplate.GetCapturePointsError;
import uk.ac.ox.oppf.www.wsplate.GetCapturePointsResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement;
import uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileError;
import uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetFirstDropElement;
import uk.ac.ox.oppf.www.wsplate.GetFirstDropError;
import uk.ac.ox.oppf.www.wsplate.GetFirstDropResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasks;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksError;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponse;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateID;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDError;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfo;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoError;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateType;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponseElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypes;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponseElement;
import uk.ac.ox.oppf.www.wsplate.ImageType;
import uk.ac.ox.oppf.www.wsplate.ImagedPlate;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateElement;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateResponseElement;
import uk.ac.ox.oppf.www.wsplate.ImagingPlate;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateElement;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateResponseElement;
import uk.ac.ox.oppf.www.wsplate.ListProjectsElement;
import uk.ac.ox.oppf.www.wsplate.ListProjectsError;
import uk.ac.ox.oppf.www.wsplate.ListProjectsFault;
import uk.ac.ox.oppf.www.wsplate.ListProjectsResponse;
import uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement;
import uk.ac.ox.oppf.www.wsplate.ListSamples;
import uk.ac.ox.oppf.www.wsplate.ListSamplesElement;
import uk.ac.ox.oppf.www.wsplate.ListSamplesError;
import uk.ac.ox.oppf.www.wsplate.ListSamplesFault;
import uk.ac.ox.oppf.www.wsplate.ListSamplesResponse;
import uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement;
import uk.ac.ox.oppf.www.wsplate.Project;
import uk.ac.ox.oppf.www.wsplate.Sample;
import uk.ac.ox.oppf.www.wsplate.SkippedImaging;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingElement;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingError;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingResponse;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingResponseElement;
import uk.ac.ox.oppf.www.wsplate.SupportsPriority;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityError;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponse;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponseElement;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriority;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityError;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponse;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponseElement;
import uk.ac.ox.oppf.www.wsplate.UploadImage;
import uk.ac.ox.oppf.www.wsplate.UploadImageResponse;
import uk.ac.ox.oppf.www.wsplate.UploadImages;
import uk.ac.ox.oppf.www.wsplate.UploadImagesElement;
import uk.ac.ox.oppf.www.wsplate.UploadImagesError;
import uk.ac.ox.oppf.www.wsplate.UploadImagesFault;
import uk.ac.ox.oppf.www.wsplate.UploadImagesResponse;
import uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement;
import uk.ac.ox.oppf.www.wsplate.WSPlateError;
import uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton;

/**
 * WSPlateImpl
 * 
 */
public class WSPlateImpl extends WSPlateSkeleton {

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
        GetPlateIDResponse response = pip.getPlateID(request);

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

        // Get a GetPlateInfoResponse wrapper and populate it
        GetPlateInfoResponse response = pip.getPlateInfo(request);

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

        // Get a GetPlateTypeResponse wrapper and populate it
        GetPlateTypeResponse response = pip.getPlateType(request);

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

        // Get a GetPlateTypesResponse wrapper and populate it
        GetPlateTypesResponse response = pip.getPlateTypes(request);

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

        // Get a GetImagingTasksResponse wrapper and populate it
        GetImagingTasksResponse response = itp.getImagingTasks(request);

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

        // Get a ImagingPlateResponse wrapper and populate it
        ImagingPlateResponse response = itp.imagingPlate(request);

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

        // Get a ImagedPlateResponse wrapper and populate it
        ImagedPlateResponse response = itp.imagedPlate(request);

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

        // Get a SkippedImagingResponse wrapper and populate it
        SkippedImagingResponse response = itp.skippedImaging(request);

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

        // Get a SupportsPriorityResponse wrapper and populate it
        SupportsPriorityResponse response = itp.supportsPriority(request);

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

        // Get an UpdatedPriorityResponse wrapper and populate it
        UpdatedPriorityResponse response = itp.updatedPriority(request);

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
        // TODO Auto-generated method stub
        return super.getCapturePoints(requestElement);
    }

    /**
     * WSPlateImpl.getDefaultCaptureProfile
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getDefaultCaptureProfile(uk.ac.ox.oppf.www.wsplate.GetDefaultCaptureProfileElement)
     */
    @Override
    public GetDefaultCaptureProfileResponseElement getDefaultCaptureProfile(
        GetDefaultCaptureProfileElement requestElement) throws GetDefaultCaptureProfileError {
        // TODO Auto-generated method stub
        return super.getDefaultCaptureProfile(requestElement);
    }

    /**
     * WSPlateImpl.getFirstDrop
     * 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getFirstDrop(uk.ac.ox.oppf.www.wsplate.GetFirstDropElement)
     */
    @Override
    public GetFirstDropResponseElement getFirstDrop(GetFirstDropElement requestElement)
        throws GetFirstDropError {
        // TODO Auto-generated method stub
        return super.getFirstDrop(requestElement);
    }

    /* ********************************************************************************************************* */
    /* *****************************        ImageProcessorProvider Methods         ***************************** */
    /* ********************************************************************************************************* */

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

        catch (BusinessException e) {
            // TODO Log instead
            e.printStackTrace();
            try {
                dataStorage.abort();
            } catch (BusinessException e1) {
                // TODO Log instead
                e1.printStackTrace();
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage(e.getLocalizedMessage());
            err.setDetails(e.getLocalizedMessage());

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
                } catch (BusinessException e1) {
                    // TODO Log instead
                    e1.printStackTrace();
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
            Collection<SimpleSampleView> simpleSamples = is.findSimpleSampleViews();
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
        } catch (BusinessException e) {
            try {
                dataStorage.abort();
            } catch (BusinessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage("Failed to list samples");
            err.setDetails(e.getLocalizedMessage());

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
                } catch (BusinessException e1) {
                    // TODO Log instead
                    e1.printStackTrace();
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
        } catch (BusinessException e) {
            try {
                dataStorage.abort();
            } catch (BusinessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // TODO: Add error codes and messages
            WSPlateError err = new WSPlateError();
            err.setErrorCode(-1);
            err.setMessage("Failed to list samples");
            err.setDetails(e.getLocalizedMessage());

            CanListSamplesFault fault = new CanListSamplesFault();
            fault.setWSPlateError(err);

            CanListSamplesError ex = new CanListSamplesError();
            ex.setFaultMessage(fault);

            throw ex;
        } finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                } catch (BusinessException e1) {
                    // TODO Log instead
                    e1.printStackTrace();
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
            try {
                dataStorage.abort();
            } catch (BusinessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
                } catch (BusinessException e1) {
                    // TODO Log instead
                    e1.printStackTrace();
                }
            }
        }

        return responseElement;
    }

}
