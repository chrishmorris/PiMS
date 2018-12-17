/**
 * org.pimslims.metamodel ExampleFactory.java
 * 
 * @date 18-Oct-2006 08:45:29
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.test;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaAttributeImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.AbstractSample;

/**
 * ExampleFactory: create different example POJO for testing
 * 
 */
public abstract class POJOFactory {

    private static List<String> hookList = new LinkedList<String>();

    private static int i = 0;

    /**
     * @return Returns the i.
     */
    private static synchronized int getNext() {
        i = i + 1;
        return i;
    }

    static final AbstractModel model = ModelImpl.getModel();

    /**
     * Create a mock object with minimum attributes/roles only, related role will have minimum attributes as
     * well with a provided attribute/role
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T create(WritableVersion wv, Class javaClass, String propertyName,
        Object value) throws AccessException, ConstraintException {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(propertyName, value);
        T modelObject = (T) createModelObject(wv, javaClass, false, attributes);

        return modelObject;
    }

    /**
     * Create a mock object with minimum attributes/roles only, related role will have minimum attributes as
     * well
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T create(WritableVersion wv, Class javaClass)
        throws AccessException, ConstraintException {

        T modelObject = (T) createModelObject(wv, javaClass, false, Collections.EMPTY_MAP);

        return modelObject;
    }

    /**
     * Create a mock object with minimum roles but all attributes, related role will have minimum attributes
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum roles but all attributes
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T createWithFullAttributes(WritableVersion wv, Class javaClass)
        throws AccessException, ConstraintException {

        T modelObject = (T) createModelObject(wv, javaClass, true, Collections.EMPTY_MAP);

        return modelObject;
    }

    /**
     * @param <T>
     * @param wv
     * @param javaClass
     * @param attributeMap
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static <T extends ModelObject> T createModelObject(WritableVersion wv, Class javaClass,
        boolean isFullCreate, Map<String, Object> attributeMap) throws AccessException, ConstraintException {
        Map<String, Object> attributes = new HashMap<String, Object>(attributeMap);
        // attributes and roles to create the modelobject will be here
        // System.out.println("Create " + javaClass.getName());
        MetaClass metaClass = model.getMetaClass(javaClass.getName());
        while (metaClass.isAbstract()) {
            Iterator iterator = metaClass.getSubtypes().iterator();
            if (!iterator.hasNext())
                return null;
            metaClass = (MetaClass) iterator.next();
            javaClass = metaClass.getJavaClass();
        }

        // generate value for required attributes
        for (String attributeName : metaClass.getAttributes().keySet())
            if (!attributes.keySet().contains(attributeName)) {
                MetaAttribute metaAttribute = metaClass.getAttributes().get(attributeName);
                if (metaAttribute == null) {
                    continue;
                } else if (metaAttribute.isDerived()) {
                    continue;
                } else if (javaClass == org.pimslims.model.experiment.Experiment.class
                    && metaAttribute.getName().equalsIgnoreCase("isLocked")) {
                    attributes.put(attributeName, false);
                } else if (metaAttribute.isRequired() || isFullCreate) {
                    Object value = null;
                    if (metaAttribute.getType() != List.class) {
                        value = createRandomValue(metaAttribute.getType(), attributeName);
                    } else
                        value = createRandomListValue(((MetaAttributeImpl) metaAttribute).getGenericType());
                    attributes.put(attributeName, value);
                    if (value instanceof String)
                        if (metaAttribute.getLength() <= ((String) value).length()) {
                            // generated string could be too long
                            attributes.put(attributeName, getString());
                        }
                }
            }

        // generate object for required role
        for (String roleName : metaClass.getMetaRoles().keySet())
            if (!attributes.keySet().contains(roleName)) {
                MetaRole metaRole = metaClass.getMetaRoles().get(roleName);
                if (metaRole.isDerived())
                    continue;
                if (metaRole.isRequired() || isFullCreate) {

                    Object value = null;
                    if (attributes.containsKey(LabBookEntry.PROP_ACCESS)
                        & LabBookEntry.class.isAssignableFrom(metaRole.getOtherMetaClass().getJavaClass()))
                        //pass owner to sub object
                        value =
                            create(wv, metaRole.getOtherMetaClass().getJavaClass(), LabBookEntry.PROP_ACCESS,
                                attributes.get(LabBookEntry.PROP_ACCESS));
                    else
                        value = create(wv, metaRole.getOtherMetaClass().getJavaClass());
                    if (metaRole.getHigh() > 1 || metaRole.getHigh() < 0)
                        attributes.put(roleName, Collections.singleton(value));
                    else
                        attributes.put(roleName, value);
                }
            }

        if (javaClass.getName().endsWith("Citation"))
            attributes.put(org.pimslims.model.core.Citation.PROP_TITLE, getString());
        else if (javaClass.getName().equalsIgnoreCase(User.class.getName())
            && !attributeMap.keySet().contains(User.PROP_USERGROUPS)) {
            LabNotebook owner = create(wv, LabNotebook.class);
            UserGroup usergroup1 = create(wv, UserGroup.class);
            new Permission(wv, PIMSAccess.READ, owner, usergroup1);
            new Permission(wv, PIMSAccess.CREATE, owner, usergroup1);
            new Permission(wv, PIMSAccess.UPDATE, owner, usergroup1);
            new Permission(wv, PIMSAccess.DELETE, owner, usergroup1);
            attributes.put(User.PROP_USERGROUPS, usergroup1);

        }
        // System.out.println("-Creating " + javaClass + " with attributes:" + attributes.toString());
        T modelObject = null;
        try {
            modelObject = (T) wv.create(javaClass, attributes);
        } catch (RuntimeException e) {
            System.out.println("Failed to create " + javaClass + " with attributes:" + attributes);
            throw e;
        }
        // System.out.println("+Created " + javaClass);
        addHook(modelObject);

        return modelObject;
    }

    private static <T extends ModelObject> T addHook(T mo) {
        hookList.add(0, mo.get_Hook());
        return mo;
    }

    public static void removeTestingRecords(String testCaseName) {

        doRemoveTestingRecords(testCaseName, hookList);
    }

    public static void doRemoveTestingRecords(String testCaseName, Collection<String> hookList) {
        WritableVersion wv = null;

        // first round delete - needs test cases to delete where there are constraints.
        // Or can ignore exceptions. In that case, it is best to silence the log as well.

        //Category loggerCat = Logger.getLogger(Hibernate.class).getParent();
        //Level oldLevel = loggerCat.getLevel();
        //loggerCat.setLevel(Level.OFF);// it seems setLevel only work on Parent

        for (String hook : new LinkedList<String>(hookList)) {
            try {
                wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                ModelObject mo = wv.get(hook);
                if (mo != null) {
                    mo.delete();
                    // throw new RuntimeException("when does this happen?");
                }
                wv.commit();
                hookList.remove(hook);
            } catch (ModelException ex) {
                // it is ok, maybe deleteable in second round
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }

        //loggerCat.setLevel(oldLevel);
        // second round delete
        for (String hook : new LinkedList<String>(hookList)) {
            try {
                wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                ModelObject mo = wv.get(hook);
                hookList.remove(hook);
                if (mo != null)
                    mo.delete();
                wv.commit();
                hookList.remove(hook);
            } catch (ModelException ex) {
                //ex.printStackTrace();
                System.err.print(testCaseName + ": Can not delete testing Object " + hook + " as "
                    + ex.getMessage());
            } finally {
                if (wv != null && !wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
    }

    /*********************************************************************************************************
     * Methods to create random value for testing
     * **********************************************************************
     */
    /**
     * @param genericType
     * @return
     */
    private static Object createRandomListValue(Type genericType) {
        if (!genericType.toString().contains("List"))
            throw new RuntimeException("Unknow type for createRandomListValue():" + genericType);
        if (genericType.toString().contains("java.lang.String"))
            return Collections.singletonList(getString());
        else if (genericType.toString().contains("java.lang.Integer"))
            return Collections.singletonList(getInt());
        else if (genericType.toString().contains("java.lang.Long"))
            return Collections.singletonList(getLong());
        else if (genericType.toString().contains("java.sql.Timestamp"))
            return Collections.singletonList(getDate());
        else if (genericType.toString().contains("java.sql.Boolean"))
            return Collections.singletonList(getBoolean());
        else if (genericType.toString().contains("java.sql.Float"))
            return Collections.singletonList(getFloat());

        throw new RuntimeException("Please add new function to create:" + genericType);
    }

