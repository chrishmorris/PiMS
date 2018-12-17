/**
 * V4_3-web org.pimslims.servlet.holder CreateContainer.java
 * 
 * @author cm65
 * @date 11 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.holder;

import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.Create;

/**
 * CreateContainer
 * 
 */
public class CreateContainer extends Create {

    /**
     * CreateContainer.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create a Container (Holder or Location)";
    }

    @Override
    protected void addSpecialObjects(final HttpServletRequest request, final ReadableVersion rv)
        throws ServletException {
        final Collection<HolderType> holderTypes = rv.getAll(HolderType.class, 0, 200);
        request.setAttribute("holderTypes", ModelObjectShortBean.getBeans(holderTypes));
        super.addSpecialObjects(request, rv);
    }

    @Override
    protected String getMetaClassName(final HttpServletRequest request) throws ServletException {
        return Holder.class.getName();
    }

}
