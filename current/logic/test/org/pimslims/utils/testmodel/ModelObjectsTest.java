/*
 * Created on 24.06.2005
 */
package org.pimslims.utils.testmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.pimslims.constraint.Constraint;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.StuffProducer;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;

/**
 * This class are designed to test the Model object creation. It fills in the attributes with randomly
 * generated values of appropriate type. Other objects (roles) which created object contains can also be
 * filled in. For instance org.pimslims.model.target.Target class has a number of attributes of different type
 * like name, whyChosen, etc and also contain other object e.g. org.pimslims.model.molecule.Molecule,
 * org.pimslims.model.target.ResearchObjectiveElement etc. These objects can be also populated. All special
 * attributes are filtered out using the following criteria: The attribute value can be set up if it is not
 * isDerived() or if it is isDerived() and isChangeable(). In case when only mandatory attributes values set
 * up are requested in addition to the above criteria isRequired() is applied. If you can create the model
 * object and populate its attributes values as well as roles with help of this class it is likely that you
 * will have no problems using this ModelObject somewhere else. But only if you filter out the model object
 * attributes using the same criteria as described above. Abstract classes could not be created. The tool
 * skips it whenever find.
 * 
 * @author Petr Troshin
 */
@Deprecated
// obsolete
public class ModelObjectsTest {

    /*
     * Obtaining the model & its writable version
     */
    private final AbstractModel model = org.pimslims.dao.ModelImpl.getModel();

    private final WritableVersion rw = this.model.getWritableVersion(AbstractModel.SUPERUSER);

    // private int objectNum = 0;
    /**
     * The full java class names of the objects which must not be created and filled in.
     */
    public ArrayList skippedObjects = null;

    /**
     * No log only errors will be displayed
     */
    public static final int logLevelNull = 1;

    /**
     * Display overall processing information print out the objects which were requested to create
     */
    public static final int logLevelInfo = 2;

    /**
     * Print out all objects which were created and overall processing information including constraints
     * values
     */
    public static final int logLevelDebug = 3;

    /*
     * Requested log level
     */
    private int logLevel = 0;

    /**
     * Construct the ModelObjectsTest instance. Provide the information on the logLevel
     * 
     * @param logLevel choose one from statically defined in this class e.g. ModelObjectsTest.logLevelInfo
     */
    public ModelObjectsTest(final int logLevel) {
        this.logLevel = logLevel;
        this.skippedObjects = new ArrayList();
    }

    /**
     * Construct the ModelObjectsTest instance. Provide the information on the logLevel and skipped objects
     * 
     * @param logLevel choose one from statically defined in this class
     * @param skippedObject ArrayList of the full java class names which should not be created e.g.
     *            "org.pimslims.model.molecule.Molecule"
     */
    public ModelObjectsTest(final int logLevel, final ArrayList skippedObject) {
        this.logLevel = logLevel;
        this.skippedObjects = skippedObject;
    }

