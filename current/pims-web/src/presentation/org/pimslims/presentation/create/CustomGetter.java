/*
 * Created on 19.07.2005
 */
package org.pimslims.presentation.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.presentation.AttributeToHTML;
import org.pimslims.presentation.ServletUtil;

/**
 * @author Petr Troshin
 * 
 *         This is all a bit complicated, but it is only used in the generic Create servlet, and there is a
 *         fairly full test case. I don't think any further cleanup is needed, it should be modified only if
 *         it breaks __Chris.
 */

public class CustomGetter {

    /**
     * NOT_PROVIDED String
     */
    private static final String NOT_PROVIDED = "notProvided";

    private boolean cannotCreate;

    /**
     * Html generation result for Attributes
     */
    private final List dochtml;

    /**
     * Generated Html for required attributes
     */
    private List<AttributeToHTML> reqAttrhtml;

    /**
     * Generated Html for optional attributes
     */
    private List<AttributeToHTML> optAttrhtml;

    /**
     * Generated Html for required roles
     */
    private List<AttributeToHTML> reqRoleshtml = new ArrayList();

    /**
     * Generated Html for optional roles
     */
    @Deprecated
    // obsolete, not used
    private ArrayList optRoleshtml;

    /**
     * Generated javascript for a page
     */
    private String javascript;

    /**
     * Whether the form elements is disable
     */
    private boolean readOnly;

    /**
     * Action on the reset button
     */
    private String resetAction;

    /**
     * Readable version
     */
    private final ReadableVersion version;

    /**
     * Metaclass for which the html is generated
     */
    private final MetaClass metaClass;

    /**
     * Map where all the errors goes Attribute.name -> error message Role.name -> error message Role.name
     * +_ODD -> error message (for odd hooks) Special key -> error message notProvided - the String which
     * lists the roles which are not provided (completed) but required for the creation of the Metaclass
     */
    private final Map errorMessages; // attribute to error message if any

    /**
     * Javascript code which will go outside the javascript function In particular help strings.
     */
    private String globalVars;

    /**
     * The context path of the web application e.g. /pims
     */
    private final String context;

    /**
     * The constructor used by Create
     * 
     * @param version
     * @param metaClass
     * @param errorMessages
     * @param attributes - HashMap of MetaAttributes
     * @param parms - HashMap of parameters
     * @param metaroles - HashMap of MetaRoles
     * @param parentClass -
     * @param context -
     * @param param - RoleHooksHolder constructed from String of parameters. Generally the list of hooks and
     *            roles.
     */
    public CustomGetter(final ReadableVersion version, final MetaClass metaClass, final Map errorMessages,
        final Collection attributes, final AttributeValueMap parms, final Collection<MetaRole> metaroles,
        final MetaClass parentClass, final RoleHooksHolder param, final String context) {
        this.cannotCreate = false;
        this.errorMessages = errorMessages;
        this.version = version;
        this.metaClass = metaClass;
        this.setJavascript("");
        this.globalVars = "";
        this.resetAction = "";
        this.readOnly = false;
        this.dochtml = new ArrayList();
        this.setOptAttrhtml(new ArrayList());
        this.setReqAttrhtml(new ArrayList());
        this.optRoleshtml = new ArrayList();
        this.context = context;
        this.setup(attributes, parms, metaroles, parentClass, param);
    }

    public boolean cannotCreate() {
        return this.cannotCreate;
    }

    /**
     * Find the error messages which have no attribute/role related to them.
     * 
     * @param errorMessages
     */
    public void getMissed(final Map errorMessages) {
        final ArrayList allFields = new ArrayList(this.getOptAttrhtml());
        allFields.addAll(this.getReqAttrhtml());
        allFields.addAll(this.optRoleshtml);
        allFields.addAll(this.reqRoleshtml);
        // "notProvided" attrNAme + _ODD; ?
        final ArrayList missedFields = new ArrayList();
        for (final Iterator iter = errorMessages.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();

            if (!allFields.contains(key) && !key.equals(CustomGetter.NOT_PROVIDED)) {
                missedFields.add(key);
                // System.out.println("Missed field: "+ key);
            }
        }
        errorMessages.put("missedErrorFields", missedFields);
    }

