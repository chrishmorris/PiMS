/**
 * current-pims-web org.pimslims.lab.experiment ExtensionBean.java
 * 
 * @author susy
 * @date 05-Jul-2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 susy
 * 
 * 
 * 
 */
package org.pimslims.presentation.construct;

import java.io.Serializable;

import org.pimslims.model.molecule.Extension;
import org.pimslims.presentation.ModelObjectBean;

/**
 * ExtensionBean used to make list of Extensions for Primers
 * 
 */
public class ExtensionBean extends ModelObjectBean implements Serializable {

    private String exName;

    private String exSeq;

    private String direction;

    private String encodedTag;

    private String restrictionSite;

    // Hook to the corresponding PIMS object    
    private String extensionHook;

    @Override
    public int compareTo(final Object obj) {

        if (!(obj instanceof ExtensionBean)) {
            throw new ClassCastException("obj1 is not an ExtensionBean! ");
        }
        return 0;
    }

    /**
     * Constructor for ExtensionBean
     */
    public ExtensionBean(final Extension extension) {
        super(extension);
        assert null != extension;
    }

    /**
     * Constructor for ExtensionBean
     */
    public ExtensionBean() {
        super();
        // 
    }

    /**
     * @return Returns the exName.
     */
    public String getExName() {
        return this.exName;
    }

    /**
     * @param exName The exName to set.
     */
    public void setExName(final String exName) {
        this.exName = exName;
    }

    /**
     * @return Returns the exSeq.
     */
    public String getExSeq() {
        return this.exSeq;
    }

    /**
     * @param exSeq The exSeq to set.
     */
    public void setExSeq(final String exSeq) {
        this.exSeq = exSeq;
    }

    /**
     * @return Returns the direction.
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * @param direction The direction to set.
     */
    public void setDirection(final String direction) {
        this.direction = direction;
    }

    /**
     * @return Returns the encodedTag.
     */
    public String getEncodedTag() {
        return this.encodedTag;
    }

    /**
     * @param encodedTag The encodedTag to set.
     */
    public void setEncodedTag(final String encodedTag) {
        this.encodedTag = encodedTag;
    }

    /**
     * @return Returns the restrictionSite.
     */
    public String getRestrictionSite() {
        return this.restrictionSite;
    }

    /**
     * @param restrictionSite The restrictionSite to set.
     */
    public void setRestrictionSite(final String restrictionSite) {
        this.restrictionSite = restrictionSite;
    }

    /**
     * @return Returns the extensionHook.
     */
    public String getExtensionHook() {
        return this.extensionHook;
    }

    /**
     * @param extensionHook The extensionHook to set.
     */
    public void setExtensionHook(final String extensionHook) {
        this.extensionHook = extensionHook;
    }

}
