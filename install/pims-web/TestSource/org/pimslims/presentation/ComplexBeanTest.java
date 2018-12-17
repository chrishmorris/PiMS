/**
 * pims-web org.pimslims.presentation ComplexBeanTest.java
 * 
 * @author Marc Savitsky
 * @date 15 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * ComplexBeanTest
 * 
 */
public class ComplexBeanTest extends AbstractTestCase {

    ComplexBean bean;

    private Target target;

    private final String name = "ComplexBeanTest";

    private final String whychosen = "Selected for testing";

    private final String details = "The quick brown fox jumped over the lazy dog";

    /**
     * @param name
     */
    public ComplexBeanTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

        this.bean = new ComplexBean();
        this.bean.setName(this.name);
        this.bean.setWhyChosen(this.whychosen);
        this.bean.setDetails(this.details);

        this.target = POJOFactory.create(this.wv, Target.class);
        this.target.setName("target" + System.currentTimeMillis());

        final ResearchObjectiveElement bpc = POJOFactory.create(this.wv, ResearchObjectiveElement.class);
        bpc.setTarget(this.target);
        bpc.setComponentType("target");
        bpc.setDetails(this.details);

        this.bean.addComponent(new ResearchObjectiveElementBean(bpc));

        try {
            ResearchObjectiveElementBean.createNewComplex(this.wv, this.bean);
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        Assert.assertFalse(this.wv.isCompleted());
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testCreate() throws AccessException, ConstraintException {
        final ComplexBean bean0 = new ComplexBean();
        bean0.setName(AbstractTestCase.UNIQUE);
        ResearchObjectiveElementBean.createNewComplex(this.wv, bean0);

    }

    public void testComplexName() throws ConstraintException {

        final ComplexBean myBean = ComplexBeanTest.readComplexName(this.wv, this.name);
        Assert.assertEquals("Problem with ComplexName", this.name, myBean.getName());
        final String hook = myBean.getBlueprintHook();

        final String newName = "New Complex Name";
        myBean.setName(newName);

        ComplexBeanWriter.setName(this.wv, myBean);
        final ComplexBean myNewBean = ResearchObjectiveElementBean.readComplexHook(this.wv, hook);
        Assert.assertEquals("Problem with ComplexName", newName, myNewBean.getName());

    }

    public void testComplexWhyChosen() throws ConstraintException {

        final ComplexBean myBean = ComplexBeanTest.readComplexName(this.wv, this.name);
        Assert.assertEquals("Problem with ComplexWhyChosen", this.whychosen, myBean.getWhyChosen());
        final String hook = myBean.getBlueprintHook();

        final String newWhyChosen = "New Complex Name";
        myBean.setWhyChosen(newWhyChosen);

        ComplexBeanWriter.setWhyChosen(this.wv, myBean);
        final ComplexBean myNewBean = ResearchObjectiveElementBean.readComplexHook(this.wv, hook);
        Assert.assertEquals("Problem with ComplexWhyChosen", newWhyChosen, myNewBean.getWhyChosen());

    }

    public void testComplexDetails() throws ConstraintException {

        final ComplexBean myBean = ComplexBeanTest.readComplexName(this.wv, this.name);
        Assert.assertEquals("Problem with ComplexDetails", this.details, myBean.getDetails());
        final String hook = myBean.getBlueprintHook();

        final String newDetails = "New Complex Name";
        myBean.setDetails(newDetails);

        ComplexBeanWriter.setDetails(this.wv, myBean);
        final ComplexBean myNewBean = ResearchObjectiveElementBean.readComplexHook(this.wv, hook);
        Assert.assertEquals("Problem with ComplexDetails", newDetails, myNewBean.getDetails());

    }

    public void testComplexNewComponent() throws AccessException, ConstraintException {

        final ComplexBean myBean = ComplexBeanTest.readComplexName(this.wv, this.name);
        Assert.assertEquals("Problem with ComplexComponents", 1, myBean.getComponents().size());
        final String hook = myBean.getBlueprintHook();

        final ResearchObjectiveElement bpc = POJOFactory.create(this.wv, ResearchObjectiveElement.class);
        bpc.setComponentType("target");
        bpc.setTarget(this.target);
        bpc.setDetails(this.details);
        final ResearchObjectiveElementBeanI bean = new ResearchObjectiveElementBean(bpc);
        myBean.addComponent(bean);
        ComplexBeanTest.setComponents(this.wv, myBean);
        final ComplexBean myNewBean = ResearchObjectiveElementBean.readComplexHook(this.wv, hook);
        Assert.assertEquals("Problem with ComplexComponents", 2, myNewBean.getComponents().size());

    }

    public void testComplexNoComponents() throws ConstraintException {

        final ComplexBean myBean = ComplexBeanTest.readComplexName(this.wv, this.name);
        Assert.assertEquals("Problem with ComplexComponents", 1, myBean.getComponents().size());
        final String hook = myBean.getBlueprintHook();

        myBean.setComponents(Collections.EMPTY_LIST);
        ComplexBeanTest.setComponents(this.wv, myBean);
        final ComplexBean myNewBean = ResearchObjectiveElementBean.readComplexHook(this.wv, hook);
        Assert.assertEquals("Problem with ComplexComponents", 0, myNewBean.getComponents().size());

    }

    //TODO this makes no sense - the components already exist
    public static void setComponents(final WritableVersion version, final ComplexBean complex)
        throws ConstraintException {
        final ResearchObjective blueprint = (ResearchObjective) version.get(complex.getBlueprintHook());
        final Set<ResearchObjectiveElement> set = new HashSet<ResearchObjectiveElement>();
        for (final ResearchObjectiveElementBeanI bean : complex.getComponents()) {
            final ResearchObjectiveElement component = (ResearchObjectiveElement) version.get(bean.getHook());
            set.add(component);
        }
        blueprint.setResearchObjectiveElements(set);
    }

    public static ComplexBean readComplexName(final ReadableVersion version, final String name) {

        final ComplexBean bean = new ComplexBean();

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(ResearchObjective.PROP_COMMONNAME, name);

        final ResearchObjective blueprint = version.findFirst(ResearchObjective.class, criteria);
        bean.setBlueprintHook(blueprint.get_Hook());
        bean.setName(blueprint.getLocalName());
        bean.setWhyChosen(blueprint.getWhyChosen());
        bean.setDetails(blueprint.getDetails());

        for (final ResearchObjectiveElement component : blueprint.getResearchObjectiveElements()) {
            final ResearchObjectiveElementBeanI componentBean = new ResearchObjectiveElementBean(component);
            bean.addComponent(componentBean);
        }

        return bean;
    }
}
