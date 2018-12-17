/*
 * Created on 01.09.2005 - Code Style - Code Templates
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.servlet.utils.ProgressListener;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Petr Troshin
 */
@Deprecated
//  obsolete
public class TargetConstructReport extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Prepare the list of target and html header, then forward to the presentation page";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }

        Collection<ResearchObjectiveElement> results = null;
        final PrintWriter writer = response.getWriter();
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            final MetaClass searchType =
                this.getModel().getMetaClass(
                    org.pimslims.model.target.ResearchObjectiveElement.class.getName());
            // add roles needed in TargetBean into joinlist
            final java.util.List<String> rolesToJoin = new ArrayList<String>();
            rolesToJoin.add(ResearchObjectiveElement.PROP_RESEARCHOBJECTIVE);
            rolesToJoin.add(ResearchObjectiveElement.PROP_MOLECULE);
            rolesToJoin.add(ResearchObjectiveElement.PROP_TARGET);
            rolesToJoin.add(ResearchObjectiveElement.PROP_TRIALMOLECULES);

            results = this.getAll(rv, searchType, rolesToJoin);
            if (results.size() == 0) {
                this.writeHead(request, response, "Browse Target with Constructs");
                writer.print(" No Targets are recorded yet.<br />"
                    + "<a href='Create/org.pimslims.model.target.Target'>Create a target</a>"

                );
                PIMSServlet.writeFoot(writer, request);
                return;
            }
            // now show the list

            // Drop Blueprintcomponent without target of expblueprints
            results = this.filterResults(results);
            this.writeHead(request, response, "Browse Target with constructs");

            final ProgressListener progress =
                new ProgressListener(results.size(), writer, "Formatting target list...");

            //NO, could overflow memory
            request.setAttribute("beans", TargetConstructBean.getAll(results, progress));
            request.setAttribute("pagerSize", "20");
            request.setAttribute("includeHeader", new Boolean(false));

            if (progress != null) {
                progress.setProgressHidden(true);
            }

            final ServletContext sCont = this.getServletContext();
            final RequestDispatcher dispatcher =
                sCont.getRequestDispatcher("/JSP/browseTargetConstructs.jsp");
            dispatcher.forward(request, response);
            PIMSServlet.writeFoot(writer, request);
            rv.commit();
        } catch (final AbortedException e1) {
            this.writeHead(request, response, "Browse Target with Constructs");
            writer.print(" Sorry, there has been a problem, please try again.");
            PIMSServlet.writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

    List<ResearchObjectiveElement> filterResults(final Collection<ResearchObjectiveElement> results) {
        if (results == null) {
            return Collections.EMPTY_LIST;
        }
        final List<ResearchObjectiveElement> filteredList =
            new ArrayList<ResearchObjectiveElement>(results.size());
        for (final ResearchObjectiveElement bc : results) {
            if (bc.getTarget() == null) {
                continue;
            }
            if (bc.getResearchObjective() == null) {
                continue;
            }
            filteredList.add(bc);
        }
        return filteredList;
    }

} // servlet end
