/**
 * 
 */
package org.pimslims.presentation.mru;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * MRU is most recent used modelObject by a user
 * 
 * @author bl67
 * 
 */
public class MRU extends ModelObjectShortBean {
    private String userName;

    // menu

    static int maxLengthofObjectShortDisplayName = 25;

    /**
     * use this contructor when the object already in a readable/writableVersion
     */
    public MRU(final String userName, final ModelObject modelObject) {
        super(modelObject);
        this.setUserName(userName);
    }

    public String getUserName() {
        return this.userName;
    }

    private void setUserName(final String userName) {
        this.userName = userName;
    }

}
