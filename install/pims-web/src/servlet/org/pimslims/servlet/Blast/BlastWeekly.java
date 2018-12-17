package org.pimslims.servlet.Blast;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.BioinfUtility;
import org.pimslims.bioinf.TopHitBean;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.bioinf.BlastMultipleUtility;
import org.pimslims.presentation.bioinf.TopHitBeanUtility;
import org.pimslims.servlet.PIMSServlet;

/**
 * Retrieve the 'Top Hit' DbRefs for all targets Create TopHit beans and forward to WeeklyBlastReport.jsp
 * 
 * @author Susy Griffiths YSBL
 * @date October 2007
 * 
 */
public class BlastWeekly extends PIMSServlet {

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
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

        ReadableVersion version = null;

        try {
            version = this.getReadableVersion(request, response);
            if (version == null) {
                return;
            }

            //Get all Targets iterate and make topHitBeans
            final Map coll = Collections.EMPTY_MAP;
            final List<Target> allTargets = (List) version.findAll(Target.class, coll);

            //Get date of latest search = topHitDbRef with latest date and pass to JSP
            //Get all topHit DbRefs
            final List<ExternalDbLink> allDbRefs = (List) version.findAll(ExternalDbLink.class, coll);
            final List<ExternalDbLink> topHitList = BlastMultipleUtility.getTopHits(allDbRefs);

            //Find PDB and TDB DbRefstopHits
            final List<ExternalDbLink> pDBHits = TopHitBeanUtility.findRefsForDbName(topHitList, "pdb");
            final int numPDBDbRefs = pDBHits.size();
            final List<ExternalDbLink> tDBHits = TopHitBeanUtility.findRefsForDbName(topHitList, "targetdb");
            final int numTDBDbRefs = tDBHits.size();

            if (!allTargets.isEmpty()) {
                //Get the date of the latest Blast searches and pass to JSP as PIMS date

                final List<TopHitBean> topHitBeans = new java.util.ArrayList();

                for (final Target targ : allTargets) {
                    if (!PIMSTarget.isDNATarget(targ)) {
                        TopHitBean thb;
                        try {
                            thb = TopHitBeanUtility.makeTopHitBean(targ);
                        } catch (final AccessException e) {
                            throw new RuntimeException(e);
                        } catch (final ConstraintException e) {
                            throw new RuntimeException(e);
                        } catch (final ParseException e) {
                            throw new RuntimeException(e);
                        }
                        topHitBeans.add(thb);
                        //set up progrss bar here??
                    }
                }
                final String numTargets = Integer.toString(allTargets.size());
                // succeeded
                final RequestDispatcher rd =
                    request.getRequestDispatcher("/JSP/bioinf/WeeklyBlastReport.jsp");
                request.setAttribute("topHitBeans", topHitBeans);
                request.setAttribute("numTargets", numTargets);

                if (numPDBDbRefs > 0) {
                    final Date latestPDBSearchDate = TopHitBeanUtility.getLatestDate(pDBHits);
                    final String datePDBSearch = BioinfUtility.getDate(latestPDBSearchDate);
                    request.setAttribute("datePDBSearch", datePDBSearch);
                }
                if (numTDBDbRefs > 0) {
                    final Date latestTDBSearchDate = TopHitBeanUtility.getLatestDate(tDBHits);
                    final String dateTDBSearch = org.pimslims.lab.Utils.getDate(latestTDBSearchDate);
                    request.setAttribute("dateTDBSearch", dateTDBSearch);
                }
                request.setAttribute("numPDBDbRefs", numPDBDbRefs);
                request.setAttribute("numTDBDbRefs", numTDBDbRefs);

                rd.forward(request, response);

                version.commit();
            }
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Get list of all Target Top Hits";
    }
}