    /**
     * @param type
     * @param attributeName
     * @return
     */
    private static Object createRandomValue(Class type, String attributeName) {
        //some must_be value
        if (attributeName.equalsIgnoreCase("extensionType"))
            return "reverse";
        if (attributeName.equalsIgnoreCase("opType"))
            return "create";
        if (attributeName.equalsIgnoreCase("status"))
            return "To_be_run";
        if (attributeName.equalsIgnoreCase("molType"))
            return "DNA";
        if (attributeName.equalsIgnoreCase("paramType"))
            return "Float";
        if (attributeName.equalsIgnoreCase("concentrationUnit"))
            return "kg/m3";
        if (attributeName.equalsIgnoreCase("protocol"))
            return "file";
        if (attributeName.equalsIgnoreCase("classification"))
            return "local";
        if (attributeName.equalsIgnoreCase("amountUnit") || attributeName.equalsIgnoreCase("unit"))
            return "kg";
        if (type == String.class)
            return getString(attributeName);
        else if (type == int.class || type == Integer.class)
            return getInt();
        else if (type == long.class || type == Long.class)
            return getLong();
        else if (type == java.sql.Timestamp.class)
            return getDate();
        else if (type == java.lang.Boolean.class)
            return getBoolean();
        else if (type == java.lang.Float.class)
            return getFloat();
        else if (type == java.lang.Double.class)
            return getDouble();
        else if (type == java.util.Calendar.class)
            return getDate();
        else if (attributeName.equalsIgnoreCase("SameAs"))
            return Collections.singletonList("http://example.org/resource");
        else
            throw new RuntimeException("Please add new function to create:" + type.getName() + " for "
                + attributeName);

    }