    /**
     * Write javascript to page
     * 
     * @return String javascript for a page
     */
    private String writeJavascript() {
        final String jslocal =
            "<script type=\"text/javascript\">\n<!--\n" + "className = '"
                + StringEscapeUtils.escapeJavaScript(ServletUtil.getDisplayName(this.metaClass)) + "';\n"
                + "function onLoadPims() {\n" + this.getJavascript() + "\n} " + this.globalVars
                + "// -->\n</script>\n";
        return jslocal;
    }

    /**
     * Put the value from the array to one String. Separate values by delimiter.
     * 
     * @param String[]
     * @return String
     */
    private static String makeString(final String[] array, final String delimiter) {
        String str = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                str += array[i] + delimiter;
            }
        }
        return str.length() != 0 ? str.substring(0, str.length() - delimiter.length()) : "";
    }

    /**
     * Put the value from the List to one String. Separate values by delimiter.
     * 
     * @return String
     */
    private static String makeString(final ArrayList array, final String delimiter) {
        String str = "";
        if (array != null) {
            for (final Iterator iter = array.iterator(); iter.hasNext();) {
                final String elem = (String) iter.next();
                str += elem + delimiter;
            }
        }
        return str.length() != 0 ? str.substring(0, str.length() - delimiter.length()) : "";
    }

    /*
     * private String getError(String name) { String error =""; if( errorMessages.containsKey(name) ) { Object
     * errorO = (Object)errorMessages.get(name); if(errorO instanceof String[]) { error =
     * makeString((String[])errorO, ", "); } else { error = (String) errorO; } } return error; }
     */
    /**
     * Generate HTML and javascript for MetaAttributes
     * 
     * @param attributes - Collection of MetaAttributes
     * @param parms Map - come from servlet with possible values
     */
    private void getHTMLForAttribute(final Collection attributes, final Map parms) {
        for (final Iterator iter = attributes.iterator(); iter.hasNext();) {
            final Object attribute = iter.next();
            if (attribute == null) {
                continue;
                // if (! attribute.isChangeable() ) continue; - should be used then
                // edit!
            }

            final MetaAttribute attr = (MetaAttribute) attribute;
            if (attr.isDerived()) {
                continue; // Could not change it anyway
            }
            final String[] values = this.getParameterValue(attr, parms);

            AttributeToHTML htmlattr = null;
            Boolean sequenceType = null;
            if (Molecule.PROP_SEQUENCE.equals(attr.getName())
                && Primer.class.getName().equals(this.metaClass.getName())) {
                sequenceType = AttributeToHTML.SEQ_STRING_IS_NUCLEIC_ACID;
            }

            htmlattr = new AttributeToHTML(attr, values, this.context, sequenceType);
            // System.out.println(attr.getName());

            this.setJavascript(this.getJavascript() + htmlattr.getJsLocal());
            this.globalVars += htmlattr.getJsGlobal();
            this.dochtml.add(htmlattr);
        }
    }

    /**
     * Retrieve parameter values from previous request Construct the name of the parameter like:
     * MetaClassName:parameterName
     * 
     * @param attribute - MetaAttribute of MetaAttributes
     * @param parms Map - come from servlet with possible values
     */
    private String[] getParameterValue(final MetaAttribute attribute, final Map parms) {
        String[] values = null;
        if (parms != null) {
            final String parmName =
                attribute.getMetaClass().getJavaClass().getName() + AttributeToHTML.CLASS_ATTRIBUTE_SEPARATOR
                    + attribute.getName();
            values = (String[]) parms.get(parmName);
        }
        return values;
    }

    /**
     * Make a clickable link out of the hook
     * 
     * @param roleName String MetaRole name
     * @param hooks String[] hooks
     * @return String[] of the links
     */
    private String[] makeHookLinks(final String roleName, final String[] hooks) {
        // Copy the array to prevent hooks to be modified
        final String[] hooksCopy = new String[hooks.length];
        System.arraycopy(hooks, 0, hooksCopy, 0, hooks.length);
        for (int i = 0; i < hooksCopy.length; i++) {
            hooksCopy[i] = AttributeToHTML.hooksToLink(this.version, roleName, hooksCopy[i], this.context);
        }
        return hooksCopy;
    }

    /**
     * Make a clickable link out of the hook
     * 
     * @param Map roleNameHooks roleName -> String[] hooks
     * @return HashMap roleName -> String[] clickable links to the modelobject represented by hooks
     */
    private HashMap makeHookLinks(final HashMap roleNameHooks) {
        final HashMap engHooksLinks = new HashMap(roleNameHooks);
        for (final Iterator iterator = roleNameHooks.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry elem = (Map.Entry) iterator.next();
            final String roleName = (String) elem.getKey();
            final String[] hooks = (String[]) elem.getValue();
            engHooksLinks.put(roleName, this.makeHookLinks(roleName, hooks));
        }
        return engHooksLinks;
    }

    /**
     * Prepare and generate HTML and JavaScript for a page
     * 
     * @param attributes ArrayList of MetaAttributes
     * @param parms - parameters come from the request may be with value (relevant to the second generation of
     *            the page only, this usually happens due to the errors)
     * 
     * @param metaroles Map roleName -> MetaRole
     * @param parentClass - main class which is going to be created for instance for a call on the servlet
     *            level with the parameter like this - org.pimslims.model.target.Target:protein
     *            org.pimslims.model.target.Target - parent class, protein which represented by
     *            org.pimslims.model.molecule.Molecule is a metaclass
     * 
     * @param param RoleHooksHolder represented roles and hooks
     */
    // could get this under test
    private void setup(final Collection attributes, final AttributeValueMap parms,
        final Collection<MetaRole> metaroles, final MetaClass parentClass, final RoleHooksHolder param) {

        // Remove engaged hooks which cannot be used
        // should Display warning message to user
        if (metaroles != null && param != null) {
            for (final Iterator iter = metaroles.iterator(); iter.hasNext();) {
                final MetaRole role = (MetaRole) iter.next();
                if (param.isRoleEngaged(role, this.version)) {
                    final HashMap engagedHooks = param.getEngagedHooks(role, this.version);
                    // roleName -> String[] of object hook
                    // System.out.println("engagedHooks "+engagedHooks);
                    this.errorMessages.putAll(this.makeHookLinks(engagedHooks));
                    param.removeHooks(engagedHooks);
                }
                // Do not allow user to set more than required !!!
                // Set one appropriate, remove the rest
                // User errors Map to display warning message
                if (!param.canAssociateMore(role)) {
                    final String[] oddHooks = param.removeOddHooks(role.getRoleName(), this.version);
                    /*
                     * could produce a link to view deleted hooks String roleMetaclName =
                     * role.getOwnMetaClass().getMetaClassName(); HashMap oddHookMap = new HashMap();
                     * oddHookMap.put(roleMetaclName, oddHooks);
                     */
                    // roleName -> String[] hooks
                    if (oddHooks != null) {
                        final String[] links = this.makeHookLinks(role.getRoleName(), oddHooks);
                        this.errorMessages.put(role.getRoleName() + "_ODD", links);
                    }
                }
            }
        }
        this.readOnly = false;
        if (metaroles != null && ServletUtil.containsRequiredRoles(parentClass)
            && (param == null || !param.isRequiredRolesProvided())) {
            this.readOnly = true;
            if (param == null) {
                this.errorMessages.put(CustomGetter.NOT_PROVIDED,
                    CustomGetter.makeString(ServletUtil.getMandatoryRoles(parentClass.getMetaRoles()), ", "));
            } else {
                this.errorMessages.put(CustomGetter.NOT_PROVIDED,
                    CustomGetter.makeString(param.getEmptyMandatoryRoles(), ", "));
            }
            // if(parms != null) parms.clear();
        }

        // Write mandatory roles

        if (metaroles != null) {
            final List<MetaRole> manRoleSub = CustomGetter.getRolesSubset(true, metaroles, param);
            this.reqRoleshtml = this.getHtmlforRole(manRoleSub, parentClass, param, parms);

            if (manRoleSub.size() != this.reqRoleshtml.size()) {
                for (final Iterator iter = manRoleSub.iterator(); iter.hasNext();) {
                    final MetaRole role = (MetaRole) iter.next();
                    if (param == null || !param.isRequiredRoleProvided(role)) {
                        this.cannotCreate = true;
                        break;
                    }
                }
            }
        }

        // Write optional roles
        if (metaroles != null) {
            this.optRoleshtml =
                this.getHtmlforRole(CustomGetter.getRolesSubset(false, metaroles, param), parentClass, param,
                    parms);
        }

        this.getHTMLForAttribute(attributes, parms); // Void make fill in dochtml
        // array
        final List<AttributeToHTML> mandatAttr = this.getSortedAtributes(true);
        if (mandatAttr.size() != 0) {
            // add script for focus
            this.addFocusScript(mandatAttr);
            this.setReqAttrhtml(mandatAttr);
        }
        final List<AttributeToHTML> optAttr = this.getSortedAtributes(false);
        if (optAttr.size() != 0) {
            // add script for focus
            if (mandatAttr.size() == 0) {
                this.addFocusScript(optAttr);
            }
            this.setOptAttrhtml(optAttr);
        }

        if (this.metaClass.getMetaClassName().equals(parentClass.getMetaClassName())) {
            this.resetAction = "../Create/" + parentClass.getMetaClassName();
            if (ServletUtil.containsRequiredRoles(parentClass)
                && (param == null || !param.isRequiredRolesProvided())) {
                this.readOnly = true;
            }

        } else {
            this.resetAction = "javascript:document.forms[1].reset()";
        }
        this.setJavascript(this.writeJavascript());
    }

    /**
     * generate the script for focus and add it into first AttributeToHTML's html
     * 
     * @param optAttr
     */
    private void addFocusScript(final List<AttributeToHTML> optAttr) {
        final AttributeToHTML firstAH = optAttr.get(0);
        // <script type="text/javascript"> if (null==focusElement) focusElement
        // =
        // document.getElementsByName('org.pimslims.model.molecule.Molecule:name')[0]
        // </script>
        final String elementName =
            firstAH.getMetaElement().getMetaClass().getJavaClass().getName() + ":" + firstAH.getName();
        final String scriptForForcus =
            "<script type=\"text/javascript\">	if (null==focusElement) focusElement = document.getElementsByName('"
                + elementName + "')[0] </script>";
        optAttr.get(0).setHtml(optAttr.get(0).getHtml() + scriptForForcus);

    }

    /**
     * Utility method to sort attributes
     * 
     * @param mandatory boolean If true return only required attributes otherwise return only optional
     *            attributes
     * @return List of attributes
     */
    List<AttributeToHTML> getSortedAtributes(final boolean mandatory) {
        final ArrayList mand = new ArrayList();
        final ArrayList opt = new ArrayList();
        for (final Iterator iter = this.dochtml.iterator(); iter.hasNext();) {
            final AttributeToHTML htmlattr = (AttributeToHTML) iter.next();
            if (htmlattr.isRequired()) {
                mand.add(htmlattr);
            } else {
                opt.add(htmlattr);
            }
        }
        if (mandatory) {
            return ServletUtil.sortAttributeToHtml(mand);
        }
        return ServletUtil.sortAttributeToHtml(opt);
    }

    /**
     * Generate html (active links) for roles and javaScript (help on the names)
     * 
     * @param metaroles - Map
     * @param parentClass - main class which is going to be created for instance for a call on the servlet
     *            level with the parameter like this - org.pimslims.model.target.Target:protein
     *            org.pimslims.model.target.Target - parent class, protein which represented by
     *            org.pimslims.model.molecule.Molecule is a metaclass
     * 
     * @param param RoleHooksHolder represented roles and hooks
     * 
     * @return List of AttributeToHTML class instances
     */

    private ArrayList<AttributeToHTML> getHtmlforRole(final List<MetaRole> metaroles,
        final MetaClass parentClass, final RoleHooksHolder param, final AttributeValueMap params) {
        final ArrayList roleshtml = new ArrayList();
        for (final Iterator iter = metaroles.iterator(); iter.hasNext();) {
            final MetaRole role = (MetaRole) iter.next();
            AttributeToHTML htmlAttr = null;

            if (role.isHidden()) {
                // System.out.println("CustomGetter:Will not generate link to
                // create role '" + role.getName()+
                // "' because it hidden ");
                continue;
            }

            final MetaRole mrole = role;
            htmlAttr =
                new AttributeToHTML(mrole, parentClass, param, this.version, this.context, params, null);

            // Do not check roles whether it set or not!
            // javascript += htmlAttr.jsLocal;
            this.globalVars += htmlAttr.getJsGlobal();
            roleshtml.add(htmlAttr);
        }
        return roleshtml;
    }

    /**
     * Return manadatory or optional roles depending on the request
     * 
     * @param mandatory - boolean If true return only required roles otherwise return only optional
     * @param roles List of the roles
     * @param hooks
     * @return List of the MetaRoles
     */
    static ArrayList<MetaRole> getRolesSubset(final boolean mandatory, final Collection<MetaRole> roles,
        final RoleHooksHolder hooks) {
        assert roles != null;
        final ArrayList rolesSubset = new ArrayList();
        for (final Iterator iter = roles.iterator(); iter.hasNext();) {
            final MetaRole role = (MetaRole) iter.next();
            if (mandatory) {
                if (role.getLow() > 0) {
                    rolesSubset.add(role);
                }
            } else {
                if (null == hooks || null == hooks.getFullHooks(role.getName())) {
                    continue; // do not generate HTML for unoccupied optional role
                    // create is complicated enough already. The user can edit this later.
                }
                if (role.getLow() == 0) {
                    rolesSubset.add(role);
                }
            }
        }
        return rolesSubset;
    }

    /**
     * @return Returns the reqRoleshtml.
     */
    public List<AttributeToHTML> getReqRoleshtml() {
        return this.reqRoleshtml;
    }

    /**
     * @return Returns the optRoleshtml.
     */
    @Deprecated
    // obsolete, not used
    public ArrayList<AttributeToHTML> getOptRoleshtml() {
        return this.optRoleshtml;
    }

    public void setOptAttrhtml(final List<AttributeToHTML> optAttr) {
        this.optAttrhtml = optAttr;
    }

    public List<AttributeToHTML> getOptAttrhtml() {
        return this.optAttrhtml;
    }

    public void setReqAttrhtml(final List<AttributeToHTML> mandatAttr) {
        this.reqAttrhtml = mandatAttr;
    }

    public List<AttributeToHTML> getReqAttrhtml() {
        return this.reqAttrhtml;
    }

    /**
     * @return Returns the javascript. Used in Create servlet
     */
    public String getJavascript() {
        return this.javascript;
    }

    /**
     * @param javascript The javascript to set.
     */
    private void setJavascript(final String javascript) {
        this.javascript = javascript;
    }

    /**
     * @Note name is a constraint "parametersString" (!)
     * @param String param = ?molecule=23,23,545&otherMolecules=45
     * @return <input name="asdfasdf" type="hidden" value="skldfjklsdjflksdjflksdjflkjsdkjf" />
     * 
     *         public void writeHooks(String param) {
     *         writer.print(AttributeToHTML.writeHidden("parametersString", param) ); }
     */

} // class end
