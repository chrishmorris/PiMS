/*
 * Created on 24.06.2005
 */
package org.pimslims.utils.testmodel;

import java.util.ArrayList;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.Target;

/**
 * Examples on usage of ModelObjectsTest class Please see the code.
 * 
 * @author Petr Troshin
 */
@Deprecated
// obsolete
public class ModelObjectsTestExample {

    private ModelObjectsTestExample() { /* empty */
    }

    public static void main(final String[] args) {

        // Create the ModelObjectsTest class providing the desired log level
        // to the constructor. The log levels defined in the ModelObjectsTest
        // class
        // Possible log levels
        // logLevelNull - no log only error messages
        // logLevelInfo - general information on class creation will be printed
        // out
        // logLevelDebug - all possible information on a created object will be
        // printed out

        final ArrayList<String> skippedObjects = new ArrayList<String>();
        skippedObjects.add("org.pimslims.model.core.Citation");
        skippedObjects.add("org.pimslims.model.people.Group");
        skippedObjects.add("org.pimslims.model.people.PersonInGroup");
        skippedObjects.add("org.pimslims.model.chemElement.ChemElement");
        skippedObjects.add("org.pimslims.model.molecule.MolResidue");
        skippedObjects.add("org.pimslims.model.molecule.MolResLinkEnd");
        //skippedObjects.add("ccp.api.ps.Implementation.ContentStorage");
        skippedObjects.add("org.pimslims.model.molecule.MolResLink");
        skippedObjects.add("org.pimslims.model.molecule.MolSeqFragment");
        skippedObjects.add("org.pimslims.model.molecule.MoleculeSysName");
        skippedObjects.add("org.pimslims.model.target.ResearchObjectiveElement");
        skippedObjects.add("org.pimslims.model.refSampleComponent.MolCompFeature");
        skippedObjects.add("org.pimslims.model.molSystem.Chain");
        skippedObjects.add("org.pimslims.model.core.AccessObject");
        skippedObjects.add("org.pimslims.model.target.TargetDbRefs");
        // Construct the ModelObjectsTest class providing log level and list of
        // the object class names that should not be created
        final ModelObjectsTest mot = new ModelObjectsTest(ModelObjectsTest.logLevelNull, skippedObjects);

        // Create desired ModelObject, providing false as the second parameter
        // means
        // that only mandatory attributes will be filled in
        // mot.constructModelObject(Molecule.class.getName(), false);

        // Change the log level to see the creation information now
        mot.setLogLevel(ModelObjectsTest.logLevelInfo);

        // Create the ModelObject and fill in all its attributes
        // mot.constructModelObject(Molecule.class.getName(), true);

        // Create the ModelObject fill in all ModelObject attributes and fill in
        // all roles. Fill in also all the attributes of all objects in roles.
        // mot.constructModelObject(Molecule.class.getName(), true, true, 1);

        // The same as above but fill in only mandatory fields of all objects
        // mot.constructModelObject(Molecule.class.getName(), false, true, 1);

        // This will not work since second false means do not create and fill in
        // the
        // objects in roles and recursion depth does not make sense here.
        // molecule = mot.constructModelObject(Molecule.class.getName(), true,
        // false, 1);

        // Create the ModelObject fill in all ModelObject attributes and fill in
        // all roles. All second level roles will also be created and filled in.
        // Fill in also all the attributes of all objects in roles.
        // molecule = mot.constructModelObject(Molecule.class.getName(), true,
        // true, 2);

        // Set up the javaClass names which should not be created.
        mot.skippedObjects.add("org.pimslims.model.core.Citation");
        mot.skippedObjects.add("org.pimslims.model.target.ResearchObjectiveElement");
        mot.skippedObjects.add("org.pimslims.model.core.AccessObject");
        mot.skippedObjects.add("org.pimslims.model.target.TargetDbRef");

        // Create the ModelObject, fill in all roles except the above.
        final ModelObject target = mot.constructModelObject(Target.class.getName(), true, true, 1);

        // Get the created and filled in role
        // Collection otherMoleculesAttrib = target.get("otherMolecules");
        // ArrayList ar = new ArrayList(otherMoleculesAttrib);
        // ModelObject mo = (ModelObject)ar.get(0);
        // Obtain the specific object
        // Molecule realmolecule = (Molecule)mo;

        // Set new log level
        mot.setLogLevel(ModelObjectsTest.logLevelDebug);

        // Reset all attributes of the ModelObject
        mot.resetValues(target, true);

        // Reset mandatory attributes of the ModelObject
        mot.resetValues(target, false);

    }

} // class end