    /**
     * @return
     */
    private static Object getDouble() {
        // was getNext();
        return new Double(getInt() / 1000000.00);
    }

    private static Float getFloat() {
        // was getNext();
        return new Float(getInt() / 1000000.00);
    }

    private static Object getBoolean() {
        // was getNext();
        return new java.util.Random().nextBoolean();
    }

    private static String getString(String name) {
        // was getNext();
        return name + getNext() + " " + System.currentTimeMillis();
    }

    private static String getString() {
        // was getNext();
        return getNext() + "" + System.currentTimeMillis();
    }

    private static Long getLong() {
        // was getNext();
        return getNext() + System.currentTimeMillis();
    }

    private static Integer getInt() {
        // was getNext();
        return (new java.util.Random().nextInt(getNext()) + 1); //some values can not be 0
    }

    static Calendar now = java.util.Calendar.getInstance();

    private static java.util.Calendar getDate() {
        Calendar date = java.util.Calendar.getInstance();
        while (date.equals(now)) {
            date = java.util.Calendar.getInstance();
        }
        now = date;
        return date;
    }

    /*********************************************************************************************************
     * Methods to create specific data model objects for testing
     * **********************************************************************
     */
    // Target.Target
    public static Map<String, Object> getAttrTarget() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.Target.PROP_NAME, "testName" + getString());
        attributes.put(org.pimslims.model.target.Target.PROP_WHYCHOSEN, "testWhyChosen" + getString());
        return attributes;
    }

    public static Map<String, Object> getAllAttrTarget(WritableVersion wv) throws ConstraintException {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.Target.PROP_NAME, "testName" + getString());
        attributes.put(org.pimslims.model.target.Target.PROP_WHYCHOSEN, "testWhyChosen" + getString());
        attributes.put(org.pimslims.model.target.Target.PROP_PROTEIN, createProtein(wv));
        return attributes;
    }

    public static org.pimslims.model.target.Target createTarget(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.target.Target(wv, getAllAttrTarget(wv)));
    }

    public static org.pimslims.model.target.Target createTarget(WritableVersion wv,
        org.pimslims.model.molecule.Molecule molComponent) throws ConstraintException {
        Map<String, Object> allAttrTarget = new HashMap<String, Object>(getAttrTarget());
        allAttrTarget.put(org.pimslims.model.target.Target.PROP_PROTEIN, molComponent);
        return addHook(new org.pimslims.model.target.Target(wv, allAttrTarget));
    }

    @Deprecated
    // Target.Project no longer used
    public static Map<String, Object> getAttrProject() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.Project.PROP_SHORTNAME, "testShortName" + getString());
        attributes.put(org.pimslims.model.target.Project.PROP_COMPLETENAME, "testCompleteName" + getString());
        return attributes;
    }

    public static org.pimslims.model.target.TargetGroup createTargetGroup(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.target.TargetGroup(wv, getAttrTargetGroup()));
    }

    // Target.Milestone
    public static Map<String, Object> getAttrMilestone() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.Milestone.PROP_DATE, getDate());
        return attributes;
    }

    public static org.pimslims.model.target.Milestone createMilestone(WritableVersion wv,
        org.pimslims.model.target.Target target) throws ConstraintException {
        Map<String, Object> allAttrMilestone = new HashMap<String, Object>(getAttrMilestone());
        allAttrMilestone.put(org.pimslims.model.target.Milestone.PROP_TARGET, target);
        allAttrMilestone.put(org.pimslims.model.target.Milestone.PROP_STATUS, createStatus(wv));
        return addHook(new org.pimslims.model.target.Milestone(wv, allAttrMilestone));
    }

    public static org.pimslims.model.target.Milestone createMilestone(WritableVersion wv,
        org.pimslims.model.target.Target target, org.pimslims.model.reference.TargetStatus status)
        throws ConstraintException {
        Map<String, Object> allAttrMilestone = new HashMap<String, Object>(getAttrMilestone());
        allAttrMilestone.put(org.pimslims.model.target.Milestone.PROP_TARGET, target);
        allAttrMilestone.put(org.pimslims.model.target.Milestone.PROP_STATUS, status);
        return addHook(new org.pimslims.model.target.Milestone(wv, allAttrMilestone));
    }

    // Target.Status
    public static Map<String, Object> getAttrStatus() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.TargetStatus.PROP_NAME, "Selected" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.TargetStatus createStatus(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.TargetStatus(wv, getAttrStatus()));
    }

    public static org.pimslims.model.reference.TargetStatus createStatus(WritableVersion wv, String string)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.TargetStatus(wv, string));
    }

    // Annotation.Annotation
    public static Map<String, Object> getAttrAnnotation() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.core.Annotation.PROP_NAME, "testName" + getString());
        return attributes;
    }

    // DbRef.DbRef
    public static Map<String, Object> getAttrDbRef() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes
            .put(org.pimslims.model.core.ExternalDbLink.PROP_ACCESSION_NUMBER, "testCode" + getString());
        return attributes;
    }

    // DbRef.DbName
    public static Map<String, Object> getAttrDbName() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.Database.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.Database createDbName(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.Database(wv, getAttrDbName()));
    }

    // ExpBlueprint.ExpBlueprint
    public static Map<String, Object> getAttrExpBlueprint() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.ResearchObjective.PROP_COMMONNAME, "testCommonName"
            + getString());
        attributes.put(org.pimslims.model.target.ResearchObjective.PROP_WHYCHOSEN, "testWhyChosen"
            + getString());
        return attributes;
    }

    public static org.pimslims.model.target.ResearchObjective createExpBlueprint(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.target.ResearchObjective(wv, getAttrExpBlueprint()));
    }

    // ExpBlueprint.BlueprintComponent
    public static Map<String, Object> getAttrBlueprintComponent() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.target.ResearchObjectiveElement.PROP_COMPONENTTYPE,
            "testComponentType");
        attributes.put(org.pimslims.model.target.ResearchObjectiveElement.PROP_WHYCHOSEN, "testWhyChosen"
            + getString());
        return attributes;
    }

    public static org.pimslims.model.target.ResearchObjectiveElement createBlueprintComponent(
        WritableVersion wv) throws ConstraintException {
        Map<String, Object> allAttrBlueprintComponent =
            new HashMap<String, Object>(getAttrBlueprintComponent());
        allAttrBlueprintComponent
            .put(org.pimslims.model.target.ResearchObjectiveElement.PROP_RESEARCHOBJECTIVE,
                createExpBlueprint(wv));
        return addHook(new org.pimslims.model.target.ResearchObjectiveElement(wv, allAttrBlueprintComponent));
    }

    // Taxonomy.NaturalSource
    public static Map<String, Object> getAttrNaturalSource() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.Organism.PROP_NAME, "testOrganismName" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.Organism createNaturalSource(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.Organism(wv, getAttrNaturalSource()));
    }

    // People.Organisation
    public static Map<String, Object> getAttrOrganisation() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.people.Organisation.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.people.Organisation createOrganisation(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.people.Organisation(wv, getAttrOrganisation()));
    }

    // People.Group
    public static org.pimslims.model.people.Group createGroup(WritableVersion wv,
        org.pimslims.model.people.Organisation org) throws ConstraintException {
        return addHook(new org.pimslims.model.people.Group(wv, org));
    }

    // People.PersonInGroup
    public static org.pimslims.model.people.PersonInGroup createPersonInGroup(WritableVersion wv,
        org.pimslims.model.people.Group group, org.pimslims.model.people.Person person)
        throws ConstraintException {
        return addHook(new org.pimslims.model.people.PersonInGroup(wv, group, person));
    }

    // People.Person
    public static Map<String, Object> getAttrPerson() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.people.Person.PROP_FAMILYNAME, "testFamilyName");
        attributes.put(org.pimslims.model.people.Person.PROP_GIVENNAME, "testGivenName");
        attributes.put(org.pimslims.model.people.Person.PROP_TITLE, "testTitle" + getString());
        return attributes;
    }

    public static org.pimslims.model.people.Person createPerson(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.people.Person(wv, getAttrPerson()));
    }

    // RefSampleComponent.MolComponent
    public static Map<String, Object> getAttrProtein() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.molecule.Molecule.PROP_MOLTYPE, "protein");
        String name = "testName" + getString();
        attributes.put(Substance.PROP_NAME, name);
        return attributes;
    }

    public static org.pimslims.model.molecule.Molecule createProtein(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.molecule.Molecule(wv, getAttrProtein()));
    }

    // Sample.RefSample
    public static Map<String, Object> getAttrRefSample() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbstractSample.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.sample.RefSample createRefSample(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.sample.RefSample(wv, getAttrRefSample()));
    }

    // Sample.Sample
    public static Map<String, Object> getAttrSample() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbstractSample.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.sample.Sample createSample(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.sample.Sample(wv, getAttrSample()));
    }

    public static org.pimslims.model.sample.Sample createSample(WritableVersion wv,
        org.pimslims.model.reference.SampleCategory sampleCategory) throws ConstraintException {
        Map<String, Object> allAttrSample = new HashMap<String, Object>(getAttrSample());
        allAttrSample.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sampleCategory));
        return addHook(new org.pimslims.model.sample.Sample(wv, allAttrSample));
    }

    // Sample.SampleCategory
    public static Map<String, Object> getAttrSampleCategory() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.SampleCategory.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.SampleCategory createSampleCategory(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.SampleCategory(wv, getAttrSampleCategory()));
    }

    // Sample.SampleComponent
    public static org.pimslims.model.sample.SampleComponent createSampleComponent(WritableVersion wv,
        org.pimslims.model.molecule.Substance refComponent,
        org.pimslims.model.sample.AbstractSample abstractSample) throws ConstraintException {
        return addHook(new org.pimslims.model.sample.SampleComponent(wv, refComponent, abstractSample));
    }

    // RiskAssessment.HazardPhrase
    public static Map<String, Object> getAttrHazardPhrase() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.HazardPhrase.PROP_CLASSIFICATION, "local");
        attributes.put(org.pimslims.model.reference.HazardPhrase.PROP_CODE, "testCode" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.HazardPhrase createHazardPhrase(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.HazardPhrase(wv, getAttrHazardPhrase()));
    }

    // Holder.Holder
    public static Map<String, Object> getAttrHolder() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbstractHolder.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.holder.Holder createHolder(WritableVersion wv)
        throws ConstraintException {
        Map<String, Object> allAttrHolder = new HashMap<String, Object>(getAttrHolder());
        allAttrHolder.put(AbstractHolder.PROP_HOLDERCATEGORIES,
            Collections.singleton(createHolderCategory(wv)));
        return addHook(new org.pimslims.model.holder.Holder(wv, allAttrHolder));
    }

    // Holder.HolderCategory
    public static Map<String, Object> getAttrHolderCategory() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.HolderCategory.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.HolderCategory createHolderCategory(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.HolderCategory(wv, getAttrHolderCategory()));
    }

    // Experiment.OutputSample
    public static org.pimslims.model.experiment.OutputSample createOutputSample(WritableVersion wv,
        org.pimslims.model.experiment.Experiment exp) throws ConstraintException {
        return addHook(new org.pimslims.model.experiment.OutputSample(wv, exp));
    }

    // Experiment.InputSample
    public static org.pimslims.model.experiment.InputSample createInputSample(WritableVersion wv,
        org.pimslims.model.experiment.Experiment exp) throws ConstraintException {
        return addHook(new org.pimslims.model.experiment.InputSample(wv, exp));
    }

    // Experiment.Experiment
    public static Map<String, Object> getAttrExperiment() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.experiment.Experiment.PROP_NAME, "testName" + getString());
        attributes.put(org.pimslims.model.experiment.Experiment.PROP_STARTDATE, getDate());
        attributes.put(org.pimslims.model.experiment.Experiment.PROP_ENDDATE, getDate());
        return attributes;
    }

    public static org.pimslims.model.experiment.Experiment createExperiment(WritableVersion wv)
        throws ConstraintException {
        Map<String, Object> allAttrExperiment = new HashMap<String, Object>(getAttrExperiment());
        allAttrExperiment.put(org.pimslims.model.experiment.Experiment.PROP_EXPERIMENTTYPE,
            createExperimentType(wv));
        return addHook(new org.pimslims.model.experiment.Experiment(wv, allAttrExperiment));
    }

    // Experiment.Parameter
    public static org.pimslims.model.experiment.Parameter createParameter(WritableVersion wv,
        org.pimslims.model.experiment.Experiment exp) throws ConstraintException {
        return addHook(new org.pimslims.model.experiment.Parameter(wv, exp));
    }

    public static org.pimslims.model.experiment.Parameter createParameter(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.experiment.Parameter(wv, createExperiment(wv)));
    }

    // Protocol.ExperimentType
    public static Map<String, Object> getAttrExperimentType() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.reference.ExperimentType.PROP_NAME, "testName" + getString());
        return attributes;
    }

    public static org.pimslims.model.reference.ExperimentType createExperimentType(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.reference.ExperimentType(wv, getAttrExperimentType()));
    }

    // Protocol.Protocol
    public static org.pimslims.model.protocol.Protocol createProtocol(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.protocol.Protocol(wv, "testName" + getString(),
            createExperimentType(wv)));
    }

    // Protocol.RefInputSample
    public static org.pimslims.model.protocol.RefInputSample createRefInputSample(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.protocol.RefInputSample(wv, createSampleCategory(wv),
            createProtocol(wv)));
    }

    // Protocol.ParameterDefinition
    public static org.pimslims.model.protocol.ParameterDefinition createParameterDefinition(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.protocol.ParameterDefinition(wv, "pdname" + getString(),
            "String", createProtocol(wv)));
    }

    // Protocol.WorkflowItem
    public static org.pimslims.model.reference.WorkflowItem createWorkflowItem(WritableVersion wv,
        org.pimslims.model.reference.ExperimentType expType) throws ConstraintException {
        return addHook(new org.pimslims.model.reference.WorkflowItem(wv, expType));
    }

    // Citation.BookCitation
    public static Map<String, Object> getAttrBookCitation() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.core.BookCitation.PROP_BOOKTITLE, "testBookTitlepims handbook");
        attributes.put(org.pimslims.model.core.BookCitation.PROP_ISBN, "TestIsbn" + getString());
        attributes.put(Citation.PROP_TITLE, "testTitle");
        return attributes;
    }

    public static Map<String, Object> getBadAttrBookCitation() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.core.BookCitation.PROP_BOOKTITLE, "testBookTitlepims handbook");
        attributes.put(org.pimslims.model.core.BookCitation.PROP_ISBN, "TestIsbn" + getString());
        return attributes;
    }

    public static org.pimslims.model.core.BookCitation createBookCitation(WritableVersion wv)
        throws ConstraintException {
        try {
            return (BookCitation) addHook(create(wv, BookCitation.class));
        } catch (AccessException e) {
            throw new RuntimeException(e);
        }
    }

    // AccessControl.User
    public static final String USERNAME = "testName" + getString();

    public static Map<String, Object> getAttrUser() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.accessControl.User.PROP_NAME, USERNAME);
        return attributes;
    }

    public static org.pimslims.model.accessControl.User createUser(WritableVersion wv)
        throws ConstraintException {
        return addHook(new org.pimslims.model.accessControl.User(wv, getAttrUser()));
    }

    // Implementation.AccessObject
    public static Map<String, Object> getAttrAccessObject() {
        return getName();
    }

    private static Map<String, Object> getName() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("name", "testName" + getString());
        return attributes;
    }

    /**
     * POJOFactory.getAttrTargetGroup
     * 
     * @return
     */
    public static Map<String, Object> getAttrTargetGroup() {
        return getName();
    }

}
