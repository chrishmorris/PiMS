package org.pimslims.servlet.expert;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * Show a generic view of a PiMS record. For important classes, there are custom views, in other servlets.
 * 
 * TODO ensure that all custom views subclass this.
 * 
 * @author Petr Troshin
 * @date December 2005
 * 
 */
public class View extends PIMSServlet {

    public View() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        final PrintWriter writer = response.getWriter();

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            if (pathInfo != null && pathInfo.trim().length() > 0) {
                // OK
            } else {
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print("Hook is not provided");
                return;
            }
            final String hook = pathInfo.substring(1);

            final ModelObject mObj = this.getRequiredObject(rv, request, response, hook);
            if (null == mObj) {
                return; // an error page has been shown
            }

            // Construct a link to create a add new modelObject in role to
            // displayed object
            //final HashMap addnewLinks = new HashMap();
            //final Map<String, String> searchLinks = new HashMap();
/*
            final Map metaRoles = mObj.get_MetaClass().getMetaRoles();
            for (final Iterator iter = metaRoles.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry element = (Map.Entry) iter.next();
                final MetaRole mrole = (MetaRole) element.getValue();

                final MetaClass otherMetaClass = mrole.getOtherMetaClass();
                final MetaRole otherRole = mrole.getOtherRole();
                //final String roleName = mrole.getRoleName();
                if (mrole.getHigh() == -1) {

                    // make Search/Add link, as long as can add to reverse role
                    if (otherRole == null || otherRole.getLow() != otherRole.getHigh()) {
                        final String searchAddLink =
                            "../EditRole/" + mObj.get_Hook() + "/" + mrole.getRoleName();

                        //searchLinks.put(roleName, searchAddLink);
                    }
                    if (otherRole != null) { // cant "create for" if no back link
                        final String createLink =
                            "../Create/" + otherMetaClass.getJavaClass().getName() + "?"
                                + otherRole.getRoleName() + "=" + mObj.get_Hook();
                        //addnewLinks.put(roleName, createLink);
                    }
                }

            } */

            //request.setAttribute("createLinks", addnewLinks);  
            //request.setAttribute("chooseLinks", searchLinks);  

            // is in format (roleName-HashMap(attributeName - value))
            // Set mandatory attributes
            //was request.setAttribute("modelObject", mObjView);
            request.setAttribute("head", new Boolean(true));
            //was request.setAttribute("allHooks", mObjView.getAllHooks());
            final ModelObjectBean bean = BeanFactory.newBean(mObj);
            request.setAttribute("bean", bean);
            request.setAttribute("ObjName", bean.getClassDisplayName()); //TODO Make this obsolete
            request.setAttribute("mayUpdate",
                new Boolean(true && bean.getMayUpdate()));
            response.setStatus(HttpServletResponse.SC_OK);

            // call any special processing by subclass
            this.doDoGet(rv, mObj, request);

            // Write standard header
            PIMSServlet.dispatchCustomJsp(request, response, mObj.get_MetaClass().getMetaClassName(), "view",
                this.getServletContext());
            rv.commit();
        } catch (final AbortedException e1) {
            this.log("list aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

    /**
     * View.doDoGet
     * 
     * @param rv
     * @param request
     */
    protected void doDoGet(final ReadableVersion rv, final ModelObject object,
        final HttpServletRequest request) {
        // override this to add functionality, e.g. return additional beans
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "View PiMS record";
    }

    public static boolean canFindAll(final MetaRole otherMetaRole) {
        return null == otherMetaRole || otherMetaRole.getHigh() > otherMetaRole.getLow()
            || -1 == otherMetaRole.getHigh() // means infinity
        ;
    }

}
