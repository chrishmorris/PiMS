/**
 * ImageSearch.java
 *
 *
 * org.pimslims.crystallization.servlet ImageSearch
 * @author Ian Berry
 * @date 04 October 2007
 *
 * Protein Information Management System
 * @version: 1.3
 *
 * Copyright (c) 2007 Ian Berry, University Of Oxford
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */

package org.pimslims.crystallization.servlet;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.exception.BusinessException;

@Deprecated
// does not work, presumably not used
public class ImageSearch {

	/** Creates a new instance of ImageSearch. */
	public ImageSearch() {
	}

	public Collection<Image> findImages(String type, int index, String barcode,
			String well, String inspectionName, ImageType imageType,
			DataStorage dataStorage) throws BusinessException {
		Collection<Image> imageList = null;

		// Then we are doing a plate!
		// TODO FIX THIS
		// PlateInspectionService plateInspectionService =
		// dataStorage.getPlateInspectionService();
		// PlateInspection plateInspection =
		// plateInspectionService.findByInspectionName(inspectionName);
		// ImageService imageService = dataStorage.getImageService();
		// imageList = imageService.findImages(barcode, plateInspection,
		// imageType, null);
		//        
		// if ((type.equals("TimeCourse")) && (index != -1)) {
		// //Then we are doing a timecourse
		// imageList = imageService.findImages(barcode, imageList.toArray(new
		// Image[0])[index].getDrop().getWellPosition(), imageType, false,
		// null);
		//            
		// }

		return imageList;
	}

}
