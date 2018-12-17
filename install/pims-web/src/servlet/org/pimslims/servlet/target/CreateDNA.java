/*
 * Created on 25-October-2007 @author: Peter Troshin
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.servlet.PIMSServlet;

public class CreateDNA extends PIMSServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "Create DNA Molecule for a target";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final java.io.PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        String targetHook = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            targetHook = pathInfo.substring(1);
        }

        if (!Util.isHookValid(targetHook)) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Target for DNA is not specified or is incorrect request " + "Target specified: "
                + targetHook);
            return;
        }

        WritableVersion rw = null;
        try {
            rw = this.getWritableVersion(request, response);
            if (rw == null) {
                return;
            }
            final Target target = rw.get(targetHook);

            final HashMap<String, Object> prop = new HashMap<String, Object>();
            prop.put(Molecule.PROP_MOLTYPE, "DNA");
            prop.put(Substance.PROP_NAME, target.getName() + "_seq");
            prop.put(Molecule.PROP_SEQUENCE, " ");
            prop.put(Molecule.PROP_DNA_FOR_TARGETS, Util.makeCollection(target));

            rw.create(org.pimslims.model.molecule.Molecule.class, prop);
            rw.commit();
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
        PIMSServlet.redirectPostToReferer(request, response, request.getParameterMap());
    }
}
