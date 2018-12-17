package org.pimslims.business.criteria;

import org.pimslims.business.exception.BusinessException;

public interface PropertyNameConvertor<T> {
    /**
     * This function will convert the names of the xtalPiMS view's properties into the properties that the
     * database will understand when building up the HQL query.
     * 
     * @param property the name of the view property to convert
     * @return the equivalent db property name
     * @throws org.pimslims.business.exception.BusinessException
     */
    public String convertPropertyName(String property) throws BusinessException;
    
}
