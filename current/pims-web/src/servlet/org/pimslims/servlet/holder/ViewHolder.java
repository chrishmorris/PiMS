/**
 * 
 */
package org.pimslims.servlet.holder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.holder.Holder;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.sample.HolderContents;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ViewHolder extends PIMSServlet {

    private static final String HOLDER_FORWARD_URL = "/JSP/holder/ViewHolder.jsp";

    /**
     * 
     */
    public ViewHolder() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of an holder";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("ViewHolder.doGet");
        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Holder holder = (Holder) version.get(pathInfo); // e.g.
            if (null == holder) {
                this.writeErrorHead(request, response, "Experiment not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Construct a link to create a add new modelObject in role to
            // displayed object
            final HashMap addnewLinks = new HashMap();
            final Map<String, String> searchLinks = new HashMap();

            // TODO move this logic to ModelObjectView
            final Map metaRoles = holder.get_MetaClass().getMetaRoles();
            for (final Iterator iter = metaRoles.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry element = (Map.Entry) iter.next();
                final MetaRole mrole = (MetaRole) element.getValue();

                final MetaClass otherMetaClass = mrole.getOtherMetaClass();
                final MetaRole otherRole = mrole.getOtherRole();
                final String roleName = mrole.getRoleName();
                if (mrole.getHigh() == -1) {

                    // make Search/Add link, as long as can add to reverse role
                    if (otherRole == null || otherRole.getLow() != otherRole.getHigh()) {
                        String searchAddLink = "";
                        searchAddLink = "../EditRole/" + holder.get_Hook() + "/" + mrole.getRoleName();
                        searchLinks.put(roleName, searchAddLink);
                    }
                    if (otherRole != null) { // cant "create for" if no back link
                        final String createLink =
                            "../Create/" + otherMetaClass.getJavaClass().getName() + "?"
                                + otherRole.getRoleName() + "=" + holder.get_Hook();
                        addnewLinks.put(roleName, createLink);
                    }
                }
            }
            request.setAttribute("locationTrail", SampleLocationBean.getCurrentLocationTrail(holder));
            request.setAttribute("createLinks", addnewLinks);
            request.setAttribute("chooseLinks", searchLinks);

            request.setAttribute("head", new Boolean(true));
            final ModelObjectBean bean = BeanFactory.newBean(holder);
            request.setAttribute("bean", bean);
            request.setAttribute("ObjName", bean.getClassDisplayName());
            request.setAttribute("mayUpdate",
                new Boolean(true && bean.getMayUpdate()));
            response.setStatus(HttpServletResponse.SC_OK);

            final int dimension = HolderContents.getDimensions(holder);
            System.out.println("ViewHolder.doGet dimension [" + dimension + "]");

            if (null != holder.getHolderType()) {
                request.setAttribute("holderType", BeanFactory.newBean(holder.getHolderType()));
            }
            request.setAttribute("subHolders",
                ModelObjectShortBean.getModelObjectShortBeans(holder.getSubHolders()));

            System.out.println("ViewHolder.doGet forward [" + ViewHolder.HOLDER_FORWARD_URL + "]");
            final RequestDispatcher dispatcher = request.getRequestDispatcher(ViewHolder.HOLDER_FORWARD_URL);
            dispatcher.forward(request, response);
            version.commit();

        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }

    }

}
