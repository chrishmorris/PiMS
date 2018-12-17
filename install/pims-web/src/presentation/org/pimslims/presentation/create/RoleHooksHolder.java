package org.pimslims.presentation.create;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.ServletUtil;

/**
 * Parse and store the following parameters
 * "org.pimslims.model.target.Target?molecule=8787,234234,4545,565&otherMolecules=34,4545";
 * 
 * @author Petr Troshin
 */
public class RoleHooksHolder {

    private final MetaClass metaClass;

    private RoleHooks[] rolehooks;

    public RoleHooksHolder(final MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    /**
     * metaClassName org.pimslims.model.target.Target
     * 
     * @param String metaClassName
     */
    public RoleHooksHolder(final String metaClassName) {
        this(ServletUtil.getMetaClass(metaClassName));
    }

    public HashMap getModelObjects(final ReadableVersion rv) {
        final HashMap roleObjs = new HashMap();
        for (int i = 0; i < this.rolehooks.length; i++) {
            roleObjs.put(this.rolehooks[i].roleName, this.rolehooks[i].getObjectsAsList(rv));
        }
        return roleObjs;
    }

    /**
     * @param roleName
     * @param hook
     */
    @Deprecated
    // probably obsolete
    public void addHook(String roleName, final String hook) {
        roleName = RoleHooksHolder.cleanRoleName(roleName);
        this.parse(roleName, hook, -1);
    }

    public void addHooks(String roleName, final String[] hooks) {
        roleName = RoleHooksHolder.cleanRoleName(roleName);
        for (int i = 0; i < hooks.length; i++) {
            this.parse(roleName, hooks[i], -1);
        }
    }

    public boolean isEmpty() {
        return this.rolehooks == null ? true : false;
    }

    public void parse(final Map param) {
        for (final Iterator iter = param.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            final String[] hooksString = (String[]) elem.getValue();
            // There must be only one role name in the path
            assert hooksString != null && hooksString.length == 1;
            String roleName = (String) elem.getKey();

            int setInd = -1;
            if (RoleHooksHolder.isRolewithIndex(roleName)) {
                setInd = RoleHooksHolder.getSetIndex(roleName);
                roleName = RoleHooksHolder.cleanRoleName(roleName);
            }
            // TODO Change this to handle multiple hooks with the same name
            this.parse(roleName, hooksString[0], setInd);
        }
    }

    /**
     * Data example
     * 
     * @param hookString =
     *            ?molecule=org.pimslims.model.molecule.Molecule:23&otherMolecules=org.pimslims.model.
     *            molecule.Molecule:245
     * 
     */
    public void parse(final String roleNameAndFullhookString) {
        final String[] unphook = roleNameAndFullhookString.split("&");
        for (int i = 0; i < unphook.length; i++) {
            final String roleId = unphook[i];
            final int eqs = roleId.indexOf("=");
            assert -1 != eqs : "no '=' in rolename and hook string";
            String roleName = roleId.substring(0, eqs);
            int setInd = -1;
            if (RoleHooksHolder.isRolewithIndex(roleName)) {
                setInd = RoleHooksHolder.getSetIndex(roleName);
                roleName = RoleHooksHolder.cleanRoleName(roleName);
            }

            final String hookId = roleId.substring(eqs + 1);
            this.parse(roleName, hookId, setInd);
        }
    }

    RoleHooks getRoleHook(final String roleName) {
        RoleHooks roleHook = null;
        if (this.rolehooks == null) {
            return null;
        }
        for (int i = 0; i < this.rolehooks.length; i++) {
            if (this.rolehooks[i].roleName.equals(roleName)) {
                roleHook = this.rolehooks[i];
            }
        }
        return roleHook;
    }

    /**
     * Check whether it is possible to associate more objects in role with this.metaClass E.g. Target.Target
     * can have exactly 1 molecule. If there is a hook for molecule this - method returns false, if no hooks
     * found it returns true.
     * 
     * Note that where an associate is required, it is 1..1.
     * 
     * @param role
     * @return true if it is; false otherwise
     */
    public boolean canAssociateMore(final MetaRole role) {
        assert role != null;
        final int card = role.getHigh();
        if (card == -1) {
            return true;
        }
        final RoleHooks rhooks = this.getRoleHook(role.getRoleName());
        int hooksNum = 0;
        if (rhooks != null) {
            hooksNum = rhooks.hooks.length;
        }
        if (hooksNum < card) {
            return true;
        }
        return false;
    }

    //TODO get this under test
    public void removeHooks(final HashMap engagedHooks) {
        for (final Iterator iter = engagedHooks.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            this.removeHooks((String) elem.getKey(), (String[]) elem.getValue());
        }
    }

    private void removeHooks(final String roleName, final String[] hooks) {
        for (int i = 0; i < hooks.length; i++) {
            this.removeHook(roleName, hooks[i]);
        }
    }

    private void removeHook(final String roleName, final String hook) {
        final RoleHooks rhooks = this.getRoleHook(roleName);
        if (rhooks != null) {
            rhooks.removeHook(hook);
        }
        if (rhooks.isHooksEmpty()) {
            this.removeRoleHook(rhooks); // There is no point to have roleName only
        }
    }

    private void removeRoleHook(final RoleHooks rhook) {
        final ArrayList roleHooks = new ArrayList(Arrays.asList(this.rolehooks));
        for (int i = 0; i < this.rolehooks.length; i++) {
            if (this.rolehooks[i].roleName.equals(rhook.roleName)) {
                roleHooks.remove(i);
            }
        }
        this.rolehooks = (RoleHooks[]) roleHooks.toArray(new RoleHooks[roleHooks.size()]);
    }

    /**
     * If current object cannot be associated with given number of objects in the given role, the redundant
     * hooks (leading to objects) must be removed. The method choose the hooks (objects) which are not engaged
     * from other side and therefore can be used in the current object creation. For example only one Molecule
     * in role molecule can be associated to the Target the rest of supplied hooks will be deleted. The method
     * removed redundant hooks and return them in the array
     * 
     * @param role
     * @return String[] of removed hooks for a given role
     */
    @Deprecated
    // solves a problem we no longer have
    public String[] removeOddHooks(final String roleName, final ReadableVersion rv) {
        final ArrayList oddHooks = new ArrayList();
        RoleHooks rhooks = this.getRoleHook(roleName);
        assert rhooks != null; // There are some hooks defined for the role
        final MetaRole role = this.metaClass.getMetaRole(roleName);
        assert role != null : "no such role in the metaclass";
        final int card = role.getHigh();
        assert card != -1 : "no appropriate usage of the method! Use canAssociateMore first";
        assert rhooks.hooks != null && rhooks.hooks.length >= card : "Use canAssociateMore first ";
        if (rhooks.hooks.length == card) {
            return null;
        }
        final String[] fullHooks = rhooks.getHooks();
        for (int i = 0; i < fullHooks.length; i++) {
            if (rhooks.isHookEngaged(fullHooks[i], rv)) {
                oddHooks.add(fullHooks[i]);
                this.removeHook(roleName, fullHooks[i]); // remove all engaged hooks
            }
        }
        rhooks = this.getRoleHook(roleName); // might be removed
        if (rhooks != null && rhooks.hooks.length > card) { // if there are
            // still too many
            // hooks
            final String[] hooks = new String[card];
            for (int i = 0; i < card; i++) {
                hooks[i] = rhooks.hooks[i]; // choose the first
            }
            rhooks.hooks = hooks; // set choosed
            for (int i = card; i < rhooks.hooks.length; i++) {
                oddHooks.add(rhooks.hooks[i]); // add the rest to removed hooks
                // array
            }
        }
        return (String[]) oddHooks.toArray(new String[oddHooks.size()]);
    }

    /**
     * Return true if at least one (or more) of the supplied hooks cannot be use for the object represented by
     * the metaclass creation.
     * 
     * This applies if the object chosen already has as many links of this type as permitted, e.g. a
     * MolComponent is already Protein for another target.
     * 
     * @see getEngagedHooks method for more info
     * @param role
     * @return true if the object on another side of association cannot be associated with current object
     *         (represented by metaclass), false otherwise
     */
    @Deprecated
    // such objects should never be chosen
    public boolean isRoleEngaged(final MetaRole role, final ReadableVersion rv) {
        // was assert role != null && rv != null;
        final MetaRole otherRole = role.getOtherRole();
        if (null == otherRole) {
            // role not navigable the other way
            return false;
        }
        final int card = otherRole.getHigh();
        if (card == -1) {
            return false; // role could accept any number of objects
        }
        final RoleHooks rhooks = this.getRoleHook(role.getRoleName());
        if (rhooks == null) {
            return false; // no object in this role have been associated yet
        }
        final String[] eHooks = rhooks.getEngagedHooks(rv);
        if (eHooks != null) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the object on the other side of assiciation (represented by hook) could be connected to
     * the object on this side. E.g. Target has a role molecule, hook for this role is supplied but the
     * instance of molecule may allready being associated with another Target. Molecule has role
     * MoleculeTarget with max cardinality 1. If the hook(s) cannot be used because of the reason above they
     * will be returned in the Map - roleName - String[] hook(s)
     * 
     * @param role
     * @param rv
     * @return HashMap roleName->String[] hooks Returns the HashMap of hooks which of the objects which cannot
     *         be associated with current object represented by metaClass If no engaged hooks have been found
     *         returns null
     */
    @Deprecated
    // such objects should never be chosen
    public HashMap getEngagedHooks(final MetaRole role, final ReadableVersion rv) {
        final MetaRole otherRole = role.getOtherRole();
        final int card = otherRole.getHigh();
        if (card == -1) {
            return null; // role could accept any number of objects
        }
        final RoleHooks rhooks = this.getRoleHook(role.getRoleName());
        if (rhooks == null) {
            return null; // no hooks for role were provided
        }
        final String[] engHooks = rhooks.getEngagedHooks(rv);
        if (engHooks == null) {
            return null; // no hooks are engaged
        }
        final HashMap roleEngHooks = new HashMap();
        roleEngHooks.put(role.getRoleName(), engHooks);
        return roleEngHooks;
    }

    boolean containsHookForRole(final String roleName) {
        if (this.getRoleHook(roleName) != null) {
            return true;
        }
        return false;
    }

    public boolean isRequiredRolesProvided() {
        final ArrayList mroles = ServletUtil.getMandatoryRoles(this.metaClass.getMetaRoles());
        final int reqRolesNum = mroles.size();
        int rolesCounter = 0;
        for (final Iterator iter = mroles.iterator(); iter.hasNext();) {
            final String roleName = (String) iter.next();
            //System.out.println("isReq RoleName: " + roleName);
            if (this.containsHookForRole(roleName)) {
                rolesCounter++;
            }
        }
        return reqRolesNum == rolesCounter ? true : false;
    }

    public boolean isRequiredRoleProvided(final MetaRole role) {
        if (this.containsHookForRole(role.getRoleName())) {
            return true;
        }
        return false;
    }

    public String[] getEmptyMandatoryRoles() {
        final ArrayList mroles = ServletUtil.getMandatoryRoles(this.metaClass.getMetaRoles());
        final ArrayList emptyMroles = new ArrayList(mroles);
        int count = -1;
        for (final Iterator iter = mroles.iterator(); iter.hasNext();) {
            count++;
            final String roleName = (String) iter.next();
            if (this.containsHookForRole(roleName)) {
                emptyMroles.remove(count);
            }
        }
        return (emptyMroles.size() != 0) ? (String[]) emptyMroles.toArray(new String[emptyMroles.size()])
            : null;
    }

    private static String cleanRoleName(String roleName) {
        final int star = roleName.indexOf("*");
        if (star >= 0) {
            roleName = roleName.substring(star + 1);
        }
        return roleName;
    }

    private static boolean isRolewithIndex(final String roleName) {
        return roleName.indexOf("*") >= 0 ? true : false;
    }

    //TODO get this under test
    private static int getSetIndex(String roleName) {
        final int star = roleName.indexOf("*");
        assert star >= 0 : "Role must contain setindex info e.g. 1*citation";
        int absMClassId = -1;
        if (star >= 0) {
            absMClassId = Integer.parseInt(roleName.substring(0, star).trim());
            roleName = RoleHooksHolder.cleanRoleName(roleName);
        }
        assert absMClassId != -1 : "Parsing was not successful. POssibly role name was formed incorrectly";
        return absMClassId;
    }

    // "molecule=8787,234234,4545,565
    /**
     * @param roleName = molecule
     * @param String[] hooksIds = 8787,234234,4545,565 or: org.pimslims.model.molecule.Molecule:98
     */
    private void parse(final String roleName, final String hooksIdsString, final int setIndex) {
        // Obtain rolehook if there is one with the same name
        // create new if not
        // System.out.println("RoleHooksHolder.parse
        // ("+roleName+","+hooksIdsString+","+setIndex+")");
        RoleHooks roleHook = this.getRoleHook(roleName);
        boolean isNew = false;
        if (roleHook == null) {
            if (!RoleHooksHolder.roleExists(this.metaClass, roleName)) {
                return; // ignore attributes
            }
            MetaClass roleMetaCl = null;
            if (setIndex == -1) {
                roleMetaCl = ServletUtil.getMetaClassForRole(this.metaClass, roleName);
            } else {
                roleMetaCl = ServletUtil.getMetaClassForAbstractRole(this.metaClass, roleName, setIndex);
            }
            roleHook = new RoleHooks(roleName, roleMetaCl.isAbstract() ? true : false, this.metaClass);
            isNew = true;
        }

        roleHook.addHook(hooksIdsString);
        // Add roleHook to RoleHooksHolder
        if (isNew) {
            this.addRoleHook(roleHook);
        }
    }

    // TODO if is not new update or remove the old one!
    private void addRoleHook(final RoleHooks roleHook) {
        if (this.rolehooks != null) {
            final ArrayList roleh = new ArrayList(Arrays.asList(this.rolehooks));
            roleh.add(roleHook);
            this.rolehooks = (RoleHooks[]) roleh.toArray(new RoleHooks[roleh.size()]);
        } else {
            this.rolehooks = new RoleHooks[1];
            this.rolehooks[0] = roleHook;
        }
    }

    static class RoleHooks {

        private final MetaClass metaClass;

        String roleName;

        //TODO make this always true, it's simpler
        final boolean fullHooks;

        String[] hooks;

        protected RoleHooks(final String roleName, final boolean fullHooks, final MetaClass metaClass) {
            this.roleName = roleName;
            this.fullHooks = fullHooks;
            this.metaClass = metaClass;
        }

        public boolean isHooksEmpty() {
            if (this.hooks == null || this.hooks.length == 0) {
                return true;
            }
            return false;
        }

        ModelObject[] getObjects(final ReadableVersion rv) {
            final ModelObject[] mObjs = new ModelObject[this.hooks.length];
            for (int i = 0; i < this.hooks.length; i++) {
                mObjs[i] = this.getObject(this.hooks[i], rv);
            }
            return mObjs;
        }

        /**
         * 
         * @param hook - always full hook
         */
        void removeHook(final String hook) {
            final ArrayList hooks = new ArrayList(Arrays.asList(this.hooks));
            String procHook = "";
            if (this.fullHooks) {
                procHook = hook;
            } else {
                procHook = this.processHook(hook);
            }
            for (int i = 0; i < this.hooks.length; i++) {
                if (this.hooks[i].equals(procHook)) {
                    System.out.println("removed Hook " + hooks.get(i));
                    hooks.remove(i);
                }
            }
            this.hooks = (String[]) hooks.toArray(new String[hooks.size()]);
        }

        @Deprecated
        // such objects should not have been selected
        String[] getEngagedHooks(final ReadableVersion rv) {
            final ArrayList engHooks = new ArrayList();
            final String[] hooks = this.getHooks();
            for (int i = 0; i < hooks.length; i++) {
                if (this.isHookEngaged(hooks[i], rv)) {
                    engHooks.add(hooks[i]);
                }
            }
            if (engHooks.size() == 0) {
                return null;
            }
            return (String[]) engHooks.toArray(new String[engHooks.size()]);
        }

        @Deprecated
        // such objects should not have been selected
        private boolean isHookEngaged(final String hook, final ReadableVersion rv) {
            //TODO return false;
            final MetaRole role = this.metaClass.getMetaRole(this.roleName);
            assert role != null;
            final String otherRoleName = role.getOtherRole().getRoleName();
            final ModelObject mObj = this.getObject(hook, rv);
            assert null != mObj : "Bad hook: " + hook;
            final Object mo = ServletUtil.getModelObject(otherRoleName, mObj);
            System.out.println("otherRoleName " + otherRoleName);
            System.out.println("mo " + mo);
            if (mo != null) {
                return true;
            }
            return false;
        }

        Collection getObjectsAsList(final ReadableVersion rv) {
            final ModelObject[] ms = this.getObjects(rv);
            return new HashSet(Arrays.asList(ms));
        }

        private String getMetaClassNameForRole() {
            assert this.metaClass != null;
            final MetaRole mrole = this.metaClass.getMetaRole(this.roleName);
            return mrole.getOtherMetaClass().getMetaClassName();
        }

        private ModelObject getObject(String hook, final ReadableVersion rv) {
            ModelObject mObj = null;
            if (!this.fullHooks && hook.indexOf(":") < 0) {
                hook = this.unProcessHook(hook);
            }
            System.out.println("getObject " + hook);
            mObj = rv.get(hook);
            return mObj;
        }

        @Deprecated
        // TODO always use full hooks
        private String unProcessHook(final String hookId) {
            if (hookId.contains(":")) {
                return hookId;
            }
            if (this.fullHooks) {
                return hookId;
            }
            return this.getMetaClassNameForRole() + ":" + hookId;
        }

        @Deprecated
        // TODO always use full hooks
        private String processHook(final String hookString) {
            final int del = hookString.indexOf(":");
            assert del >= 0;
            return hookString.substring(del + 1).trim();
        }

        private String[] getHooks() {
            if (this.metaClass == null) {
                throw new AssertionError("Please set metaclass first ");
            }
            final String[] fullHooks = new String[this.hooks.length];
            for (int i = 0; i < this.hooks.length; i++) {
                fullHooks[i] = this.unProcessHook(this.hooks[i]);
            }
            return fullHooks;
        }

        private ArrayList cleanHooks(final String[] hooks) {
            final ArrayList phooks = new ArrayList(hooks.length);
            for (int i = 0; i < hooks.length; i++) {
                final String hook = hooks[i].trim();
                if (hook != null && hook.length() > 0) {
                    if (this.fullHooks && hook.indexOf(":") < 0) {
                        throw new AssertionError("Hook must be full for the " + this.roleName + " role!");
                    }
                    /* Removed to fix PIMS-2939
                    if (!this.fullHooks && hook.indexOf(".") >= 0 && hook.indexOf(":") >= 0) {
                        hook = this.processHook(hook);
                    } */
                    phooks.add(hook);
                }
            }
            return phooks;
        }

        /**
         * Data example: 123,34534,4545
         * 
         * @param hook org.pimslims.model.target.Target:8769 or 9823
         */
        void addHook(final String hookIds) {
            // Add hooks to roleHook
            if (this.hooks != null) {
                final ArrayList phooks = this.cleanHooks(hookIds.split(","));
                final ArrayList setlist = new ArrayList(Arrays.asList(this.hooks));
                // System.out.println("phooks "+phooks);
                // System.out.println("setlist "+setlist);
                setlist.addAll(phooks);
                this.hooks = this.removeDuplicates((String[]) setlist.toArray(new String[setlist.size()]));
            } else {
                final ArrayList phooks = this.cleanHooks(hookIds.split(","));
                this.hooks = this.removeDuplicates((String[]) phooks.toArray(new String[phooks.size()]));
            }
        }

        /*
         * Remove duplicate hooks
         */
        private String[] removeDuplicates(final String[] hooks) {
            if (hooks == null) {
                return null;
            }
            final HashSet h = new HashSet();
            for (int i = 0; i < hooks.length; i++) {
                h.add(hooks[i]);
            }
            return (String[]) h.toArray(new String[h.size()]);
        }

        @Override
        public String toString() {
            String hook = "RoleName: " + this.roleName + "\n";
            if (this.hooks != null) {
                for (int i = 0; i < this.hooks.length; i++) {
                    hook += "Object hook: " + this.hooks[i] + "\n";
                }
            } else {
                hook += "there are no hooks defined " + "\n";
            }
            return hook;
        }

        protected String toParamString() {
            String out = this.roleName + "=";
            for (int i = 0; i < this.hooks.length; i++) {
                out += this.hooks[i] + ",";
            }
            return out.substring(0, out.length() - 1);
        }

    } // Hooks class end

    @Override
    public String toString() {
        String out = "MetaClassName: " + this.metaClass.getMetaClassName() + "\n";
        if (this.rolehooks != null) {
            for (int i = 0; i < this.rolehooks.length; i++) {
                out += this.rolehooks[i].toString() + "\n";
            }
        }
        return out;
    }

    public String toDecodedParamString() {
        if (null == this.rolehooks) {
            return "";
        }
        String out = "";
        for (int i = 0; i < this.rolehooks.length; i++) {
            out += this.rolehooks[i].toParamString() + "&";
        }
        if (0 == out.length()) {
            return "";
        }
        return out.substring(0, out.length() - 1);
    }

    public String[] getFullHooks(final String roleName) {
        final RoleHooks rhook = this.getRoleHook(roleName);
        if (rhook != null) {
            return rhook.getHooks();
        }
        return null;
    }

    public static boolean roleExists(final MetaClass parentMetaClass, final String roleName) {
        return parentMetaClass.getMetaRoles().containsKey(roleName);
    }

} // RoleHooks class end