    /**
     * Allow to change log level of the ModelObjectsTest class instance
     * 
     * @param logLevel new log level
     */
    public void setLogLevel(final int logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Construct and fill in the ModelObject with randomly generated information.
     * 
     * @param mObjectName the name of the ModelObject to create e.g. "org.pimslims.model.target.Target" the
     *            convenient way to obtain the full java class name is Target.class.getName()
     * @param fillAllAttr whether to fill in all attributes (true) or mandatory ones only (false)
     * @param fillAllRoles whether to fill in all roles (true) or mandatory ones only (false)
     * @param recursionDepth how deep fill in the roles. For instance if this is 1 and you test the Target
     *            class all roles in the Target class will be filled in. But for Molecule (as role in Target
     *            class) no roles will be filled in, only Molecule attributes will be filled in and the same
     *            is true for all other roles of Target class. If the recursionDepth is 2, all roles of Target
     *            class and all roles of the first level will be filled in. So the Molecule will be filled in
     *            in full e.g. attributes values and roles. Class Molecule has
     *            "org.pimslims.model.molecule.ChemCompHead" class as role this class will be created and its
     *            attributes filled in but not the roles of this class. Note - Mandatory roles created
     *            irrelative of fillAllRoles parameter since it is impossible to create the proper ModelObject
     *            and not fill in mandatory roles.
     * 
     *            <PRE>
     * Class: 
     * org.pimslims.model.target.Target 
     * Attributes 
     * * name
     * * whyChosen
     * * commonName
     * * etc
     * Roles of Target class (filled in if recursionDepth is 1)
     * * org.pimslims.model.molecule.Molecule (Mandatory one)
     * * * Attributes (filled in if recursionDepth is 1)
     * * * * name
     * * * * systematicName
     * * * * etc
     * * * Roles of Molecule class (filled in if recursionDepth is 2)
     * * * * org.pimslims.model.chemComp.ChemComp
     * * * * org.pimslims.model.people.Group
     * * * * org.pimslims.model.target.ResearchObjectiveElement
     * * * * etc
     * * org.pimslims.model.core.AccessObject
     * * org.pimslims.model.target.ResearchObjectiveElement
     * * etc
     * </PRE>
     * 
     *            Note that both Target and Molecule has link to the ResearchObjectiveElement. The
     *            ResearchObjectiveElement will be created twice for Target and Molecule separately. Even if
     *            the Molecule creation is requested from Target class and the Molecule itself has Target as
     *            role the new Target will be created.
     * 
     * @return ModelObject constructed ModelObject with values in attributes fields and roles
     */
    public ModelObject constructModelObject(final String mObjectName, final boolean fillAllAttr,
        final boolean fillAllRoles, final int recursionDepth) {

        try {
            if (!fillAllRoles && recursionDepth != 0) {
                throw new AssertionError("Please use appropriate constructor! ");
            }
        } catch (final AssertionError aer) {
            throw new RuntimeException(aer);
        }
        // Make model object
        ModelObject mobject = null;
        // Make model object properties map.
        final HashMap objectAttr = new HashMap();
        // Get model object metaclass & metaattribute in order to find out
        // what should be filled in
        final MetaClass mc = this.model.getMetaClass(mObjectName);
        final Map metaAttr = mc.getAttributes();

        // Fill in attributes of the particular role.
        final String fill = fillAllAttr ? "ALL" : "mandatory";
        this.printLog("START FILL IN " + fill + " ATTRIBUTES OF THE OBJECT " + mObjectName + "\n",
            ModelObjectsTest.logLevelInfo);
        this.fillAttributes(metaAttr, objectAttr, fillAllAttr);

        // Get model object roles
        final Map roles = mc.getMetaRoles();

        if (mc.isAbstract()) {
            this.printLog("Cannot create an abstract class skipping it.. ", ModelObjectsTest.logLevelInfo);
            return null;
        }

        for (final Iterator iter = roles.keySet().iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            // Make sure that property key is not null
            assert element.length() != 0;
            // Get information on metarole
            final MetaRole mr = (MetaRole) roles.get(element);

            final String javaClass = mr.getOtherMetaClass().getJavaClass().getName();

            // Test whether this object should not be created and its properties
            // filled in.
            if (this.isSkippedObject(javaClass)) {
                this.printLog("SKIP this role cause its in the skip list " + javaClass + "... ",
                    ModelObjectsTest.logLevelInfo);
                continue;
            }

            final String roleName = mr.getRoleName();
            this.printLog("Current recursion depth: " + recursionDepth, ModelObjectsTest.logLevelDebug);
            this.printLog("Next role name: " + element + "\nrole class: "
                + mr.getOtherMetaClass().getJavaClass(), ModelObjectsTest.logLevelDebug);
            // Create and fill in object attributes if requested.
            // Mandatory roles fills in anyway
            // Stop when recursion depth is 0
            if (fillAllRoles && recursionDepth > 0) {
                final String cardHi = mr.getHigh() == -1 ? "*" : Integer.toString(mr.getHigh());
                this.printLog("-------- Attempting to fill ALL Roles! ---------\n" + "achieved role is "
                    + javaClass + "\n" + "cardinality value is " + mr.getLow() + ".." + cardHi + "\n",
                    ModelObjectsTest.logLevelInfo);

                final ModelObject modelObjinRole =
                    this.constructModelObject(javaClass, fillAllAttr, fillAllRoles, recursionDepth - 1);
                objectAttr.put(roleName, modelObjinRole);
            } else {
                // Fill in role only if its mandatory
                if (mr.getLow() > 0) {
                    this.printLog(" Mandatory Role found. Cardinality is " + mr.getLow(),
                        ModelObjectsTest.logLevelInfo);
                    final ModelObject modelObjinRole =
                        this.constructModelObject(javaClass, fillAllAttr, fillAllRoles, 0);
                    objectAttr.put(roleName, modelObjinRole);
                }
                this.printLog("Skipping role creation since recursion depth is achieved ",
                    ModelObjectsTest.logLevelDebug);
            }
        }
        try {
            // Skip the object creation and filling in if requested
            if (!this.isSkippedObject(mc.getMetaClassName())) {

                this.printLog(
                    "Last log record before actual object creation attempt " + mc.getMetaClassName(),
                    ModelObjectsTest.logLevelInfo);
                // Create the model object with prefilled attributes.
                mobject = this.rw.create(mc.getJavaClass(), objectAttr);
            }
            this.printLog("++++++++  Object " + mc.getJavaClass() + " creation done +++++++",
                ModelObjectsTest.logLevelInfo);
            this.printLog("---------------------------------------------------------------------\n",
                ModelObjectsTest.logLevelInfo);
            if (this.logLevel == ModelObjectsTest.logLevelDebug) {
                this.printHm(mc.getJavaClass().getName(), objectAttr);
            }
        } catch (final AccessException aex) {
            throw new RuntimeException(aex);
        } catch (final ConstraintException cex) {
            throw new RuntimeException(cex);
        }

        return mobject;
    }

    /**
     * Print the messages to the standard output depending on the current logLevel
     * 
     * @param message mesage to print
     * @param messagelogLevel log level
     */
    private void printLog(final String message, final int messagelogLevel) {
        if (messagelogLevel <= this.logLevel) {
            System.out.println(message);
        }
    }

    /**
     * When there is no aim to fill in the roles use this method.
     * 
     * @param mObjectName ModelObject full java class name of object to be created e.g.
     *            "org.pimslims.model.target.Target"
     * @param fillAllAttr - fill in all (true) or only mandatory attributes (false)
     * @return ModelObject
     */
    public ModelObject constructModelObject(final String mObjectName, final boolean fillAllAttr) {
        return this.constructModelObject(mObjectName, fillAllAttr, false, 0);
    }

    /**
     * Fill in Model object attributes.
     * 
     * @param metaAttr - pimslims.api.MetaAttrubute
     * @param objectAttr empty hashmap to fill in
     * @param fillAllAttr - fill in all (true) or only mandatory attributes (false)
     */
    private void fillAttributes(final Map metaAttr, final HashMap objectAttr, final boolean fillAllAttr) {
        for (final Iterator iter = metaAttr.keySet().iterator(); iter.hasNext();) {
            boolean set = false;
            final String element = (String) iter.next();
            final MetaAttribute mr = (MetaAttribute) metaAttr.get(element);
            assert element.length() != 0;
            final Constraint constr = mr.getConstraint();

            // Must treat below as List for the ModelObject construction but as
            // String[]
            // for the setXXX() methods of the ModelObject
            String valueType = mr.getType().getName();
            if (valueType.equalsIgnoreCase(String[].class.getName())) {
                valueType = "java.util.ArrayList";
            }

            if (fillAllAttr) { // (
                if (!mr.isDerived() || (mr.isDerived() && mr.isChangeable())) {
                    this.printLog("Next Constraint ! ", ModelObjectsTest.logLevelDebug);
                    this.printLog("Constraint values: " + mr.getConstraint().toString(),
                        ModelObjectsTest.logLevelDebug);
                    this.printLog("Attribute name which is under constraint: " + element,
                        ModelObjectsTest.logLevelDebug);
                    this.printLog("Attribute type from MetaAttribute: " + mr.getType().getName() + "\n",
                        ModelObjectsTest.logLevelDebug);
                    set = this.setConstraintValues(constr, objectAttr, element);
                    if (!set) {
                        this.setValues(valueType, mr.getLength(), objectAttr, element);
                    }
                }
            } else {
                if (mr.isRequired() && (!mr.isDerived() || (mr.isDerived() && mr.isChangeable()))) {
                    this.printLog("Next Constraint ! ", ModelObjectsTest.logLevelDebug);
                    this.printLog("Constraint values: " + mr.getConstraint().toString(),
                        ModelObjectsTest.logLevelDebug);
                    this.printLog("Attribute name which is under constraint: " + element,
                        ModelObjectsTest.logLevelDebug);
                    this.printLog("Attribute type: " + mr.getType().getName() + "\n",
                        ModelObjectsTest.logLevelDebug);
                    set = this.setConstraintValues(constr, objectAttr, element);
                    if (!set) {
                        this.setValues(valueType, mr.getLength(), objectAttr, element);
                    }
                }
            }
            // System.out.println("key " + element + " value " + mr.getType());
            // System.out.println(" derived ? " + mr.isDerived());
            // System.out.println(" required ? " + mr.isRequired());
            // System.out.println(" changeable ? " + mr.isChangeable());
            // System.out.println(" Length ? " + mr.getLength());
        }
    }

    /**
     * Test whether or not the object should be created
     * 
     * @param javaClassName full java class name
     * @return true if the object should be created otherwise false
     */
    private boolean isSkippedObject(final String javaClassName) {
        for (final Iterator iter = this.skippedObjects.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            if (element.equalsIgnoreCase(javaClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set values for the constrained fields
     * 
     * @param constr instance of ccp.api.metamodel.Constraint class
     * @param objectAttr - hashmap of ModelObject attributes to fill in
     * @param element - the name of the ModelObject attribute to set up
     * @return the correct value for the ModelObject attribute (element)
     */
    private boolean setConstraintValues(final Constraint constr, final HashMap objectAttr,
        final String element) {
        boolean set = false;
        Object possibleValue = null;
        if (constr instanceof org.pimslims.constraint.Constraint.EnumerationConstraint) {
            final Collection ar =
                ((org.pimslims.constraint.Constraint.EnumerationConstraint) constr).allowedValues;
            final Random r = new Random();
            final int rnum = r.nextInt(ar.size());
            final Iterator iter = ar.iterator();
            for (int i = rnum; i < ar.size(); i++) {
                possibleValue = iter.next();
            }
            this.printLog("POSSIBLE VALUES: " + possibleValue, ModelObjectsTest.logLevelDebug);
            objectAttr.put(element, possibleValue);
            set = true;
        }
        // if(constr instanceof org.pimslims.constraint.Constraint.) {
        return set;
    }

    /**
     * Set value of the attribute
     * 
     * @param mr
     * @param objectAttr
     * @param attrName
     */
    private void setValues(final String valueType, final int valueLength, final HashMap objectAttr,
        final String attrName) {
        final Object value = this.getValue(valueType, valueLength);
        objectAttr.put(attrName, value);
    }

    /**
     * Get the random values of appropriate type for the model object field
     * 
     * @param javaType the Java type of the value that should be generated
     * @param fieldLength the maximum length of a generated string. If the fieldLength equal 0 this means that
     *            the string has no maximum length limit and for uniform method accessing be limited to 300
     *            characters.
     * @return value object
     */
    private Object getValue(final String javaType, int fieldLength) {
        Object value = "";

        fieldLength = fieldLength == 0 ? 300 : fieldLength;
        if (javaType.equals("java.lang.String")) {
            value = StuffProducer.getString(fieldLength);
        } else if (javaType.equals("java.lang.Boolean")) {
            value = StuffProducer.getBoolean();
        } else if (javaType.equals("java.lang.Long")) {
            value = StuffProducer.getLong();
        } else if (javaType.equals("java.lang.Integer")) {
            value = StuffProducer.getInt();
        } else if (javaType.equals("java.lang.Float")) {
            value = StuffProducer.getFloat();
        } else if (javaType.equals("java.lang.Double")) {
            value = StuffProducer.getDouble();
            // Must treat below as List for the ModelObject construction but
            // as String[]
            // for the setXXX() methods of the ModelObject
        } else if (javaType.equals(String[].class.getName())) { // "[Ljava.lang.String"
            value = StuffProducer.getStringArray(fieldLength);
        } else if (javaType.equals("java.util.ArrayList")) {
            value = StuffProducer.getCollection(fieldLength);
        } else if (javaType.equals("java.util.Map")) {
            value = StuffProducer.getMap();
        } else if (javaType.equals(Calendar.class.getName())) {
            value = Calendar.getInstance();
        } else {
            throw new UnsupportedOperationException("Data generation for class " + javaType
                + " is unsupported ");
        }

        return value;
    }

    /**
     * Reset values of the object attributes (but not the roles)
     * 
     * @param mobject pass the ModelObject object
     * @param resetAllAttr whether or not reset all values (if true) or reset only mandatory ones (if false).
     */
    public void resetValues(final ModelObject mobject, final boolean resetAllAttr) {

        final MetaClass mc = mobject.get_MetaClass();
        final Map metaAttr = mc.getAttributes();
        final String what = resetAllAttr ? "ALL" : "mandatory";
        final String javaClass = mobject.get_MetaClass().getMetaClassName();
        this.printLog("RESETTING " + what + " VALUES OF THE OBJECT " + javaClass,
            ModelObjectsTest.logLevelInfo);
        for (final Iterator iter = metaAttr.keySet().iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            final MetaAttribute mr = (MetaAttribute) metaAttr.get(element);
            final Object value = this.getValue(mr.getType().getName(), mr.getLength());
            final Object oldvalue = mobject.get_Value(element);

            this.printLog("Changing value of " + element + "\nOLDVALUE: " + oldvalue + "\nOLDVALUE TYPE: "
                + (oldvalue != null ? oldvalue.getClass().getName() : "unknown")
                + "\ntype of the attribute: " + value.getClass().getName() + " the value should be of type "
                + mr.getType().getName() + "\n", ModelObjectsTest.logLevelDebug);
            // Do not set the new value if the field is restricted by the
            // constraint type below.
            if (mr.getConstraint() instanceof org.pimslims.constraint.Constraint.EnumerationConstraint) {
                continue;
            }

            try {
                if (resetAllAttr) {
                    if (mr.isChangeable()) {
                        mobject.set_Value(element, value);
                    }
                } else {
                    if (mr.isRequired() && mr.isChangeable()) {
                        mobject.set_Value(element, value);
                    }
                }
            } catch (final AccessException aex) {
                throw new RuntimeException(aex);
            } catch (final ConstraintException cex) {
                throw new RuntimeException(cex);
            }
            // printLog(" derived ? " + mr.isDerived(), logLevelDebug);
            // printLog(" required ? " + mr.isRequired(), logLevelDebug);
            // printLog(" changeable ? " + mr.isChangeable(), logLevelDebug);
            // printLog(" Length ? " + mr.getLength(), logLevelDebug);
        }
        if (this.logLevel >= ModelObjectsTest.logLevelInfo) {
            this.printHm(javaClass, mobject.get_Values());
        }
    }

    /**
     * Print out object attributes
     * 
     * @param javaClassName Java class name of the ModelObject
     * @param hm object attributes Map
     */
    protected void printHm(final String javaClassName, final Map hm) {
        /* System.out.println("CREATED OBJECT " + javaClassName + " CONTENTS:
        // ");
        for (Iterator iter = hm.keySet().iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            Object o = hm.get(element);
            // System.out.println("ATTRIBUTE NAME: " + element + "\n VALUE: " +
            // o + "\n");
        } */
    }

} // ModelObjectsTest class end
