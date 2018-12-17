package org.pimslims.crystallization.dao;

import org.pimslims.business.XtalObject;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.metamodel.AbstractModelObject;

/**
 * PO: pims' Object
 * 
 * XO: xtalPiMS' Object
 * 
 * This interface defined:
 * 
 * PO->XO: Convert PO to Simple or Full XO
 * 
 * XO->PO: Create/Update/Find PO from XO
 * 
 * @param P
 * @param X
 * @author Bill Lin
 */
public interface XPDAOInterface<P extends AbstractModelObject, X extends XtalObject> {

    /**
     * PO->XO: Convert PO to Simple XO
     * 
     * Simple XO have attribues like: xPerson's name, username
     * 
     * It do NOT have related XO(xRole) like: xPerson's group
     * 
     * @param <X>
     * @param pobject
     * @return
     * @throws BusinessException
     */
    public abstract X getSimpleXO(P pobject) throws BusinessException;

    /**
     * PO->XO: Convert PO to Full XO
     * 
     * Addition to Simple XO, Full XO have related XO(xRole) in Simple like: xPerson's group
     * 
     * @param <X>
     * @param pobject
     * @return
     * @throws BusinessException
     */

    @SuppressWarnings("unchecked")
    public abstract X getFullXO(P pobject) throws BusinessException;

    /**
     * XO->PO: Create PO from XO
     * 
     * @param <T>
     * @param xobject
     * @return
     * @throws BusinessException
     */
    public abstract P createPO(X xobject) throws BusinessException;

    /**
     * XO->PO: Update PO from XO
     * 
     * @param <T>
     * @param xobject
     * @return
     * @throws BusinessException
     */
    public abstract P updatePO(X xobject) throws BusinessException;

    /**
     * XO->PO: Find PO from XO
     * 
     * @param <T>
     * @param xobject
     * @return
     */
    public abstract P getPO(X xobject);

    /**
     * find or create a pObject by a xObject
     * 
     * @param xobject
     * @return
     * @throws BusinessException
     */
    public P findOrCreate(X xobject) throws BusinessException;

}
