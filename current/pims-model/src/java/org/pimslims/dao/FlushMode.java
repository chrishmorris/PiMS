/**
 * xtalPiMS-model org.pimslims.dao FlushMode.java
 * 
 * @author bl67
 * @date 7 Apr 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import java.util.Map;

import javax.persistence.FlushModeType;

import org.pimslims.metamodel.AbstractModelObject;

/**
 * FlushMode
 * 
 */
public class FlushMode {

    /**
     * flush hibernate Session after create a PiMS object, When =false, performance is be better, but error
     * value will be detected at commit/flush.
     * 
     */
    private boolean flushSessionAfterCreate;

    /**
     * Controls whether inverse roles are automatically updated
     */
    private boolean autoUpdateInverseRoles;

    /**
     * when checkValue=false, set_property/get_propery will not check constraint and access right. is this
     * used?
     */
    private boolean checkValue;

    /**
     * hibernate session flush mode
     */

    private org.hibernate.FlushMode hibernateFlushMode;

    /**
     * Constructor for FlushMode
     * 
     * @param autoUpdateInverseRoles
     * @param checkValue
     * @param flushSessionAfterCreate
     * @param hibernateFlushMode
     */
    private FlushMode() {
        super();
    }

    /**
     * default mode is slow but safe FlushMode.defaultMode
     * 
     * @return
     */
    static public FlushMode defaultMode() {
        FlushMode mode = new FlushMode();
        mode.setAutoUpdateInverseRoles(true);
        mode.setCheckValue(true);
        mode.setFlushSessionAfterCreate(true);
        mode.setHbFlushMode(org.hibernate.FlushMode.AUTO);
        return mode;
    }

    /**
     * faster than default mode as it does not flush/save object until version.commit or version.flush
     * FlushMode.fastMode
     * 
     * @return
     */
    static public FlushMode fastMode() {
        FlushMode mode = new FlushMode();
        mode.setAutoUpdateInverseRoles(true);
        mode.setCheckValue(true);
        mode.setFlushSessionAfterCreate(false);
        mode.setHbFlushMode(org.hibernate.FlushMode.COMMIT);
        return mode;
    }

    /**
     * fastest model as it does not check the value try to save and no flush/save unless
     * version.commit()/flush() called
     * 
     * @WARNING "invalid" value may save to database. Suggest only use with bulk insert/update and know the
     *          values are valid. FlushMode.fastMode
     * @return
     */
    static public FlushMode batchMode() {
        FlushMode mode = new FlushMode();
        mode.setAutoUpdateInverseRoles(true);
        mode.setCheckValue(true);
        mode.setFlushSessionAfterCreate(false);
        mode.setHbFlushMode(org.hibernate.FlushMode.COMMIT);
        return mode;
    }

    /**
     * @return Returns the flushSessionAfterCreate.
     */
    public boolean isFlushSessionAfterCreate() {
        return flushSessionAfterCreate;
    }

    /**
     * <p>
     * If flushSessionAfterCreate is true then any newly instantiated AbstractModelObject will be flushed to
     * the data store within the constructor. This, whilst convenient, is likely to have an adverse impact on
     * performance. Significant performance benefits can be obtained by setting this parameter to false if you
     * have to do several inserts/updates. Note that setting {@link setFlushMode(FlushMode)} to
     * {@link FlushMode#MANUAL} does not stop this flush from happening. The default value is true.
     * </p>
     * 
     * @return true if this WritableVersionImpl is set to flush the session after the creation of a new mapped
     *         object, otherwise false
     * @see AbstractModelObject#flush()
     * @see AbstractModelObject#init(Map)
     */
    public void setFlushSessionAfterCreate(boolean flushSessionAfterCreate) {
        this.flushSessionAfterCreate = flushSessionAfterCreate;
    }

    /**
     * @return Returns the autoUpdateInverseRoles.
     */
    public boolean isAutoUpdateInverseRoles() {
        return autoUpdateInverseRoles;
    }

    /**
     * @param autoUpdateInverseRoles The autoUpdateInverseRoles to set.
     */
    public void setAutoUpdateInverseRoles(boolean autoUpdateInverseRoles) {
        this.autoUpdateInverseRoles = autoUpdateInverseRoles;
    }

    /**
     * @param checkValue The checkValue to set.
     */
    public void setCheckValue(boolean checkValue) {
        this.checkValue = checkValue;
    }

    /**
     * @return Returns the checkValue.
     */
    public boolean isCheckValue() {
        return checkValue;
    }

    /**
     * @return Returns the hibernateFlushMode.
     */
    @Deprecated
    public org.hibernate.FlushMode getHibernateFlushMode() {
        return hibernateFlushMode;
    }

    /**
     * @param hibernateFlushMode The hibernateFlushMode to set.
     */
    private void setHbFlushMode(org.hibernate.FlushMode hibernateFlushMode) {
        this.hibernateFlushMode = hibernateFlushMode;
    }

    /**
     * @param hibernateFlushMode The hibernateFlushMode to set.
     */
    @Deprecated
    public void setHibernateFlushMode(org.hibernate.FlushMode hibernateFlushMode) {
        this.hibernateFlushMode = hibernateFlushMode;
    }

    /**
     * FlushMode.clone
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected FlushMode clone() throws CloneNotSupportedException {
        FlushMode newMode = new FlushMode();
        newMode.autoUpdateInverseRoles = this.autoUpdateInverseRoles;
        newMode.checkValue = this.checkValue;
        newMode.flushSessionAfterCreate = this.flushSessionAfterCreate;
        newMode.hibernateFlushMode = this.hibernateFlushMode;
        return newMode;
    }

    /**
     * FlushMode.getJPAFlushMode
     * 
     * @return
     */
    public FlushModeType getJPAFlushMode() {
        if (org.hibernate.FlushMode.COMMIT == this.hibernateFlushMode) {
            return FlushModeType.COMMIT;
        }
        ;
        return FlushModeType.AUTO;
    }
}
