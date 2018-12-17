/**
 * pims-web org.pimslims.servlet.standard Move.java
 * 
 * @author Marc Savitsky
 * @date 31 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */

package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * Move a containable object into a container
 */
public class Move extends PIMSServlet {

    /**
     * DESTINATION String
     */
    private static final String DESTINATION = "destination";

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */

    private static String ACTION_REMOVE = "remove";

    @Override
    public String getServletInfo() {
        return "manage sample holders";
    }

    /**
     * MoveHolder.doGet it doesn't allow you to move a holder into a location - it doesn't seem to make a
     * request that actually moves the holder
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     * @Override
     * @Deprecated // obsolete, seems to be broken, see above protected void doGet(final HttpServletRequest
     *             request, final HttpServletResponse response) throws ServletException, IOException {
     * 
     *             //final Long start = System.currentTimeMillis();
     * 
     *             // ensure that PIMS was able to connect to the database if (!this.checkStarted(request,
     *             response)) { return; }
     * 
     *             String pathInfo = request.getPathInfo(); if (null == pathInfo || 0 == pathInfo.length()) {
     *             this.writeErrorHead(request, response, "Object must be specified",
     *             HttpServletResponse.SC_BAD_REQUEST); return; } pathInfo = pathInfo.substring(1); // strip
     *             initial slash
     * 
     *             // get a read transaction final ReadableVersion version = this.getReadableVersion(request,
     *             response); try { final ModelObject object = version.get(pathInfo); if (null == object) {
     *             this.writeErrorHead(request, response, "Object not found: " + pathInfo,
     *             HttpServletResponse.SC_NOT_FOUND); return; }
     * 
     *             ModelObject parent = null; if (null != request.getParameter("parentHook")) { parent =
     *             version.get(request.getParameter("parentHook")); }
     * 
     *             final Collection<Holder> holders = version.findAll(Holder.class,
     *             AbstractHolder.PROP_SUPHOLDER, parent);
     * 
     *             final List<ModelObjectBean> beans = new ArrayList<ModelObjectBean>(); for (final Holder
     *             holder : holders) { if (!HolderFactory.isPlate(holder.getHolderType())) {
     *             beans.add(BeanFactory.newBean(holder)); } }
     * 
     *             if ("true".equals(request.getParameter("isAJAX"))) { // just send list of holders
     *             response.setContentType("text/xml"); final XMLOutputter xo = new XMLOutputter();
     *             xo.output(this.makeAjaxListXML(beans), response.getWriter()); return; }
     * 
     *             Collections.sort(beans); request.setAttribute("results", beans);
     *             request.setAttribute("currentobject", Move.getCurrentHolder(object));
     *             request.setAttribute("hostobject", new ModelObjectBean(object));
     *             request.setAttribute("locationTrail",
     *             SampleLocationBean.getCurrentLocationTrail((Containable) object));
     * 
     *             version.commit(); final RequestDispatcher rd =
     *             request.getRequestDispatcher("/JSP/holder/MoveObject.jsp"); rd.forward(request, response);
     * 
     *             } catch (final AbortedException e) { throw new ServletException(e); } catch (final
     *             ConstraintException e) { throw new ServletException(e); } finally { if
     *             (!version.isCompleted()) { version.abort(); } }
     * 
     *             }
     */

    /**
     * MoveHolder.doPost Used successfully from the view of a holder, to move in new contents.
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Object to move must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final Containable object =
                (Containable) this.getRequiredObject(version, request, response, pathInfo);
            if (null == object) {
                return; // 404 already sent
            }

            final String action = request.getParameter("action");

            Container destination = null;
            Integer rowPosition = null;
            Integer colPosition = null;
            Integer subPosition = null;
            if (null != request.getParameter(Move.DESTINATION)) {
                destination = (Container) version.get(request.getParameter(Move.DESTINATION));
            }

            if (Move.ACTION_REMOVE.equals(action) && null == destination) {
                destination = (Holder) Move.getParent(object); //why?
            }

            final String row = request.getParameter("rowPosition");
            if (null != row && !"undefined".equals(row) && !"".equals(row)) {
                final int x = new Integer(row);
                if (x > 0) {
                    rowPosition = new Integer(x);
                }
            }
            final String col = request.getParameter("colPosition");
            if (null != col && !"undefined".equals(col) && !"".equals(col)) {
                final int x = new Integer(col);
                if (x > 0) {
                    colPosition = new Integer(x);
                }
            }
            final String sub = request.getParameter("subPosition");
            if (null != sub && !"undefined".equals(sub) && !"".equals(sub)) {
                final int x = new Integer(sub);
                if (x > 0) {
                    subPosition = new Integer(x);
                }
            }

            if (Move.ACTION_REMOVE.equals(action)) {
                ContainerUtility.move(object, null, null, null, null);
            } else {
                ContainerUtility.move(object, destination, rowPosition, colPosition, subPosition);
            }

            version.commit();

            String anchor = "";
            if (null != destination) {
                anchor = destination.get_Hook();
            }
            PIMSServlet.redirectPostToReferer(request, response, anchor);

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    private Document makeAjaxListXML(final Collection<ModelObjectBean> results) {

        final Element rootElement = new Element("list");
        for (final ModelObjectBean bean : results) {
            final Element elem = new Element("object");
            // protocol name and hook in xml
            elem.setAttribute("name", bean.getName());
            elem.setAttribute("hook", bean.getHook());

            rootElement.addContent(elem);
        }
        final Document xml = new Document(rootElement);
        return xml;
    }

    public static ModelObjectBean getCurrentHolder(final ModelObject object) {

        if (object instanceof Holder) {
            final Holder current = (Holder) object;
            return BeanFactory.newBean(current.getParentHolder());
        }
        return null;
    }

    private static ModelObject getParent(final Containable object) {

        if (object instanceof Holder) {
            final Holder current = (Holder) object;
            if (null == current.getParentHolder()) {
                return current;
            }
            return current.getParentHolder();
        }

        if (object instanceof Sample) {
            final Sample current = (Sample) object;
            return current.getHolder();
        }
        return null;
    }

}
