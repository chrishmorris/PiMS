/**
 * pims-web org.pimslims.servlet.plateExperiment PlateExperimentDAOTest.java
 * 
 * @author bl67
 * @date 16 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.bioinf.local.SequenceGetter;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.presentation.plateExperiment.PlateCriteria;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * PlateExperimentDAOTest
 * 
 */
public class PlateExperimentDAOTest extends AbstractTestCase {
    private static final String UNIQUE = "ed" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    public void testGetGroupsForPlates() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, PlateExperimentDAOTest.UNIQUE);
            final ExperimentGroup group = this.createGroup(version, category);

            final Collection<ExperimentGroup> groups =
                PlateExperimentDAO.getExperimentGroupsForPlates(category);
            Assert.assertEquals(1, groups.size());
            Assert.assertEquals(group, groups.iterator().next());
        } finally {
            version.abort();
        }
    }

    public void testPrivacy() throws AccessException, ConstraintException, AbortedException {
        final String userName = PlateExperimentDAOTest.UNIQUE;
        // create userid with limited read permissions
        WritableVersion version =
            AbstractTestCase.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final User user = new User(version, userName);
            final UserGroup group = new UserGroup(version, userName);
            group.addMemberUser(user);
            final LabNotebook book = new LabNotebook(version, userName);
            new Permission(version, "create", book, group);
            new Permission(version, "update", book, group);
            new Permission(version, "delete", book, group);
            new Permission(version, "read", new LabNotebook(version, userName + "two"), group);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // now test
        version = AbstractTestCase.model.getWritableVersion(userName);
        try {
            final SampleCategory category = new SampleCategory(version, PlateExperimentDAOTest.UNIQUE);
            final ExperimentGroup group = this.createGroup(version, category);

            final Collection<ExperimentGroup> groups =
                PlateExperimentDAO.getExperimentGroupsForPlates(category);
            Assert.assertEquals(0, groups.size());
        } finally {
            version.abort();
        }

        // clean up
        version = AbstractTestCase.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final User user = version.findFirst(User.class, User.PROP_NAME, userName);
            final UserGroup group = user.getUserGroups().iterator().next();
            final Set<Permission> permissions = group.getPermissions();
            final Set<LabNotebook> books = new HashSet();
            for (final Iterator iterator = permissions.iterator(); iterator.hasNext();) {
                final Permission permission = (Permission) iterator.next();
                books.add(permission.getLabNotebook());
                permission.delete();
            }
            for (final Iterator iterator = books.iterator(); iterator.hasNext();) {
                final LabNotebook labNotebook = (LabNotebook) iterator.next();
                labNotebook.delete();
            }
            group.delete();
            user.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    private ExperimentGroup createGroup(final WritableVersion version, final SampleCategory category)
        throws ConstraintException {
        final ExperimentGroup group =
            new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
        final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
        final Holder holder = new Holder(version, PlateExperimentDAOTest.UNIQUE, null);
        final Experiment experiment =
            new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                PlateExperimentDAOTest.NOW, type);
        experiment.setExperimentGroup(group);
        final Sample sample = new Sample(version, PlateExperimentDAOTest.UNIQUE);
        sample.addSampleCategory(category);
        sample.setIsActive(true);
        sample.setHolder(holder);
        new OutputSample(version, experiment).setSample(sample);
        return group;
    }

    public void testGetPlates() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            final ExperimentGroup eg = this.createPlate(version, type);
            final Collection<Holder> holders = HolderFactory.getPlates(eg);
            Assert.assertEquals(1, holders.size());
            Assert.assertTrue(HolderFactory.isPlateExperiment(eg));
        } finally {
            version.abort();
        }
    }

    public void testGetPlatesMixed() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            final ExperimentGroup eg = this.createPlate(version, type);
            // add another experiment, with output not in holder
            final Experiment exp =
                AbstractTestCase.create(version, Experiment.class, Experiment.PROP_EXPERIMENTTYPE, type);
            exp.setExperimentGroup(eg);
            final Sample sample = AbstractTestCase.create(version, Sample.class);
            final OutputSample os =
                AbstractTestCase.create(version, OutputSample.class, OutputSample.PROP_EXPERIMENT, exp);
            os.setSample(sample);
            version.flush();
            Assert.assertEquals(2, eg.getExperiments().size());

            final Collection<Holder> holders = HolderFactory.getPlates(eg);
            Assert.assertEquals(1, holders.size());
            Assert.assertFalse(HolderFactory.isPlateExperiment(eg));
        } finally {
            version.abort();
        }
    }

    public final void testGetNextExperiments() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
            final Protocol protocol = new Protocol(version, PlateExperimentDAOTest.UNIQUE, type);
            final RefOutputSample out = new RefOutputSample(version, category, protocol);
            final RefInputSample in = new RefInputSample(version, category, protocol);

            final Protocol protocol2 = new Protocol(version, PlateExperimentDAOTest.UNIQUE + "two", type);
            protocol2.setIsForUse(false);
            new RefInputSample(version, category, protocol2);

            final Collection<RefInputSampleBean> beans = PlateExperimentDAO.getRefInputBeans(out);
            Assert.assertEquals(1, beans.size());
            Assert.assertEquals(in.get_Hook(), beans.iterator().next().getHook());
        } finally {
            version.abort();
        }
    }

    public final void testGetNextExperimentsSorted() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
            final Protocol protocol = new Protocol(version, PlateExperimentDAOTest.UNIQUE, type);
            final RefOutputSample out = new RefOutputSample(version, category, protocol);
            new RefInputSample(version, category, protocol);
            new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                PlateExperimentDAOTest.NOW, type).setProtocol(protocol);

            final Protocol protocol2 = new Protocol(version, PlateExperimentDAOTest.UNIQUE + "two", type);
            final RefInputSample in2 = new RefInputSample(version, category, protocol2);
            new Experiment(version, PlateExperimentDAOTest.UNIQUE + "twoA", PlateExperimentDAOTest.NOW,
                PlateExperimentDAOTest.NOW, type).setProtocol(protocol2);
            new Experiment(version, PlateExperimentDAOTest.UNIQUE + "twoB", PlateExperimentDAOTest.NOW,
                PlateExperimentDAOTest.NOW, type).setProtocol(protocol2);

            final Collection<RefInputSampleBean> beans = PlateExperimentDAO.getRefInputBeans(out);
            Assert.assertEquals(2, beans.size());
            Assert.assertEquals(in2.get_Hook(), beans.iterator().next().getHook());
        } finally {
            version.abort();
        }
    }

    public void testGetAllPlates() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(20);
            final long start = System.currentTimeMillis();
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertTrue(size <= 20);
            Assert.assertTrue(size >= 0);
            System.out.println("PlateExperimentDAO.getPlateBeanList using "
                + (System.currentTimeMillis() - start) + " ms to load " + size + " plates");
            //final PlateBean firstPlateBean = result.iterator().next();
            //Assert.assertNotNull(firstPlateBean);
        } finally {
            version.abort();
        }
    }

    public void testGetAllGroups() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);

            new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                PlateExperimentDAOTest.NOW, type).setExperimentGroup(group);
            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(20);
            criteria.setOnlyGroups(true);
            final long start = System.currentTimeMillis();
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertTrue(size <= 20);
            Assert.assertTrue(size > 0);
            System.out.println("PlateExperimentDAO.getPlateBeanList using "
                + (System.currentTimeMillis() - start) + " ms to load " + size + " plates");
            final PlateBean firstPlateBean = result.iterator().next();
            Assert.assertNotNull(firstPlateBean);
        } finally {
            version.abort();
        }
    }

    public void TODOtestGetAllGroupsNotAdmin() throws AccessException, ConstraintException, AbortedException {
        String username;
        WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final User user = new User(version, PlateExperimentDAOTest.UNIQUE);
            username = user.getName();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        version = AbstractTestCase.model.getWritableVersion(username);
        try {
            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(20);
            criteria.setOnlyGroups(true);
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertTrue(size <= 20);
        } finally {
            version.abort();
        }
        version = AbstractTestCase.model.getTestVersion();
        try {
            version.findFirst(User.class, User.PROP_NAME, username).delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testGetPlateBeanList() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            final ExperimentGroup eg = this.createPlate(version, type);
            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(10);
            criteria.setExpTypeName(type.getName());
            final long start = System.currentTimeMillis();
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertTrue(size <= 10);
            Assert.assertTrue(size > 0);
            System.out.println("PlateExperimentDAO.getPlateBeanList using "
                + (System.currentTimeMillis() - start) + " ms to load " + size + " plates");
            final PlateBean firstPlateBean = result.iterator().next();
            Assert.assertNotNull(firstPlateBean);
            Assert.assertEquals(eg, firstPlateBean.getmodelObject());
            Assert.assertEquals(eg.getName(), firstPlateBean.getExpGroupName());
            Assert.assertEquals(eg.getName(), firstPlateBean.getName());
            Assert.assertEquals(eg.get_Hook(), firstPlateBean.getExpGroupHook());
            Assert.assertEquals(eg.get_Hook(), firstPlateBean.getHook());
            Assert.assertEquals("", firstPlateBean.getCreator());
            for (final PlateBean plateBean : result) {
                Assert.assertEquals(type.getName(), plateBean.getExpTypeName());
            }
        } finally {
            version.abort();
        }
    }

    public void testGetTotalCount() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {

            final long start = System.currentTimeMillis();
            final int was = PlateExperimentDAO.getTotalPlateExperimentCount(version);
            final long time = (System.currentTimeMillis() - start);
            System.out.println("PlateExperimentDAO.getTotalPlateExperimentCount using " + time
                + " ms to count " + was + " plates");
            Assert.assertTrue(time < 11000);

            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            this.createPlate(version, type);
            this.createExperimentGroup(version, type);
            final int now = PlateExperimentDAO.getTotalPlateExperimentCount(version);
            Assert.assertEquals(was + 1, now);
        } finally {
            version.abort();
        }
    }

    public void testGetGroupListFindsGroups() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            this.createExperimentGroup(version, type);

            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(10);
            criteria.setExpTypeName(type.getName());
            criteria.setOnlyGroups(true);
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertEquals(1, size);
            // final PlateBean bean = result.iterator().next();
        } finally {
            version.abort();
        }
    }

    public void testCaseInsensitive() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);
            final ExperimentGroup group = this.createExperimentGroup(version, type);

            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(1);
            criteria.setPlateName(group.getName().toUpperCase());
            criteria.setOnlyGroups(true);
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertEquals(1, size);
            final PlateBean bean = result.iterator().next();
            Assert.assertEquals(group.get_Hook(), bean.getHook());
        } finally {
            version.abort();
        }
    }

    /**
     * PlateExperimentDAOTest.createExperimentGroup
     * 
     * @param version
     * @param type
     * @throws AccessException
     * @throws ConstraintException
     */
    private ExperimentGroup createExperimentGroup(final WritableVersion version, final ExperimentType type)
        throws AccessException, ConstraintException {
        final ExperimentGroup eg = AbstractTestCase.create(version, ExperimentGroup.class);
        final Experiment exp =
            AbstractTestCase.create(version, Experiment.class, Experiment.PROP_EXPERIMENTTYPE, type);
        exp.setExperimentGroup(eg);
        return eg;
    }

    public void testGetGroupListFindsNoHolders() throws AccessException, ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = AbstractTestCase.create(version, ExperimentType.class);

            this.createPlate(version, type);

            final PlateCriteria criteria = new PlateCriteria();
            criteria.setStart(0);
            criteria.setLimit(10);
            criteria.setExpTypeName(type.getName());
            criteria.setOnlyGroups(true);
            final List<PlateBean> result = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int size = result.size();
            Assert.assertEquals(0, size);
            // final PlateBean bean = result.iterator().next();
        } finally {
            version.abort();
        }
    }

    /**
     * PlateExperimentDAOTest.createPlate
     * 
     * @param version
     * @param type
     * @throws AccessException
     * @throws ConstraintException
     */
    private ExperimentGroup createPlate(final WritableVersion version, final ExperimentType type)
        throws AccessException, ConstraintException {
        final ExperimentGroup eg = AbstractTestCase.create(version, ExperimentGroup.class);

        final Experiment exp =
            AbstractTestCase.create(version, Experiment.class, Experiment.PROP_EXPERIMENTTYPE, type);
        exp.setExperimentGroup(eg);

        final HolderType ht =
            version.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");
        final Holder holder =
            AbstractTestCase.create(version, Holder.class, AbstractHolder.PROP_HOLDERTYPE, ht);
        final Sample sample = AbstractTestCase.create(version, Sample.class, Sample.PROP_HOLDER, holder);
        final OutputSample os =
            AbstractTestCase.create(version, OutputSample.class, OutputSample.PROP_EXPERIMENT, exp);
        os.setSample(sample);
        return eg;
    }

    public void testGetNoParameters() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
            final Map<Experiment, Collection<Parameter>> parameters =
                PlateExperimentDAO.getAssociates(group, Parameter.class);
            Assert.assertEquals(0, parameters.size());
        } finally {
            version.abort();
        }
    }

    public void testGetParameter() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                    PlateExperimentDAOTest.NOW, type);
            experiment.setExperimentGroup(group);
            final Parameter parameter = new Parameter(version, experiment);
            final Map<Experiment, Collection<Parameter>> parameters =
                PlateExperimentDAO.getAssociates(group, Parameter.class);
            Assert.assertEquals(1, parameters.size());
            Assert.assertTrue(parameters.containsKey(experiment));
            final Collection<Parameter> parms = parameters.get(experiment);
            Assert.assertEquals(1, parms.size());
            Assert.assertTrue(parms.contains(parameter));
        } finally {
            version.abort();
        }
    }

    public void testGetExperimentsWithObjectives() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                    PlateExperimentDAOTest.NOW, type);
            experiment.setExperimentGroup(group);
            final ResearchObjective ro =
                new ResearchObjective(version, "testTwo" + System.currentTimeMillis(),
                    PlateExperimentDAOTest.UNIQUE);
            experiment.setProject(ro);

            final Map<Experiment, ResearchObjective> map =
                PlateExperimentDAO.getExperimentsAndResearchObjectives(group);
            Assert.assertTrue(map.containsKey(experiment));
            Assert.assertEquals(ro, map.get(experiment));
        } finally {
            version.abort();
        }
    }

    public void testGetPcrProduct() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PlateExperimentDAOTest.UNIQUE, PlateExperimentDAOTest.NOW,
                    PlateExperimentDAOTest.NOW, type);
            experiment.setExperimentGroup(group);
            final ResearchObjective ro =
                new ResearchObjective(version, "test" + System.currentTimeMillis(),
                    PlateExperimentDAOTest.UNIQUE);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, PlateExperimentDAOTest.UNIQUE,
                    PlateExperimentDAOTest.UNIQUE, ro);
            final Molecule tmc =
                new Molecule(version, "DNA", PlateExperimentDAOTest.UNIQUE + SequenceGetter.PCR_PRODUCT);
            tmc.setSequence("AAAA");
            final Collection<Molecule> tmcs = new HashSet();
            tmcs.add(tmc);
            tmcs.add(new Molecule(version, "other", PlateExperimentDAOTest.UNIQUE));
            roe.setTrialMolComponents(tmcs);
            experiment.setProject(ro);

            final Map<Experiment, Collection<Molecule>> map = PlateExperimentDAO.getTrialMolecules(group);
            Assert.assertTrue(map.containsKey(experiment));
            Assert.assertEquals(2, map.get(experiment).size());
            Assert.assertTrue(map.get(experiment).contains(tmc));
        } finally {
            version.abort();
        }
    }

    public void testGetParentGroups() throws ConstraintException, AccessException {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final String parentGroupName = "ParentGroup" + System.currentTimeMillis();
            final ExperimentGroup parentGroup =
                new ExperimentGroup(version, parentGroupName, PlateExperimentDAOTest.UNIQUE);

            final ExperimentGroup group =
                new ExperimentGroup(version, "ChildGroup" + System.currentTimeMillis(),
                    PlateExperimentDAOTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, PlateExperimentDAOTest.UNIQUE);

            for (int i = 0; i < 96; i++) {

                final String childExperiment = "ChildExperiment_" + i;
                final Experiment experiment =
                    new Experiment(version, childExperiment, PlateExperimentDAOTest.NOW,
                        PlateExperimentDAOTest.NOW, type);
                experiment.setExperimentGroup(group);

                final InputSample inputSample =
                    POJOFactory.create(version, InputSample.class, InputSample.PROP_NAME,
                        PlateExperimentDAOTest.UNIQUE);
                experiment.addInputSample(inputSample);

                final String sampleName = "Sample_" + i;
                final Sample sample =
                    POJOFactory.create(version, Sample.class, AbstractSample.PROP_NAME, sampleName);
                inputSample.setSample(sample);

                final Experiment exp = POJOFactory.createExperiment(version);

                final OutputSample outputSample = POJOFactory.createOutputSample(version, exp);
                sample.setOutputSample(outputSample);

                exp.setExperimentGroup(parentGroup);
            }

            final long start = System.currentTimeMillis();
            final Collection<ExperimentGroup> groups = PlateExperimentDAO.getParentGroups(group);
            System.out.println("GetParentGroups took [" + (System.currentTimeMillis() - start) + "]");

            Assert.assertEquals(groups.size(), 1);
            Assert.assertEquals(parentGroupName, groups.iterator().next().get_Name());

        } finally {
            version.abort();
        }
    }
}
