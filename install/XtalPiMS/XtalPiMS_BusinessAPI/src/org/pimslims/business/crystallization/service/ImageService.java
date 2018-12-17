/*
 * ImageService.java Created on 17 April 2007, 09:11 To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;

/**
 * This Interface describes the methods required to access and add details of the images stored in the PiMS
 * database.
 * 
 * @author IMB
 */
public interface ImageService extends BaseService, ViewService<ImageView> {

    /**
     * Since an id may be a long or it may be a string or perhaps something else in the database, provide
     * different accessors for this....
     * 
     * @param id The id to search for
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Image find(long id) throws BusinessException;

    /**
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Image> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
	 * Gets the latest images available for a plate. If no images have been
	 * taken, or the plate barcode is invalid, null is returned.
	 * 
	 * @return A list of images requested
	 * @param barcode
	 *            The Plate reference
	 * @param imageType
	 *            The type of image requested (High resolution, Medium
	 *            Resolution, Composite, Thumbnail)
	 * @param paging
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated // not implemented public Collection<ImageView>
	 *             findLatestImages(BusinessCriteria criteria) throws
	 *             BusinessException;
	 */

    /**
	 * Adds details of an image to the database
	 * 
	 * @param image
	 *            The image data
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated // use saveAndLink public void create(Image image) throws
	 *             BusinessException;
	 */

    /**
     * Adds details of multiple images to the database
     * 
     * @param image list of image data to add to the database
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Collection<Image> image) throws BusinessException;

    /**
	 * 
	 * @param image
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated public void update(Image image) throws BusinessException;
	 */

    /**
     * 
     * @param images
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Collection<Image> images) throws BusinessException;

}
