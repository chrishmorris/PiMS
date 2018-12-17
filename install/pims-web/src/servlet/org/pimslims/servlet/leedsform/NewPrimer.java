package org.pimslims.servlet.leedsform;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.leeds.LeedsFormServletUtils;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.Person;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanWriter;
import org.pimslims.presentation.leeds.PrimerForm;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * 
 * @author Peter Troshin
 * @date May 2008
 * 
 */
@Deprecated
//Leeds functionality is obsolete
public class NewPrimer extends PIMSServlet implements FormFieldsNames {

    @Override
    public String getServletInfo() {
        return "Create a new Primer ";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/Leedsforms/NewSinglePrimer.jsp");

        ReadableVersion rv = null;
        try {
            rv = this.getReadableVersion(request, response);
            if (rv == null) {
                return;
            }
            NewPrimer.setDefaultCreator(rv, request, PIMSServlet.getCurrentUser(rv, request));
            request.setAttribute("rb", PrimerForm.fprimerProp);
            request.setAttribute("primer", new PrimerBean(true));
            rd.forward(request, response);
            rv.commit();
        } catch (final AbortedException e) {
            e.printStackTrace();
        } catch (final ConstraintException e) {
            e.printStackTrace();
        } finally {
            if (rv != null) {
                if (!rv.isCompleted()) {
                    rv.abort();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Record new values

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        Sample sample = null;

        try {

            final PrimerBean upprimer =
                (PrimerBean) LeedsFormServletUtils.updateProperties(PrimerBean.class, request,
                    PrimerForm.fprimerProp);
            // Assume that all single primers are direct
            upprimer.setDirection(true);
            PrimerBeanWriter.writePrimer(rw, upprimer, null);
            PrimerBeanWriter.update(rw, upprimer);
            sample = upprimer.getSample();
            rw.commit();
        } catch (final InstantiationException e) {
            throw new ServletException(e);
        } catch (final IllegalAccessException e) {
            throw new ServletException(e);
        } catch (final ClassNotFoundException e) {
            throw new ServletException(e);
        } catch (final IllegalArgumentException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            rw.abort();
            System.out.println("Constrain EX in servlet in commit");
            request.setAttribute("javax.servlet.error.exception", e);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final SecurityException e) {
            throw new ServletException(e);
        } catch (final InvocationTargetException e) {
            throw new ServletException(e);
        } finally {
            if (rw != null) {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
            }

        }

        this.redirect(response, request.getContextPath() + "/ViewPrimer/" + sample.get_Hook());
    }

    public static void setDefaultCreator(final ReadableVersion rv, final HttpServletRequest request,
        final User user) {
        Person person = null;
        if (user != null) {
            person = user.getPerson();
        }
        if (person != null) {
            final String creator = person.getGivenName() + " " + person.getFamilyName();
            request.setAttribute("creator", creator);
        }
    }

}
