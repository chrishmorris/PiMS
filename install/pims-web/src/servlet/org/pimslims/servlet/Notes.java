package org.pimslims.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.Note;

/**
 * Add a note to a LabBookEntry
 * 
 * @author cm65
 * 
 */
public class Notes extends PIMSServlet {

    /**
     * Id for notes box
     */
    public static final String NOTES = "notes";

    /**
     * Parameter name for adding a new note
     */
    public static final String ADD_NOTE = "_addNote";

    public Notes() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Add a note";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // a user may end up here if their browser does not support the referer header
        // TODO send a page that does a javascript back
        throw new ServletException(this.getClass().getName() + " does not support GET");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        try {
            final HttpSession session = request.getSession();
            Notes.processRequest(version, parms, session);
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response, Notes.NOTES);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            this.log("You aren't allowed to do that edit. You were on page: "
                + PIMSServlet.getReferer(request));
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * ${hook}:${fieldName}
     */
    public static final Pattern KEY = Pattern.compile("^([a-zA-Z.]+:\\d+):(\\w+)$");

    public static void processRequest(final WritableVersion version, final Map<String, String[]> parms,
        final HttpSession session) throws ServletException, AccessException, ConstraintException {

        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();
            if (key.startsWith("_")) {
                continue;
            }
            String value = null; // can use this method to set to null
            final String[] values = parms.get(key);
            if (1 == values.length && !"".equals(values[0])) {
                value = values[0];
            }
            if (1 < values.length) {
                throw new ServletException("Too many values for: " + key);
            }
            final Matcher m = Notes.KEY.matcher(key);
            if (!m.matches()) {
                throw new ServletException("Invalid parameter name: " + key);
            }
            final String hook = m.group(1);
            final String name = m.group(2);
            final ModelObject object = version.get(hook);

            if (null == object) {
                throw new ServletException("Lab book entry not found: " + hook);
            }

            assert Notes.ADD_NOTE.equals(name);
            new Note(version, (LabBookEntry) object).setDetails(value);
        }
    }

}
