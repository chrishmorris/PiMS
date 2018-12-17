/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.implementation ImagerService.java
 * 
 * @author jon
 * @date 25 Aug 2010
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2010 jon The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.service;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.core.model.Project;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.SimpleSampleView;
import org.pimslims.business.exception.BusinessException;

/**
 * ImagerService
 * 
 */
public interface ImagerService {

    /**
     * Indicates a scheduled imaging that has not yet been acted on.
     */
    public static final int IMAGING_STATE_SCHEDULED = 1;

    /**
     * Imaging session has been skipped.
     */
    public static final int IMAGING_STATE_SKIPPED = 2;

    /**
     * Imaging session is being prepped.
     */
    public static final int IMAGING_STATE_PENDING = 3;

    /**
     * Imager has begun acting on this session, but not yet started imaging.
     */
    public static final int IMAGING_STATE_QUEUED = 4;

    /**
     * Imager has begun imaging this session.
     */
    public static final int IMAGING_STATE_IMAGING = 5;

    /**
     * Imaging session has been completed.
     */
    public static final int IMAGING_STATE_COMPLETED = 6;

    /**
     * 
     * ImagerService.findSchedules
     * 
     * @param barcode
     * @param robot
     * @return
     * @throws BusinessException
     */
    Collection<ScheduleView> findSchedules(final String barcode, final String robot) throws BusinessException;

    /**
     * 
     * ImagerService.schedulePlate
     * 
     * @param barcode
     * @param robot
     * @return
     * @throws BusinessException
     */
    public Collection<ScheduleView> schedulePlate(final String barcode, final String robot)
        throws BusinessException;

    /**
     * 
     * ImagerService.startedImaging
     * 
     * @param barcode
     * @param dateToImage
     * @param dateImaged
     * @param robot
     * @return
     * @throws BusinessException
     */
    String startedImaging(final String barcode, final Calendar dateToImage, final Calendar dateImaged,
        final String robot) throws BusinessException;

    /**
     * 
     * ImagerService.startedUnscheduledImaging
     * 
     * @param barcode
     * @param dateToImage
     * @param dateImaged
     * @param robot
     * @return
     * @throws BusinessException
     */
    String startedUnscheduledImaging(final String barcode, final Calendar dateToImage,
        final Calendar dateImaged, final String robot) throws BusinessException;

    /**
     * 
     * ImagerService.finishedImaging
     * 
     * @param sessionId
     * @return
     * @throws BusinessException
     */
    boolean finishedImaging(final String barcode, final String sessionId, final String robot)
        throws BusinessException;

    /**
     * 
     * ImagerService.skippedImaging
     * 
     * @param barcode
     * @param dateToImage
     * @param robot
     * @return
     * @throws BusinessException
     */
    boolean skippedImaging(final String barcode, final Calendar dateToImage, final String robot)
        throws BusinessException;

    /**
     * 
     * ImagerService.setPriority
     * 
     * @param barcode
     * @param dateToImage
     * @param robot
     * @param priority
     * @return
     * @throws BusinessException
     */
    boolean setPriority(final String barcode, final Calendar dateToImage, final String robot,
        final int priority) throws BusinessException;

    /**
     * 
     * ImagerService.findSimpleSampleViews
     * 
     * @return
     * @throws BusinessException
     */
    Collection<SimpleSampleView> findSimpleSampleViews() throws BusinessException;

    /**
     * ImagerService.createAndLink
     * 
     * @param image
     * @throws BusinessException
     */
    void createAndLink(Image image) throws BusinessException;

    /**
     * ImagerService.createAndLink
     * 
     * @param images
     * @throws BusinessException
     */
    void createAndLink(Collection<Image> images) throws BusinessException;

    /**
     * 
     * ImagerService.findProjects
     * 
     * @return
     * @throws BusinessException
     */
    Collection<Project> findProjects() throws BusinessException;

    /**
     * ImagerService.createInstrument
     * 
     * @param name
     * @param temperature
     * @throws BusinessException
     */
    public abstract void createInstrument(String name, float temperature) throws BusinessException;



}
