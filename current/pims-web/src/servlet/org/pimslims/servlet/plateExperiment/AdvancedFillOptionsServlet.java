package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.PlateExperimentUpdateBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * Plate
 * 
 * 
 * User clicks "Advanced fill options" under a ref input sample.
 * 
 * advancedFillOptions opens in modal window
 * 
 * GET /path/to/advancedFillOptionsServlet ?refInputSample=ref.input.sample.hook:1234
 * &experimentGroup=experiment.group.hook:4444 &isInModalWindow=true
 * 
 * Forwards to advancedFillOptions.jsp, which needs the name and hook of the refInputSample the name and hook
 * of the refInputSample's sampleCategory
 * 
 * User can choose between 1) varying amounts of a single sample (gradient or snake) 2) copying samples from a
 * plate 3) merging from several plates, by score
 * 
 * Option 1 - single sample, pattern fill
 * 
 * Under option 1, user can select from all samples currently in the refInputSample dropdown, or search for
 * more. If a sample was chosen in main window, this is pre-selected.
 * 
 * If user chooses a sample from the dropdown - Jump directly to pattern fill page. If user searches for a
 * sample - Use the standard sample search in modal window, restricting by sampleCategory. Javascript callback
 * function should set modal window src to show pattern fill page.
 * 
 * Pattern fill page
 * 
 * GET /path/to/advancedFillOptionsServlet ?refInputSample=ref.input.sample.hook:1234
 * &experimentGroup=experiment.group.hook:4444 &sample=sample.hook:3333 &isInModalWindow=true
 * &page=patternFill
 * 
 * Forwards to patternFill.jsp, which needs the name, hook and displayUnit of the refInputSample the name and
 * hook of the sample category the hook of the Sample chosen by the user the map of wells to experiments, just
 * like in the main plate window
 * 
 * User will be able to select a range of wells. They also specify a fill pattern (vertical or horizontal
 * gradient, snake…) and any two of [start volume, end volume, increment].
 * 
 * Javascript performs the calculations needed.
 * 
 * Then see "Final submission".
 * 
 * Option 2 - copy samples from a plate
 * 
 * Under option 2, the user can select from plates whose output samples are of the right sample category for
 * the RefInputSample.
 * 
 * Experience suggests that this will be a small enough collection to just list them all, without a search.
 * (This can be modified later if need be.)
 * 
 * User selects a plate, and clicks Next. Forward to "copy from plate" page.
 * 
 * GET /path/to/advancedFillOptionsServlet ?refInputSample=ref.input.sample.hook:1234
 * &experimentGroup=experiment.group.hook:4444 &sourceExperimentGroup=experiment.group.hook:7654
 * &isInModalWindow=true &page=copyFromPlate
 * 
 * Forwards to copyFromPlate.jsp, which needs the name, hook and displayUnit of the refInputSample the map of
 * wells to experiments, just like in the main plate window for the source plate(s), a similar map of wells to
 * samples
 * 
 * User makes selections (as in existing Copy from plate).
 * 
 * Then see "Final submission".
 * 
 * Option 3 - merge samples from several plates
 * 
 * Under option 3, the user can select several plates from which to merge. Choices are: - children of the
 * selected plate (but not the selected, parent, plate) - the selected plate and its siblings - search for a
 * random collection of plates (later)
 * 
 * Note that the dropdown lists of plates in options 1 and 2 will be different. - For "siblings", the list
 * will be of plates with the right sample category. - For "children of plate", the list will be of plates
 * whose *children* contain the right sample category.
 * 
 * [Future enhancements: AJAX GET to fetch and show list of plates that will be merged.]
 * 
 * User clicks "Next". Forward to "merge from plates" page.
 * 
 * GET /path/to/advancedFillOptionsServlet ?refInputSample=ref.input.sample.hook:1234
 * &experimentGroup=experiment.group.hook:4444 &source=experiment.group.hook:7777
 * &sourceSelectionMode=children [or siblings] &isInModalWindow=true &page=mergeFromPlates
 * 
 * Alternative - avoids re-work for "random plates" case: Replace &source=experiment.group.hook:7777
 * &sourceSelectionMode=children [or siblings] with
 * &source=experiment.group.hook:7778,experiment.group.hook:7779,experiment.group.hook:7780 This means that
 * advancedFillOptions.jsp would need to know what plates would result from selecting "siblings of"/"children
 * of" a given plate.
 * 
 * Forwards to mergeFromPlates.jsp, which needs the name, hook and displayUnit of the refInputSample the map
 * of wells to experiments, just like in the main plate window for the source plates, a similar map of wells
 * to experiments, experiments must have output sample and score) the default order of the source plates
 * 
 * This looks just like the PowerPoint mock-up.
 * 
 * Then see "Final submission".
 * 
 * Final submission
 * 
 * Final submission in all cases is via AJAX POST, and should look exactly like an update from the main window
 * (i.e., nothing to do server-side):
 * 
 * POST /update/AjaxPlateExperimentUpdate refInputSample=ref.input.sample.hook:1234
 * experiment.hook:9995:sample=sample.hook:3333 experiment.hook:9995:amount=4uL
 * experiment.hook:9996:sample=sample.hook:3333 experiment.hook:9996:amount=1uL
 * experiment.hook:9997:sample=sample.hook:3335 experiment.hook:9997:amount=1uL
 * experiment.hook:9998:sample=sample.hook:3333 experiment.hook:9998:amount=1uL
 * 
 * Samples and/or volumes will be different, depending on which option was taken.
 * 
 * Response is JSON of modified experiments, as before. Response handler should be exactly the same (though
 * either the response handler or the function making the request needs to close the modal window).
 * 
 * Numeric parameters
 * 
 * Temperatures, etc., may also be applied as gradients. A simplified flow would go directly to
 * patternFill.jsp in the modal window.
 * 
 * GET /path/to/advancedFillOptionsServlet ?parameterDefinition=parameter.definition.hook:8765
 * &experimentGroup=experiment.group.hook:4444 &isInModalWindow=true &page=patternFill
 * 
 * Forwards to patternFill.jsp, which needs the name, hook and displayUnit of the ParameterDefinition the map
 * of wells to experiments, just like in the main plate window
 * 
 * Final submission would be AJAX POST to the same servlet and in the same format as updating parameter values
 * from the main window. Response is the same JSON. Response handler is the same (but modal window needs to be
 * closed).
 * 
 * 
 */
