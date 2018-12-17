package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.model.Target;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ConstructDAO;
import org.pimslims.crystallization.dao.PersonDAO;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.crystallization.dao.view.ConstructViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;

public class ConstructServiceImpl extends BaseServiceImpl implements ConstructService {
    private final ConstructDAO constructDAO;

    public ConstructServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        constructDAO = new ConstructDAO(getVersion());
    }

    /**
     * This will close, archive or limit the users access to this object.... WE CAN IGNORE THIS FOR THE TIME
     * BEING
     * 
     * @param construct
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(final Construct construct) throws BusinessException {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * To store a construct in the database...
     * 
     * @param construct
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final Construct construct) throws BusinessException {
        constructDAO.createPO(construct);
    }

    public Construct find(final long id) throws BusinessException {
        return constructDAO.getFullXO((ResearchObjective) version.get(id));
    }

    public Construct find(final String id) throws BusinessException {
        return constructDAO.getFullXO((ResearchObjective) version.get(id));
    }

    public Construct findByName(final String name) throws BusinessException {
        final ResearchObjective eb =
            version.findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME, name);
        return constructDAO.getFullXO(eb);
    }

    public Construct findBySample(final Sample sample) throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Project getProject(final Construct construct) throws BusinessException {
        // LATER Auto-generated method stub
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<Sample> getSamples(final Construct construct, final BusinessCriteria criteria)
        throws BusinessException {
        final ResearchObjective eb =
            version
                .findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME, construct.getName());
        return getSamples(eb);
    }

    public static Collection<Sample> getProductionExperiments(final ResearchObjective eb)
        throws BusinessException {
        final Set<Experiment> experiments = eb.getExperiments();
        final Set<Sample> ret = new HashSet<Sample>(experiments.size());
        // now check which experiments made purified protein
        final SampleCategory purifiedProtein = SampleDAO.getSampleCategory(eb.get_Version());
        for (final Experiment experiment : experiments) {
            final Set<OutputSample> outputSamples = experiment.getOutputSamples();
            for (final OutputSample os : outputSamples) {
                final org.pimslims.model.sample.Sample pimsSample = os.getSample();
                if (null == pimsSample) {
                    continue;
                }
                if (!pimsSample.getSampleCategories().contains(purifiedProtein)) {
                    continue;
                }
                final Sample xtalSample = (new SampleDAO(eb.get_Version())).getFullXO(pimsSample);
                final Calendar date = Calendar.getInstance();
                date.setTimeInMillis(experiment.getEndDate().getTime().getTime());
                xtalSample.setCreateDate(date);

                xtalSample.setDescription(os.getExperiment().getDetails());
                ret.add(xtalSample);
            }
        }

        return ret;
    }

    /**
     * @param eb
     * @return
     * @throws BusinessException
     */
    static List<Sample> getSamples(final ResearchObjective eb) throws BusinessException {
        final Collection<Sample> experiments = getProductionExperiments(eb);
        final List<Sample> ret = new ArrayList<Sample>(experiments.size());
        for (final Sample experiment : experiments) {
            ret.add(experiment);
        }
        return ret;
    }

    public Target getTarget(final Construct construct) throws BusinessException {
        return construct.getTarget();
    }

    /**
     * 
     * @param construct
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final Construct construct) throws BusinessException {
        constructDAO.updatePO(construct);
    }

    public static ResearchObjective getExpBlueprint(final ReadableVersion version, final Construct construct) {
        return version.findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME, construct
            .getName());
    }

    /**
     * 
     * @param user
     * @param userOnly - if this is true, function should only return results that belong to the user, if
     *            false, it should return results belonging to the user and all the groups to which they
     *            belong.
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Construct> findByUser(final Person user) throws BusinessException {
        final PersonDAO personDAO = new PersonDAO(version);
        final User pimsScientist = personDAO.getUser(user);
        final Collection<Construct> ret = getConstructsByPerson(pimsScientist);
        return ret;
    }

    /**
     * @param pimsScientist
     * @return
     * @throws BusinessException
     */
    Collection<Construct> getConstructsByPerson(final User pimsScientist) throws BusinessException {
        final Collection<ResearchObjective> expBlueprints = pimsScientist.getResearchObjectives();
        final Collection<Construct> constructs = new HashSet<Construct>(expBlueprints.size());
        for (final ResearchObjective eb : expBlueprints) {
            final Construct construct = constructDAO.getFullXO(eb);
            constructs.add(construct);
        }
        return constructs;
    }

    /**
     * @TODO REQUIRED
     * @param group
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Construct> findByGroup(final Group group, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Construct> findAll(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<ConstructView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViews(criteria);
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        return getViewDAO().convertPropertyName(property);
    }

    private ViewDAO<ConstructView> viewDAO;

    private ViewDAO<ConstructView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new ConstructViewDAO(version);
        }
        return viewDAO;
    }
}
