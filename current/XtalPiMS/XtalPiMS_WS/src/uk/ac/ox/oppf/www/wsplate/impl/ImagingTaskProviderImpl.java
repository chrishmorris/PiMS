/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl ImagingTaskProviderImpl.java
 * @author jon
 * @date 24 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.exception.BusinessException;

import uk.ac.ox.oppf.www.wsplate.ArrayImagingTask;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasks;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksError;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponse;
import uk.ac.ox.oppf.www.wsplate.ImagedPlate;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingPlate;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingTask;
import uk.ac.ox.oppf.www.wsplate.SkippedImaging;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingError;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingResponse;
import uk.ac.ox.oppf.www.wsplate.SupportsPriority;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityError;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponse;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriority;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityError;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponse;

/**
 * ImagingTaskProviderImpl
 *
 */
public class ImagingTaskProviderImpl implements ImagingTaskProvider {

    private static Logger log = LogManager.getLogger(ImagingTaskProvider.class);
    
    /**
     * Abstraction of the provision of a DataStorageFactoy and username
     * to assist in testing.
     */
    final DataStorageFactoryProvider dsfp;
    
    /**
     * 
     * Constructor for PlateInfoProviderImpl
     */
    public ImagingTaskProviderImpl() {
        this(new AxisDataStorageFactoryProvider());
    }
    
    /**
     * 
     * Constructor for PlateInfoProviderImpl 
     * @param dsfp
     */
    public ImagingTaskProviderImpl(DataStorageFactoryProvider dsfp) {
        this.dsfp = dsfp;
    }
    