public class AdvancedFillOptionsServlet extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    private static final String IS_IN_MODAL_WINDOW = QuickSearch.IS_IN_MODAL_WINDOW;

    /**
     * 
     */
    private static final String ADVANCEDFILLOPTIONS_URL = "/JSP/plateExperiment/advancedFillOptions.jsp";

    public AdvancedFillOptionsServlet() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a plate";
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

        System.out.println("org.pimslims.servlet.plateExperiment.AdvanceFillOptions doGet");

        if (!this.checkStarted(request, response)) {
            return;
        }

        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.plateExperiment.AdvanceFillOptions request parameter ["
                + e.getKey() + ":" + s.toString() + "]");
        }

        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {

            // ChooseInputPlate for Copy-From-Plate

            final RefInputSample refInputSample =
                (RefInputSample) this.getRequiredObject(version, request, response, request
                    .getParameter("refInputSample"));
            if (null == refInputSample) {
                return;
            }

            final ExperimentGroup group =
                (ExperimentGroup) this.getRequiredObject(version, request, response, request
                    .getParameter("experimentGroup"));
            if (null == group) {
                return;
            }

            request.setAttribute("experimentGroup", group);
            request.setAttribute("refInputSample", refInputSample);
            final SampleCategory category = refInputSample.getSampleCategory();
            request.setAttribute("sampleCategory", category);
            final Collection<ExperimentGroup> groups =
                PlateExperimentDAO.getExperimentGroupsForPlates(category);
            groups.remove(group);
            request.setAttribute("groups", groups);

            // ChooseInputPlate for Copy-From-Plate
            final Collection<ExperimentGroup> siblingGroups = PlateExperimentUtility.getSiblingGroups(groups);
            request.setAttribute("siblingGroups", siblingGroups);

            final Collection<ExperimentGroup> parentGroups = new HashSet<ExperimentGroup>();
            //final Collection<ExperimentGroup> parentGroups =
            //    PlateExperimentUtility.getParentGroups(siblingGroups);
            request.setAttribute("parentGroups", parentGroups);

            if (null != request.getParameter("sample")) {
                final ModelObject modelObject =
                    this.getRequiredObject(version, request, response, request.getParameter("sample"));

                final Sample sample = (Sample) modelObject;
                request.setAttribute("sample", BeanFactory.newBean(sample));
            }

            final PlateReader reader = new PlateReader(version, group, null);
            final PlateExperimentUpdateBean updateBean = new PlateExperimentUpdateBean(reader.getExperimentGroup(), null);

            final Collection<Holder> holders = HolderFactory.getPlates(group);
            for (final Holder plate : holders) {
                // there are none, unless this is a plate experiment
                request.setAttribute(HolderFactory.getHolderPoint(plate) + "Name", plate.getName());
                request.setAttribute(HolderFactory.getHolderPoint(plate) + "Hook", plate.get_Hook());
                request.setAttribute(HolderFactory.getHolderPoint(plate), updateBean
                    .getPlateExperimentLayout(plate));
            }

            if (null != request.getParameter(AdvancedFillOptionsServlet.IS_IN_MODAL_WINDOW)) {
                request.setAttribute(AdvancedFillOptionsServlet.IS_IN_MODAL_WINDOW, request
                    .getParameter(AdvancedFillOptionsServlet.IS_IN_MODAL_WINDOW));
            }

            final String forwardURL = AdvancedFillOptionsServlet.ADVANCEDFILLOPTIONS_URL;
            /*
            if (null != request.getParameter("page")) {
                if ("patternFill".equals(request.getParameter("page"))) {
                    forwardURL = AdvancedFillOptionsServlet.PATTERNFILL_URL;
                }
                if ("copyFromPlate".equals(request.getParameter("page"))) {
                    forwardURL = AdvancedFillOptionsServlet.COPYFROMPLATE_URL;
                }
                if ("mergeFromPlate".equals(request.getParameter("page"))) {
                    forwardURL = AdvancedFillOptionsServlet.MERGEFROMPLATE_URL;
                }
            }
            */
            final RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    //protected Comparator getOrder() {
    //    return new WellExperimentBean.ColumnOrder();
    //}

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        throw new UnsupportedOperationException(this.getClass().getName() + " does not accept POST");

    }

}
