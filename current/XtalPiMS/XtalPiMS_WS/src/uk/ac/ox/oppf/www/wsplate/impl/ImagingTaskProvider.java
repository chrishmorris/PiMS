/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl ImagingTaskProvider.java
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

import uk.ac.ox.oppf.www.wsplate.GetImagingTasks;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksError;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponse;
import uk.ac.ox.oppf.www.wsplate.ImagedPlate;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingPlate;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateResponse;
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
 * ImagingTaskProvider
 *
 */
public interface ImagingTaskProvider {

    /**
     * ImagingTaskProvider.getImagingTasks
     * @param request
     * @return
     * @throws GetImagingTasksError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getImagingTasks(uk.ac.ox.oppf.www.wsplate.GetImagingTasksElement)
     */
    GetImagingTasksResponse getImagingTasks(GetImagingTasks request) throws GetImagingTasksError;

    /**
     * ImagingTaskProvider.imagingPlate
     * @param request
     * @return
     * @throws ImagingPlateError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#imagingPlate(uk.ac.ox.oppf.www.wsplate.ImagingPlateElement)
     */
    ImagingPlateResponse imagingPlate(ImagingPlate request) throws ImagingPlateError;

    /**
     * ImagingTaskProvider.imagedPlate
     * @param request
     * @return
     * @throws ImagedPlateError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#imagedPlate(uk.ac.ox.oppf.www.wsplate.ImagedPlateElement)
     */
    ImagedPlateResponse imagedPlate(ImagedPlate request) throws ImagedPlateError;

    /**
     * ImagingTaskProvider.skippedImaging
     * @param request
     * @return
     * @throws SkippedImagingError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#skippedImaging(uk.ac.ox.oppf.www.wsplate.SkippedImagingElement)
     */
    SkippedImagingResponse skippedImaging(SkippedImaging request) throws SkippedImagingError;

    /**
     * ImagingTaskProvider.supportsPriority
     * @param request
     * @return
     * @throws SupportsPriorityError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#supportsPriority(uk.ac.ox.oppf.www.wsplate.SupportsPriorityElement)
     */
    SupportsPriorityResponse supportsPriority(SupportsPriority request) throws SupportsPriorityError;

    /**
     * ImagingTaskProvider.updatedPriority
     * @param request
     * @return
     * @throws UpdatedPriorityError 
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#updatedPriority(uk.ac.ox.oppf.www.wsplate.UpdatedPriorityElement)
     */
    UpdatedPriorityResponse updatedPriority(UpdatedPriority request) throws UpdatedPriorityError;

}
