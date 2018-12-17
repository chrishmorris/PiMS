/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;

/**
 * @author cm65
 * 
 */
@Deprecated
// obsolete, not in use
public class AjaxCreate extends Create {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("This servlet does not support GET");
    }

    @Override
    public String getServletInfo() {
        return "Create a PiMS record and send an XML reply";
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);
        final java.io.PrintWriter writer = response.getWriter();

        final String metaClassName = request.getPathInfo().substring(1);

        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        if (null == metaClass) {
            // TODO provide AJAX error reporter
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Unknown type: " + metaClassName);
            return;
        }

        final MetaClass pmeta = metaClass;

        final java.util.Map parms = request.getParameterMap();

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        try {
            final Map errorMessages = Collections.synchronizedMap(new HashMap());
            final Map params = Create.parseValues(rw, parms, pmeta, errorMessages);

            if (errorMessages.size() != 0) {
                final HttpSession session = request.getSession();
                session.setAttribute("errorMessages", errorMessages);
                session.setAttribute("formValues", new HashMap(parms));
                // TODO send AJAX error response
                rw.abort();
                return;
            }

            final ModelObject mObj = rw.create(metaClass.getJavaClass(), params);
            rw.commit();
            AjaxCreate.sendXMLResponse(request, response, mObj);

        } catch (final AccessException aex) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            writer.print("You are not allowed to make these changes");
            // TODO send AJAX response
            return;
        } catch (final ConstraintException cex) {
            rw.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            // TODO send AJAX response
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }

        }

    }

    public static void sendXMLResponse(final HttpServletRequest request, final HttpServletResponse response,
        final ModelObject createdObject) throws IOException {
        final Element rootElement = new Element("saved");
        final Element elem = new Element("createdObject");
        elem.setAttribute("hook", createdObject.get_Hook());
        rootElement.addContent(elem);

        // TODO for all roles, for all associates, add an <object> element

        final Document xml = new Document(rootElement);
        response.setContentType("text/xml");
        final XMLOutputter xo = new XMLOutputter();
        xo.output(xml, response.getWriter());
    }

}
