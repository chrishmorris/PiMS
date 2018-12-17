package org.pimslims.crystallization.dao;

import java.util.Map;

import org.pimslims.logging.Logger;
import org.pimslims.business.XtalObject;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.ModelObject;

/**
 * GenericDAO is an abstract DAO. It should be extended to do:
 * 
 * PO->XO: Convert PO to Simple or Full XO
 * 
 * XO->PO: Create/Update/Find PO from XO
 * 
 * @param P
 * @param X
 * @author Bill Lin
 */
public abstract class GenericDAO<P extends AbstractModelObject, X extends XtalObject> implements
    XPDAOInterface<P, X> {

    protected final ReadableVersion version;

    private static final Logger log = Logger.getLogger(GenericDAO.class);

    /**
     * @param version
     */
    public GenericDAO(final ReadableVersion version) {
        super();
        this.version = version;
        assert version != null;
    }

    /**
     * @throws BusinessException
     * @see org.pimslims.crystallization.dao.XPDAOInterface#getSimpleXO(P)
     */
    public X getSimpleXO(final P pobject) throws BusinessException {
        if (pobject == null) {
            return null;
        }

        final X xobject = loadXAttribute(pobject);
        xobject.setId(pobject.getDbId());
        log.debug("getSimpleXO: " + xobject + " is loaded from " + pobject);
        return xobject;
    }

    /**
     * this abstract method should create a XO and set xAtttibutes
     * 
     * @param <X>
     * @param pobject
     * @return
     * @throws BusinessException
     */
    protected abstract X loadXAttribute(P pobject) throws BusinessException;

    /**
     * @throws BusinessException
     * @see org.pimslims.crystallization.dao.XPDAOInterface#getFullXO(P)
     */
    @SuppressWarnings("unchecked")
    public X getFullXO(final P pobject) throws BusinessException {
        if (pobject == null) {
            return null;
        }
        X xobject = loadXAttribute(pobject);
        if (xobject == null) {
            //pobject is not a real xobject
            return null;
        }
        xobject = loadXRole(xobject, pobject);
        xobject.setId(pobject.getDbId());
        log.debug("getFullXO: " + xobject + " is loaded from " + pobject);
        return xobject;
    }

    /**
     * this abstract method should set xRole
     * 
     * @param xobject
     * @param <X>
     * @param pobject
     * @return
     * @throws BusinessException
     */
    protected abstract X loadXRole(X xobject, P pobject) throws BusinessException;

    /**
     * @throws org.pimslims.business.exception.BusinessException
     * @see org.pimslims.crystallization.dao.XPDAOInterface#createPO(org.pimslims.business.XtalObject)
     */
    //IMB Added this to get rid of warning
    @SuppressWarnings("unchecked")
    public P createPO(final X xobject) throws BusinessException {
        if (xobject == null) {
            return null;
        }
        //find by full attributes
        P pObject = null;
        try {
            //IMB Added cast (although its unchecked) as my install was
            //complaining that this was not a valid statement
            //discussion: http://forum.java.sun.com/thread.jspa?messageID=4196171
            //TODO this is slow
            pObject = (P) getWritableVersion().create(getPClass(), getFullAttributes(xobject));
            xobject.setId(pObject.getDbId());
            createPORelated(pObject, xobject);
            log.debug("CreatePO: " + pObject + " is created from " + xobject);
        } catch (final ModelException e) {
            log.error("CreatePO: " + e.getMessage());
            throw new BusinessException(e);
        }

        return pObject;
    }

    /**
     * Addition to createPO: create other related pims object
     * 
     * @param object
     * @param xobject
     * @throws ConstraintException
     * @throws BusinessException
     * @throws ModelException
     */
    protected abstract void createPORelated(P pobject, X xobject) throws ConstraintException,
        BusinessException, ModelException;

    /**
     * @throws org.pimslims.business.exception.BusinessException
     * @see org.pimslims.crystallization.dao.XPDAOInterface#updatePO(org.pimslims.business.XtalObject)
     */
    public P updatePO(final X xobject) throws BusinessException {

        //find PO first
        final P pObject = getPO(xobject);
        if (pObject == null) {
            log.error("Can not find related pims object by " + xobject);
            throw new BusinessException("Can not find related pims object by " + xobject);
        }
        try {
            //update with full attributes
            pObject.set_Values(getFullAttributes(xobject));
            updatePORelated(pObject, xobject);
            xobject.setId(pObject.getDbId());
        } catch (final ModelException e) {
            log.error("CreatePO: " + e.getMessage());
            throw new BusinessException(e);
        }
        log.debug("UpdatePO: " + pObject + " is updated by " + xobject);

        return pObject;
    }

    /**
     * Addition to updatePO: update other related pims object
     * 
     * @param object
     * @param xobject
     * @throws ModelException
     */
    protected abstract void updatePORelated(P pobject, X xobject) throws ModelException;

    /**
     * @see org.pimslims.crystallization.dao.XPDAOInterface#getPO(org.pimslims.business.XtalObject)
     */
    @SuppressWarnings("unchecked")
    public P getPO(final X xobject) {
        if (xobject == null) {
            return null;
        }
        //find by id
        //IMB Added cast (although its unchecked) as my install was
        //complaining that this was not a valid statement
        //discussion: http://forum.java.sun.com/thread.jspa?messageID=4196171
        P pObject = null;
        if (xobject.getId() != null && xobject.getId() > 0) {
            pObject = version.get(getPClass(), xobject.getId());
        }
        if (pObject == null) {
            // find by key attributes
            pObject = version.findFirst(getPClass(), getKeyAttributes(xobject));

        }
        log.debug("GetPO: " + pObject + " is found by " + xobject);

        return pObject;
    }

    /**
     * @see org.pimslims.crystallization.dao.XPDAOInterface#findOrCreate(org.pimslims.business.XtalObject)
     */
    public P findOrCreate(final X xobject) throws BusinessException {
        if (xobject == null) {
            return null;
        }
        P pobject = getPO(xobject);
        if (pobject != null) {
            log.debug("findOrCreate: " + pobject + " is found by " + xobject);
        } else {
            pobject = createPO(xobject);
            log.debug("findOrCreate: " + pobject + " is created by " + xobject);
        }
        return pobject;
    }

    protected abstract Class<P> getPClass();//this one is redundant with P, but we have to

    /**
     * get key properties,include key arrtibutes and key roles, of a XO to identify a PO
     * 
     * these key properties should be enought to create a PO.
     * 
     * @param xobject
     * @return attribute map
     */
    protected abstract Map<String, Object> getKeyAttributes(X xobject);

    /**
     * the full attributes to create a PO
     * 
     * @param xobject
     * @return attribute map
     * @throws BusinessException
     */
    protected abstract Map<String, Object> getFullAttributes(X xobject) throws BusinessException;

    /**
     * @return Returns writable version.
     */
    protected WritableVersion getWritableVersion() {
        return (WritableVersion) version;
    }

    <G extends ModelObject> G findByName(final Class<G> pimsClass, final String name) {
        return version.findFirst(pimsClass, org.pimslims.model.people.Organisation.PROP_NAME, name);
    }
}
