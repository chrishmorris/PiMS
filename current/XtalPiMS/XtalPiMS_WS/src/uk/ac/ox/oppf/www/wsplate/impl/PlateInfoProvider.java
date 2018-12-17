/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl PlateInfoProvider.java
 * @author jon
 * @date 23 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

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

/**
 * PlateInfoProvider
 *
 */
public interface PlateInfoProvider {

    /**
     * WSPlateImpl.getPlateID
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateIDElement)
     */
    GetPlateIDResponse getPlateID(GetPlateID request) throws GetPlateIDError;

    /**
     * WSPlateImpl.getPlateInfo
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateInfo(uk.ac.ox.oppf.www.wsplate.GetPlateInfoElement)
     */
    GetPlateInfoResponse getPlateInfo(GetPlateInfo request) throws GetPlateInfoError;

    /**
     * WSPlateImpl.getPlateType
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateType(uk.ac.ox.oppf.www.wsplate.GetPlateTypeElement)
     */
    GetPlateTypeResponse getPlateType(GetPlateType request) throws GetPlateTypeError;

    /**
     * WSPlateImpl.getPlateTypes
     * @see uk.ac.ox.oppf.www.wsplate.WSPlateSkeleton#getPlateTypes(uk.ac.ox.oppf.www.wsplate.GetPlateTypesElement)
     */
    GetPlateTypesResponse getPlateTypes(GetPlateTypes request) throws GetPlateTypesError;

}
