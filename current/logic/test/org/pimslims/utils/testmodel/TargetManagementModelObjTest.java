/*
 * Created on 30.06.2005
 */
package org.pimslims.utils.testmodel;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.ConferenceCitation;
import org.pimslims.model.core.JournalCitation;
import org.pimslims.model.core.ThesisCitation;

// was import org.pimslims.model.target.GenomicProject;

/**
 * Test model classes which concern Target management implementation.
 * 
 * @author Petr Troshin
 */
public class TargetManagementModelObjTest {

    /**
     * 
     */
    private TargetManagementModelObjTest() { /* empty */
    }

    public static void main(final String[] args) {
        final ModelObjectsTest mot = new ModelObjectsTest(ModelObjectsTest.logLevelNull);
        // Nothing should be created since Citation is Abstract class
        mot.constructModelObject(Citation.class.getName(), true);

        ModelObject mobj = mot.constructModelObject(BookCitation.class.getName(), true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject(JournalCitation.class.getName(), true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject(ConferenceCitation.class.getName(), true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject(ThesisCitation.class.getName(), true);
        mot.resetValues(mobj, true);

        // mobj = mot.constructModelObject(Molecule.class.getName(), true);
        // mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.people.Person", true);

        // java.lang.ClassCastException
        // at
        // org.pimslims.implementation.ConstraintFactory$3.verify(ConstraintFactory.java:96)
        // mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.sample.Sample", true);
        mot.resetValues(mobj, true);

        /*
         * TODO test org.pimslims.model.target.Project mobj = mot.constructModelObject(GenomicProject.class.getName(),
         * true); mot.resetValues(mobj, true);
         */

        mobj = mot.constructModelObject("org.pimslims.model.people.Organisation", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.core.ExternalDbLink", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.reference.Organism", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.sample.RefSample", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.sample.Holder", true);
        mot.resetValues(mobj, true);

        /* mobj = mot.constructModelObject("org.pimslims.model.sample.CrystalSample", true);
         mot.resetValues(mobj, true); */

        mobj = mot.constructModelObject("org.pimslims.model.refSampleComponent.Cell", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.reference.ComponentCategory", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.molecule.Molecule", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.reference.HazardPhrase", true);
        mot.resetValues(mobj, true);

        mobj = mot.constructModelObject("org.pimslims.model.target.ResearchObjective", true);
        mot.resetValues(mobj, true);

        mot.setLogLevel(ModelObjectsTest.logLevelDebug);

        // Protocol test
        mobj = mot.constructModelObject(org.pimslims.model.protocol.Protocol.class.getName(), true, true, 1);
        // mot.resetValues(mobj, true);

        mot.setLogLevel(ModelObjectsTest.logLevelInfo);

    }
}
