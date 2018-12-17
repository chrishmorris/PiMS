/**
 * V3_2-web org.pimslims.servlet.standard ViewProject.java
 * 
 * @author cm65
 * @date 8 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.standard;

import org.pimslims.servlet.expert.View;

/**
 * ViewProject
 * 
 */
@Deprecated
// does nothing, and never called
public class ViewProject extends View {

    /**
     * ViewProject.doDoGet
     * 
     * @see org.pimslims.servlet.expert.View#doDoGet(org.pimslims.dao.ReadableVersion,
     *      javax.servlet.http.HttpServletRequest)
     */
    /*
    @Override
    protected void doDoGet(final ReadableVersion version, final ModelObject object,
        final HttpServletRequest request) {
        final UserGroup userGroup =
            version.findFirst(UserGroup.class, UserGroup.PROP_NAME, object.get_Name());
        if (null != userGroup) {
            final ModelObjectBean bean = BeanFactory.newBean(userGroup);
            request.setAttribute("userGroup", bean);
        }
    }
    */

}
