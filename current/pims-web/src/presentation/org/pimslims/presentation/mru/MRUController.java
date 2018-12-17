package org.pimslims.presentation.mru;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Container;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;

public abstract class MRUController {
    private static Map<String, MRUList> mruMap = null; // username->MRUList

    static int maxSize = 60; // the max mru can have in the list

    static int maxSizeOfEachClass = 15; // the max mru for each class can have

    public static void refreshObject(final ModelObject mo) {
        // null objetc is not acceptable
        if (mo == null || MRUController.mruMap == null) {
            return;
        }
        for (final MRUList mruList : MRUController.mruMap.values()) {
            for (final ModelObjectShortBean mru : mruList.getMRUs()) {
                if (mru.getHook().equals(mo.get_Hook())) {
                    mru.setName(mo.get_Name());
                }
            }
        }
    }

    // in the list

    public static void addObject(final String userName, final ModelObject mo) {
        // null objetc is not acceptable
        if (mo == null) {
            return;
        }
        // create MRU
        final MRU mru = new MRU(userName, mo);

        MRUController.addMRU(userName, mru);
    }

    public static void addObjects(final String userName, final Collection<ModelObject> objects) {
        for (final Iterator iterator = objects.iterator(); iterator.hasNext();) {
            final ModelObject object = (ModelObject) iterator.next();
            MRUController.addObject(userName, object);
        }
    }

    /**
     * @param userName
     * @param mru
     */
    private static void addMRU(final String userName, final MRU mru) {

        // get MRUList of this username
        final MRUList mruList = MRUController.getMRUList(userName);

        // add into mruList
        mruList.add(mru);
    }

    public static void deleteObject(final String hook) {
        if (MRUController.mruMap == null) {
            return;
        }
        for (final MRUList mruList : MRUController.mruMap.values()) {
            mruList.removeMRUByHook(hook);
        }
    }