    /**
     * ImagingTaskProviderImpl.getImagingTasks
     * 
     * NB This method commits changes to the data store! Use {@link #_getImagingTasks(GetImagingTasks, DataStorage)}
     * for testing purposes.
     * 
     * @throws GetImagingTasksError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#getImagingTasks(uk.ac.ox.oppf.www.wsplate.GetImagingTasks)
     */
    @Override
    public GetImagingTasksResponse getImagingTasks(final GetImagingTasks request) throws GetImagingTasksError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());
            
            // Split out for testability
            final GetImagingTasksResponse response = _getImagingTasks(request, dataStorage);
            
            dataStorage.commit();
            
            // Return the response
            return response;
        }
        catch (Throwable t) {
            log.error("Error in getImagingTasks", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new GetImagingTasksError(t.getMessage(), t);
        }
        finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                }
                catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * ImagingTaskProviderImpl.imagingPlate
     * 
     * NB This method commits changes to the data store! Use {@link #_imagingPlate(ImagingPlate, DataStorage)}
     * for testing purposes.
     * 
     * @throws ImagingPlateError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#imagingPlate(uk.ac.ox.oppf.www.wsplate.ImagingPlate)
     */
    @Override
    public ImagingPlateResponse imagingPlate(final ImagingPlate request) throws ImagingPlateError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());
            
            // Split out for testability
            final ImagingPlateResponse response = _imagingPlate(request, dataStorage);
            
            dataStorage.commit();
            
            // Return the response
            return response;
        }
        catch (Throwable t) {
            log.error("Error in imagingPlate", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new ImagingPlateError(t.getMessage(), t);
        }
        finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                }
                catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * ImagingTaskProviderImpl.imagedPlate
     * 
     * NB This method commits changes to the data store! Use {@link #_imagedPlate(ImagedPlate, DataStorage)}
     * for testing purposes.
     * 
     * @throws ImagedPlateError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#imagedPlate(uk.ac.ox.oppf.www.wsplate.ImagedPlate)
     */
    @Override
    public ImagedPlateResponse imagedPlate(final ImagedPlate request) throws ImagedPlateError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());
            
            // Split out for testability
            final ImagedPlateResponse response = _imagedPlate(request, dataStorage);
            
            dataStorage.commit();
            
            // Return the response
            return response;
        }
        catch (Throwable t) {
            log.error("Error in imagedPlate", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new ImagedPlateError(t.getMessage(), t);
        }
        finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                }
                catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * ImagingTaskProviderImpl.skippedImaging
     * 
     * NB This method commits changes to the data store! Use {@link #_skippedImaging(SkippedImaging, DataStorage)}
     * for testing purposes.
     * 
     * @throws SkippedImagingError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#skippedImaging(uk.ac.ox.oppf.www.wsplate.SkippedImaging)
     */
    @Override
    public SkippedImagingResponse skippedImaging(final SkippedImaging request) throws SkippedImagingError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());
            
            // Split out for testability
            final SkippedImagingResponse response = _skippedImaging(request, dataStorage);
            
            dataStorage.commit();
            
            // Return the response
            return response;
        }
        catch (Throwable t) {
            log.error("Error in skippedImaging", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new SkippedImagingError(t.getMessage(), t);
        }
        finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                }
                catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * ImagingTaskProviderImpl.supportsPriority
     * 
     * This method makes no changes to the data store, though the meat of the method is
     * delegated to {@link #_supportsPriority(SupportsPriority, DataStorage)} for consistency
     * with the other methods.
     * 
     * @throws SupportsPriorityError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#supportsPriority(uk.ac.ox.oppf.www.wsplate.SupportsPriority)
     */
    @Override
    public SupportsPriorityResponse supportsPriority(final SupportsPriority request) throws SupportsPriorityError {
        try {
            // Split out for testability
            final SupportsPriorityResponse response = _supportsPriority(request, null);
            return response;
        }
        catch (Throwable t) {
            log.error("Error in supportsPriority", t);
            throw new SupportsPriorityError(t.getMessage(), t);
        }
    }

    /**
     * ImagingTaskProviderImpl.updatedPriority
     * 
     * NB This method commits changes to the data store! Use {@link #_updatedPriority(UpdatedPriority, DataStorage)}
     * for testing purposes.
     * 
     * @throws UpdatedPriorityError 
     * @see uk.ac.ox.oppf.www.wsplate.impl.ImagingTaskProvider#updatedPriority(uk.ac.ox.oppf.www.wsplate.UpdatedPriority)
     */
    @Override
    public UpdatedPriorityResponse updatedPriority(final UpdatedPriority request) throws UpdatedPriorityError {
        DataStorage dataStorage = null;
        try {
            dataStorage = dsfp.getDataStorageFactory().getDataStorage();
            dataStorage.connectResources();
            dataStorage.openResources(dsfp.getUsername());
            
            // Split out for testability
            final UpdatedPriorityResponse response = _updatedPriority(request, dataStorage);
            
            dataStorage.commit();
            
            // Return the response
            return response;
        }
        catch (Throwable t) {
            log.error("Error in updatedPriority", t);
            try {
                dataStorage.abort();
            } catch (Throwable t1) {
                log.error("Error aborting resources whilst handling exception", t1);
            }
            throw new UpdatedPriorityError(t.getMessage(), t);
        }
        finally {
            if (null != dataStorage) {
                try {
                    dataStorage.closeResources();
                    dataStorage.disconnectResources();
                }
                catch (Throwable t) {
                    log.error("Error closing resources", t);
                }
            }
        }
    }

    /**
     * ImagingTaskProviderImpl._getImagingTasks
     * @param request
     * @param dataStorage
     * @return
     * @throws BusinessException
     * @see #getImagingTasks(GetImagingTasks)
     */
    protected GetImagingTasksResponse _getImagingTasks(final GetImagingTasks request, final DataStorage dataStorage) throws BusinessException {
        final ImagerService imagerService = dataStorage.getImagerService();
        Collection<ScheduleView> schedules = imagerService.findSchedules(request.getPlateID(), request.getRobot().getName());
        
        if (0 == schedules.size()) {
            log.warn("_getImagingTasks(): scheduling plate " + request.getPlateID());
            schedules = imagerService.schedulePlate(request.getPlateID(), request.getRobot().getName());
            log.debug("_getImagingTasks(): scheduled plate " + request.getPlateID());
        }
        
        final ImagingTask[] imagingTasks = new ImagingTask[schedules.size()];
        
        int i = 0;
        for (ScheduleView schedule: schedules) {
            
            imagingTasks[i] = new ImagingTask();
            imagingTasks[i].setDateToImage(schedule.getDateToImage());
            imagingTasks[i].setDateImaged(schedule.getDateImaged());
            imagingTasks[i].setPriority(schedule.getPriority());
            imagingTasks[i].setState(schedule.getState());
            i++;
        }
        
        final ArrayImagingTask array = new ArrayImagingTask();
        array.setItem(imagingTasks);
        
        // Get a GetImagingTasksResponse wrapper and populate it
        final GetImagingTasksResponse response = new GetImagingTasksResponse();
        response.setWrapper(array);
        
        return response;
    }

    /**
     * ImagingTaskProviderImpl._imagingPlate
     * @param request
     * @param dataStorage
     * @return
     * @throws BusinessException
     * @see {@link #imagingPlate(ImagingPlate)}
     */
    protected ImagingPlateResponse _imagingPlate(final ImagingPlate request, final DataStorage dataStorage) throws BusinessException {
        final ImagerService imagerService = dataStorage.getImagerService();
        String sessionId = null;
        if (! request.getScheduled()) {
            sessionId = imagerService.startedUnscheduledImaging(request.getPlateID(), request.getDateToImage(), request.getDateImaged(), request.getRobot().getName());
        }
        else {
            sessionId = imagerService.startedImaging(request.getPlateID(), request.getDateToImage(), request.getDateImaged(), request.getRobot().getName());
        }
        
        final ImagingPlateResponse response = new ImagingPlateResponse();
        response.setImagingPlateReturn(sessionId);
        
        return response;
    }

    /**
     * ImagingTaskProviderImpl._imagedPlate
     * @param request
     * @param dataStorage
     * @return
     * @throws BusinessException 
     * @see #imagedPlate(ImagedPlate)
     */
    protected ImagedPlateResponse _imagedPlate(final ImagedPlate request, final DataStorage dataStorage) throws BusinessException {
        final ImagerService imagerService = dataStorage.getImagerService();
        final boolean ret = imagerService.finishedImaging(request.getPlateID(), request.getImagingID(), request.getRobot().getName());
        
        final ImagedPlateResponse response = new ImagedPlateResponse();
        response.setImagedPlateReturn(ret);
        
        return response;
    }

    /**
     * ImagingTaskProviderImpl._skippedImaging
     * @param request
     * @param dataStorage
     * @return
     * @throws BusinessException
     * @see #skippedImaging(SkippedImaging)
     */
    protected SkippedImagingResponse _skippedImaging(final SkippedImaging request, final DataStorage dataStorage) throws BusinessException {
        final ImagerService imagerService = dataStorage.getImagerService();
        final boolean ret = imagerService.skippedImaging(request.getPlateID(), request.getDateToImage(), request.getRobot().getName());
        
        final SkippedImagingResponse response = new SkippedImagingResponse();
        response.setSkippedPlateReturn(ret);
        
        return response;
    }

    /**
     * ImagingTaskProviderImpl._supportsPriority
     * @param request
     * @param dataStorage
     * @return
     * @see #supportsPriority(SupportsPriority)
     */
    protected SupportsPriorityResponse _supportsPriority(final SupportsPriority request, final DataStorage dataStorage) {
        final SupportsPriorityResponse response = new SupportsPriorityResponse();
        response.setSupportsPriorityReturn(true);
        return response;
    }

    /**
     * ImagingTaskProviderImpl._updatedPriority
     * @param request
     * @param dataStorage
     * @return
     * @throws BusinessException
     * @see #updatedPriority(UpdatedPriority)
     */
    protected UpdatedPriorityResponse _updatedPriority(final UpdatedPriority request, final DataStorage dataStorage) throws BusinessException {
        final ImagerService imagerService = dataStorage.getImagerService();
        final boolean ret = imagerService.setPriority(request.getPlateID(), request.getDateToImage(), request.getRobot().getName(), request.getPriority());
        
        // Get an UpdatedPriorityResponse wrapper and populate it
        final UpdatedPriorityResponse response = new UpdatedPriorityResponse();
        response.setUpdatedPriorityReturn(ret);
        
        return response;
    }

}
