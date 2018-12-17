/*
 * Created on 18.07.2005
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AccessRightsUtility;
import org.pimslims.lab.Measurement;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.holder.Holder;
import org.pimslims.presentation.AttributeToHTML;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.create.AttributeValueMap;
import org.pimslims.presentation.create.CustomGetter;
import org.pimslims.presentation.create.RoleHooksHolder;
import org.pimslims.presentation.create.ValueConverter;

/**
 * @author Petr Troshin
 */
public class Create extends PIMSServlet {

    /**
     * PARM_METACLASSNAME String
     */
    public static final String PARM_METACLASSNAME = "METACLASSNAME";

    public static final long serialVersionUID = 123243546;

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create a model object";
    }

    /**
     * 
     */
    public Create() {
        super();
    }

    /**
     * Parse parameters and return them as HashMap paramName->paramValue
     * 
     * @param parms HashMap metaclassName:parameterName->Value
     * @param metaClass
     * @param errorMessages
     * @param vc - ValueConverter class instance
     * @return HashMap parameterName->Value
     * @throws ServletException
     */
    public static Map<String, Object> parseValues(final ReadableVersion version, final Map parms,
        final MetaClass metaClass, final Map errorMessages) throws ServletException {

        final ValueConverter vc = new ValueConverter(metaClass, errorMessages);
        final Map<String, Object> fieldValueMap = new HashMap<String, Object>();
        final Map<String, MetaRole> roles = metaClass.getMetaRoles();

        for (final Iterator iter = parms.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry element = (Map.Entry) iter.next();
            final String key = (String) element.getKey();
            final String[] value = (String[]) element.getValue();
            final int del = key.indexOf(AttributeToHTML.CLASS_ATTRIBUTE_SEPARATOR);
            // dont touch other attributes in the parms Map
            if (del < 0) {
                continue;
            }
            //clazz = key.substring(0, del);
            final String field = key.substring(del + 1);

            if (field.startsWith("_")) {
                if (field.endsWith("measurement")) {
                    final Measurement measurement = Measurement.getMeasurement(value[0]);
                    if (null != measurement) {
                        fieldValueMap.put(
                            ServletUtil.getStorageUnitAttributeName(metaClass.getMetaClassName(), field),
                            measurement.getStorageUnit());
                        fieldValueMap.put(
                            ServletUtil.getDisplayUnitAttributeName(metaClass.getMetaClassName(), field),
                            measurement.getDisplayUnit());
                        fieldValueMap.put("amount", measurement.getFloatValue());
                    }
                }
                continue; // allow special parms for future extensions
            }

            if (roles.containsKey(field)) {
                // not used for standard create, but used for AjaxCreate
                final List associates = new ArrayList<ModelObject>(value.length);
                for (int i = 0; i < value.length; i++) {
                    final String v = value[i];
                    if (null == v || "".equals(v)) {
                        continue;
                    }
                    final ModelObject associate = version.get(v);
                    if (null == associate) {
                        throw new ServletException(field + " not found: " + v);
                    }
                    associates.add(associate);
                }
                fieldValueMap.put(field, associates);
                continue;
            }
            // it's an attribute, convert the value
            final Object values = vc.getConvertedValue(field, value[0]);
            fieldValueMap.put(field, values);
        }
        final Map attributes = vc.metaclass.getAttributes();
        for (final Iterator iter = attributes.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            final String attrName = (String) elem.getKey();
            final MetaAttribute pattr = (MetaAttribute) elem.getValue();
            if (pattr.isRequired() && (fieldValueMap.get(attrName)) == null) {
                vc.errorMessages.put(attrName, "must be completed");
                //System.out.println("req" + pattr.getName());
            }
            if (pattr.isRequired() && fieldValueMap.get(attrName) instanceof String) {
                if (((String) fieldValueMap.get(attrName)).trim().length() == 0) {
                    vc.errorMessages.put(attrName, "must be completed");
                    //System.out.println("req" + pattr.getName());
                }
            }
        }
        return fieldValueMap;
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);

        final java.io.PrintWriter writer = response.getWriter();

        final String metaClassName = request.getParameter(Create.PARM_METACLASSNAME); //TODO can get from pathInfo

        boolean isBulkCreate = false;
        if (null != request.getParameter("_bulkcreate")) {
            isBulkCreate = true;
        }

        String parameters = request.getParameter("parametersString");
        final String pathInfo = this.getMetaClassName(request);
        //TODO assert pathInfo.equals(metaClassName);
        final int classRolesepIdx = pathInfo.indexOf(":");
        String mainMetaClassName = null;
        String roleName = null;
        if (classRolesepIdx >= 0) {
            mainMetaClassName = pathInfo.substring(0, classRolesepIdx);
            roleName = pathInfo.substring(classRolesepIdx + 1);
            throw new AssertionError("beleived obsolete");
        } else {
            mainMetaClassName = pathInfo;
        }

        final RoleHooksHolder rl = new RoleHooksHolder(mainMetaClassName);
        if (parameters != null && parameters.length() != 0) {
            parameters = java.net.URLDecoder.decode(parameters, "UTF-8");
            rl.parse(parameters.substring(1));
        }

        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        if (null == metaClass) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Unknown type: " + metaClassName);
            return;
        }

        final java.util.Map parms = request.getParameterMap();
        final Map errorMessages = new HashMap();

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        try {
            final Map params = Create.parseValues(rw, parms, metaClass, errorMessages);
            /*
             * If this is Target make sure that there is a meaningful commonName
             
            if (metaClassName.equals("org.pimslims.model.target.Target")) {
                parms = Create.getCommonName(parms, rw, request.getRemoteUser()); // obsolete
            } */

            //System.out.println(this.getClass().getName() + " ERRORS: " + errorMessages.size());

            if (errorMessages.size() != 0) {
                final HttpSession session = request.getSession();
                //TODO shouldn't this be request?
                session.setAttribute("errorMessages", errorMessages);
                session.setAttribute("formValues", new HashMap(parms));
                rw.abort();
                this.redirect(response, request.getContextPath() + "/Create/" + pathInfo + parameters);
                return;
            }

            // Everything OK create a model object
            String hook = null;

            if (!rl.isEmpty() && classRolesepIdx < 0) {
                final HashMap roleObjs = rl.getModelObjects(rw);
                //System.out.println("roleObjs " + roleObjs);
                params.putAll(roleObjs);
            }

            // create it
            ModelObject mObj = null;
            if (LabBookEntry.class.isAssignableFrom(metaClass.getJavaClass())) {
                String owner = request.getParameter("_OWNER");
                if ("".equals(owner) && PIMSServlet.isAdmin(request)) {
                    owner = "reference";
                } else if (null == owner) {
                    //throw new ServletException("Owner must provided");
                    throw new AccessException("Owner must provided");
                }
                mObj = rw.create(owner, metaClass.getJavaClass(), params);
            } else { // PublicEntry or SystemEntry
                if (!PIMSServlet.isAdmin(request)) {
                    throw new AccessException("Only the PiMS administrator can create a: "
                        + metaClass.getMetaClassName());
                }
                mObj = rw.create(metaClass.getJavaClass(), params);
            }

            rw.commit();
            hook = mObj.get_Hook();
            if (roleName != null) {
                throw new ServletException("suspected obsolete");
                /* probably obsolete - this seems to be create for create
                rl.addHook(roleName, hook);
                PIMSServlet.redirectPost(response, request.getContextPath() + "/Create/" + mainMetaClassName
                    + "?" + rl.toDecodedParamString()); */
            } else if (isBulkCreate) {
                PIMSServlet.redirectPost(response, request.getContextPath() + "/Create/" + mainMetaClassName
                    + "?_bulkcreate=true&_created=" + mObj.get_Hook());
            } else {
                PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);
            }

        } catch (final AccessException aex) {
            //PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            //writer.print("You are not allowed to make these changes");
            request.setAttribute("javax.servlet.error.exception", aex);
            request.getRequestDispatcher("/public/Denied").forward(request, response);
            return;
        } catch (final ConstraintException cex) {
            rw.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
    }

    /**
     * Specific subclasses will need to override this
     * 
     * @param request
     * @return the name of the metaclass to create, or name:role
     */
    protected String getMetaClassName(final HttpServletRequest request) throws ServletException {
        final String pathInfo = request.getPathInfo().substring(1);
        return pathInfo;
    }

    void sendErrors(final String message, final HttpServletResponse response, final PrintWriter writer) {
        PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
        writer.print(message);
    }

    /**
     * {@inheritDoc}
     * 
     * @baseURL org.pimslims.model.target.Target:molecule?molecule=hook,hook,hook&OtherMolecules=hook,hook
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        response.getWriter();
        if (!this.checkStarted(request, response)) {
            return;
        }

        //_bulkcreate=true&_created=" + mObj.get_Hook()
        if (null != request.getParameter("_bulkcreate")) {
            request.setAttribute("isBulkCreate", true);
        }

        final String pathInfo = this.getMetaClassName(request);
        //MetaClass metaClass = null;
        MetaClass mainMetaClass = null;
        final int columnp = pathInfo.indexOf(":");
        final boolean isRoleObject = columnp >= 0;
        assert !isRoleObject : "Create for create";
        final String roleName = null;

        mainMetaClass = this.getModel().getMetaClass(pathInfo);
        if (mainMetaClass == null) {
            request.setAttribute("message", "Unknown object type. The metaclass was not set.");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (mainMetaClass.isAbstract()) {
            request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(mainMetaClass));
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/create/CreateAbstract.jsp");
            dispatcher.forward(request, response);
            return;
        }

        final Map parms = request.getParameterMap();

        // get associates from parameter list
        RoleHooksHolder hooks = null;
        if (parms != null && parms.size() > 0) {
            hooks = new RoleHooksHolder(mainMetaClass);
            hooks.parse(parms);
        }

        final AttributeValueMap attributeValues = new AttributeValueMap(mainMetaClass, parms);
        Map errorMessages = null;
        Map formValues = null;
        final HttpSession session = request.getSession(false);
        if (session != null) {
            errorMessages = (Map) session.getAttribute("errorMessages");
            formValues = (Map) session.getAttribute("formValues");

            session.removeAttribute("errorMessages");
            session.removeAttribute("formValues");
        }
        if (errorMessages == null) {
            errorMessages = Collections.synchronizedMap(new HashMap());
        }
        if (null != formValues) {
            attributeValues.putAll(formValues);
        }

        // TODO rewrite as a page hidden parameter

        //MetaClass pmeta = null;
        Map attributes = null;

        //pmeta = mainMetaClass;
        attributes = mainMetaClass.getAttributes();

        attributes = this.filterAttributes(attributes);

        java.util.Collection<MetaRole> roles = null;
        if (!isRoleObject) {
            roles = mainMetaClass.getMetaRoles().values();
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {

            request.setAttribute("pathInfo", pathInfo);
            request.setAttribute("metaclass", mainMetaClass);
            //request.setAttribute("mainclass", mainMetaClass);
            request.setAttribute("roleName", roleName);

            //TODO simplify this
            final CustomGetter customget =
                new CustomGetter(rv, mainMetaClass, errorMessages,
                    ServletUtil.getSortedAttributes(attributes), attributeValues, roles, mainMetaClass,
                    hooks, request.getContextPath());

            if (customget.cannotCreate()) {
                request.setAttribute("message", "Sorry cannot create this object. Try another ways"
                    + " e.g. create all mandatory roles (relation) first");
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                return;
            }
            customget.getMissed(errorMessages);
            //request.setAttribute("helptext", customget.getHelpText());
            request.setAttribute("headerTitle", "Record details of a new " + mainMetaClass.getAlias());
            // request.setAttribute("owner", rv.getDefaultOwner(mainMetaClass, null));
            request.setAttribute("reqAttr", customget.getReqAttrhtml());
            request.setAttribute("optAttr", customget.getOptAttrhtml());
            //request.setAttribute("optRoles", customget.getOptRoleshtml());

            request.setAttribute("reqRoles", customget.getReqRoleshtml());
            //request.setAttribute("readOnly", customget.readOnly ? "disabled" : "");
            //request.setAttribute("resetAction", customget.resetAction);

            request.setAttribute("errorMessages", errorMessages);
            request.setAttribute("javascript", customget.getJavascript());

            String paramString = "";
            if (hooks != null && !hooks.isEmpty()) {
                paramString = "?" + hooks.toDecodedParamString();
            }
            request.setAttribute("parameters", paramString);
            //TODO owners replaced by accessObjects, fix Defaul.jsp
            request.setAttribute("owners",
                AccessRightsUtility.getCanCreateOwners(rv, PIMSServlet.getCurrentUser(rv, request)));
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(rv));
            response.setStatus(HttpServletResponse.SC_OK);

            //_bulkcreate=true&_created=" + mObj.get_Hook()
            if (null != request.getParameter("_created")) {
                final ModelObject lastCreated = rv.get(request.getParameter("_created"));
                request.setAttribute("lastCreated", lastCreated);
                request.setAttribute("owner", lastCreated.get_Owner());
                if (lastCreated instanceof Holder) {
                    request.setAttribute("lastCreated_HolderType", ((Holder) lastCreated).getHolderType()
                        .get_Hook());
                }
            }

            this.addSpecialObjects(request, rv);

            PIMSServlet.dispatchCustomJsp(request, response, mainMetaClass.getMetaClassName(), "create",
                this.getServletContext());

            rv.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /**
     * Create.addSpecialObjects
     * 
     * @param response
     * @param rv
     */
    protected void addSpecialObjects(final HttpServletRequest request, final ReadableVersion rv)
        throws ServletException {
        // override this to customise        
    }

    /**
     * Create.filterAttributes
     * 
     * @param attributes
     * @return
     */
    protected Map<String, MetaAttribute> filterAttributes(final Map<String, MetaAttribute> attributes) {
        final Map<String, MetaAttribute> results = new HashMap<String, MetaAttribute>(attributes);
        if (attributes.keySet().contains(LabBookEntry.PROP_CREATIONDATE)) {
            results.remove(LabBookEntry.PROP_CREATIONDATE);
        }
        if (attributes.keySet().contains(LabBookEntry.PROP_LASTEDITEDDATE)) {
            results.remove(LabBookEntry.PROP_LASTEDITEDDATE);
        }

        return results;
    }

    /**
     * Use this if you not in a Wizard and need only to have a defenition taken from MetaInfo.xml
     * 
     * @param metaAttributes
     * @param metaClassName
     * @return ArrayList of MetaAttribute instances.
     */

    public static HashMap joinAttributes(final Map metaAttributes, final String metaClassName) {
        return new HashMap(metaAttributes);
    }

}