    @Deprecated
    // it is expensive to get a model and a readable version, try to avoid this method
    public static MRU addObject(final String userName, final String hook) {
        // null objetc is not acceptable
        if (hook == null) {
            return null;
        }

        final ReadableVersion rv = ModelImpl.getModel().getReadableVersion(Access.ADMINISTRATOR);
        try {

            final ModelObject mo = rv.get(hook);
            if (mo == null) {
                // no such object, e.g. user has followed stale link to deleted
                // object
                return null;
            }
            final MRU mru = new MRU(userName, mo);
            MRUController.addMRU(userName, mru);
            rv.commit();
            return mru;
        } catch (final ModelException ex) {
            throw new RuntimeException("Fail to create a MRU", ex);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }

        }

    }

    public static List<MRU> getMRUs(final String userName) {
        final MRUList mruList = MRUController.getMRUList(userName);
        return mruList.getMRUs();

    }

    public static List<MRU> getMRUs(final String userName, final String classFullName) {
        final MRUList mruList = MRUController.getMRUList(userName);
        return mruList.getMRUs(classFullName);

    }

    public static MRURoleChoice getMRURoleChoice(final String userName, final String modelObject_hook,
        final String roleName, final String isRequired) {

        final ReadableVersion rv = ModelImpl.getModel().getReadableVersion(userName);

        final MRURoleChoice mruChoice = new MRURoleChoice();
        mruChoice.setModelObject_hook(modelObject_hook);
        mruChoice.setRole_name(roleName);
        // get related object of this role
        try {
            final ModelObject mo = rv.get(modelObject_hook);
            if (null == mo) {
                return null;
            }
            assert null != mo.get_MetaClass().getMetaRole(roleName) : "No such role: " + roleName;
            final Collection<ModelObject> roleObjects = mo.get(roleName);
            Class roleClassName = null;
            if (roleObjects.size() >= 1) {
                final ModelObject roleObject = roleObjects.iterator().next();
                mruChoice.setRole_currentObject(roleObject);
                mruChoice.setRole_currentObjectName(roleObject.get_Name());
                roleClassName = roleObject.get_MetaClass().getJavaClass();
            }

            if (roleClassName == null) {
                roleClassName = mo.get_MetaClass().getMetaRole(roleName).getOtherMetaClass().getJavaClass();
            }
            final Map<String, String> possibleItems =
                MRUController.getPossibleMRUItems(rv, roleClassName, mruChoice.getRole_currentHook(),
                    new Boolean(isRequired));
            mruChoice.setPossibleMRUItems(possibleItems);
            rv.abort();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        return mruChoice;

    }

    /**
     * Provide possible items from MRU for a drop down select List eg: when ask for ExpBlueprint, should
     * return ResearchObjective of targets in MRU as well
     * 
     * @param userName MRU of which user
     * @param classFullName name of class ask for
     * @param currentItemHook this is the item currently used
     * @param isRequired when false, the return list will contains [none],[none]
     * @return a list of possible items <String,String>=<hook,ModelObject.get_Name>
     */

    public static Map<String, String> getPossibleMRUItems(final ReadableVersion rv, final Class clazz,
        final String currentItemHook, final Boolean isRequired) {

        final MRUList mruList = MRUController.getMRUList(rv.getUsername());
        final Collection<MRU> mrus = mruList.getMRUs(clazz.getName());

        return MRUController.getPossibleMRU(rv, clazz.getName(), currentItemHook, isRequired, mruList, mrus);
    }

    public static Map<String, String> getPossibleMRUItems(final ReadableVersion rv,
        final String classFullName, final String currentItemHook, final SampleCategory category,
        final Boolean isRequired) {

        final MRUList mruList = MRUController.getMRUList(rv.getUsername());
        final Collection<MRU> mrus = MRUController.getSampleMRUItems(rv, classFullName, category, mruList);

        return MRUController.getPossibleMRU(rv, classFullName, currentItemHook, isRequired, mruList, mrus);
    }

    //TODO get this under test
    public static Collection<MRU> getSampleMRUItems(final ReadableVersion rv, final String classFullName,
        final SampleCategory category, final MRUList mruList) {
        assert category != null;
        assert mruList != null;
        final Collection<MRU> mrus = new HashSet<MRU>();

        final Collection<MRU> sampleMRUs = mruList.getMRUs(classFullName);
        for (final MRU sampleMRU : sampleMRUs) {
            final Sample sample = rv.get(sampleMRU.getHook());
            if (sample != null) {
                for (final SampleCategory sampleCategory : sample.getSampleCategories()) {
                    if (sampleCategory.getName().equals(category.getName())) {
                        mrus.add(sampleMRU);
                    }
                }
            }
        }
        return mrus;
    }

    static Map<String, String> getPossibleMRU(final ReadableVersion rv, final String classFullName,
        final String currentItemHook, final Boolean isRequired, final MRUList mruList,
        final Collection<MRU> mrus) {

        final Map<String, String> possibleItems = new LinkedHashMap<String, String>();
        // null
        if (!isRequired) {
            possibleItems.put(MRUController.NONE, MRUController.NONE);
        }

        //current person should be on the top
        if (classFullName.equalsIgnoreCase(Person.class.getName())) {
            final User user = rv.getCurrentUser();
            if (user != null) {
                if (user.getPerson() != null) {
                    possibleItems.put(user.getPerson().get_Hook(), user.getPerson().get_Name());
                }
            }
        }

        // normal possibleItems
        for (final ModelObjectShortBean mru : mrus) {
            if (!mru.getHook().equalsIgnoreCase(currentItemHook)) {
                possibleItems.put(mru.getHook(), mru.getName());
            }
        }
        // additional possibleItems
        if (classFullName.equalsIgnoreCase(ResearchObjective.class.getName())) {
            final Collection<MRU> targetMRUs = mruList.getMRUs(Target.class.getName());
            if (targetMRUs.size() > 0) {

                for (final ModelObjectShortBean targetMRU : targetMRUs) {
                    final Target target = rv.get(targetMRU.getHook());
                    if (null == target) {
                        continue;
                    }
                    final Collection<ResearchObjective> expbs = TargetUtility.getTargetExpBlueprint(target);
                    for (final ResearchObjective expb : expbs) {
                        if (!expb.get_Hook().equalsIgnoreCase(currentItemHook)) {
                            possibleItems.put(expb.get_Hook(), expb.get_Name());
                        }
                    }
                }
            }
        }

        return possibleItems;
    }

    public static MRUList getMRUList(final String userName) {
        // init mruMap
        if (MRUController.mruMap == null) {
            MRUController.mruMap = new HashMap<String, MRUList>();
        }
        // get mruList
        MRUList mruList = null;
        if (MRUController.mruMap.containsKey(userName)) {
            mruList = MRUController.mruMap.get(userName);
        } else {
            mruList = new MRUList();
            MRUController.mruMap.put(userName, mruList);
        }
        return mruList;
    }

    // for use in test cases
    public static void clearMRUList(final String userName) {
        final MRUList mruList = new MRUList();
        MRUController.mruMap.put(userName, mruList);
    }

    public static int getMaxSize() {
        return MRUController.maxSize;
    }

    public static void setMaxSize(final int _maxSize) {
        // TODO this could not be <=0
        MRUController.maxSize = _maxSize;
    }

    /**
     * @return the maxSizeOfEachClass
     */
    public static int getMaxSizeOfEachClass() {
        return MRUController.maxSizeOfEachClass;
    }

    /**
     * @param maxSizeOfEachClass the maxSizeOfEachClass to set
     */
    public static void setMaxSizeOfEachClass(final int maxSizeOfEachClass) {
        MRUController.maxSizeOfEachClass = maxSizeOfEachClass;
    }

    public static void clearAll() {
        if (MRUController.mruMap != null) {
            MRUController.mruMap.clear();
        }
    }

    /**
     * MRUController.getDisplayableMRUs Used to make the menus
     * 
     * @param userName
     * @return
     */
    public static Collection<MRU> getDisplayableMRUs(final String userName) {
        final Collection<MRU> allMRUs = MRUController.getMRUs(userName);
        final List<MRU> displayableMRUs = new LinkedList<MRU>();
        for (final MRU mru : allMRUs) {
            if (MRUController.isDisplayable(mru)) {
                displayableMRUs.add(mru);
                if (displayableMRUs.size() >= 10) {
                    break;
                }
            }
        }

        return displayableMRUs;

    }

    /**
     * MRUController.isDisplayable
     * 
     * @param mru
     * @return
     */
    private static boolean isDisplayable(final ModelObjectShortBean mru) {
        final Class mruClass = mru.getMetaClass().getJavaClass();
        if (mruClass.equals(Protocol.class)) {
            return true;
        } else if (mruClass.equals(Sample.class)) {
            return true;
        } else if (mruClass.equals(User.class)) {
            return true;
        } else if (mruClass.equals(Experiment.class)) {
            return true;
        } else if (mruClass.equals(Target.class)) {
            return true;
        } else if (mruClass.equals(ResearchObjective.class)) {
            return true;
        } else if (mruClass.equals(ExperimentGroup.class)) {
            return true;
        } else if (Container.class.isAssignableFrom(mruClass)) {
            return true;
        }

        return false;
    }

    public static final String NONE = "[none]";
}
