package org.pimslims.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.create.ValueConverter;
import org.pimslims.presentation.mru.MRUController;

/**
 * Update values in database and refresh.
 * 
 * To use this from a JSP, enclose the values displayed in a form: <form
 * action="${pageContext.request['contextPath']}/Update" method="post" > ... <input type="submit"
 * class="submit" value="Save" onclick="dontWarn()" /> <c:if test="${!mayUpdate}">Owner: ${owner}</c:if>
 * </form>
 * 
 * and make each value displayed an input tag, e.g. <input type="text"
 * name="${hook}:<%=Target.PROP_COMMONNAME%>" value="<c:out value='${name}' />" /> or better: <input
 * type="text" name="${bean.nameHook" value="<c:out value='${bean.name}' />" />
 * 
 * This will look like a form. To style it differently when the user does not have update rights, code the
 * header as: <jsp:include page="/JSP/core/Header.jsp"> <jsp:param name="HeaderName" value='... your title
 * ...' /> <jsp:param name="mayUpdate" value='${mayUpdate}' /> </jsp:include>
 * 
 * To pass the appropriate values to your JSP, the servlet's doGet method should use code like:
 * 
 * request.setAttribute("mayUpdate", t.get_MayUpdate() ); request.setAttribute("owner", t.get_Owner());
 * 
 * This servlet works fine if the values concerned can be editted independently. When some scientific
 * constraints apply, you need scientific logic to make the update. In that case you will need to extend this
 * servlet. For an example, see org.pimslims.servlet.experiment.ExperimentUpdate
 * 
 * @author cm65
 * 
 */
public class Update extends PIMSServlet {

    /**
     * BOX_TO_OPEN String
     */
    public static final String BOX_TO_OPEN = "_anchor";

    public Update() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Update values in database and refresh";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // a user may end up here if their browser does not support the referer header
        // TODO send a page that does a javascript back
        throw new ServletException(this.getClass().getName() + " does not support GET");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);
        /*
        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.Update request parameter [" + e.getKey() + ":"
                + s.toString() + "]");
        }
        */
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        boolean isAJAX = false;
        if (null != parms.get("isAJAX")) { // parms.remove would be more
            // logical, but ParameterMap is
            // locked
            isAJAX = true;
        }
        try {
            final HttpSession session = request.getSession();
            final Map<ModelObject, Map<String, Object>> editedObjects =
                Update.processRequest(version, parms, session);
            version.commit();
            // TODO add _parameters to GET e.g. to preserve tab
            if (isAJAX) {
                Update.sendXMLResponse(request, response, editedObjects.keySet());
            } else if (null != request.getPathInfo() && 1 < request.getPathInfo().length()) {
                // path included hook of object to view
                PIMSServlet.redirectPost(response, request.getContextPath() + "/View/"
                    + request.getPathInfo().substring(1));
            } else {
                PIMSServlet
                    .redirectPostToReferer(request, response, request.getParameter(Update.BOX_TO_OPEN));
            }
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            this.log("Access Exception for updating for: " + PIMSServlet.getReferer(request));
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } catch (final ParseException e) {
            this.writeErrorHead(request, response, "Invalid date", HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    private static void sendXMLResponse(final HttpServletRequest request, final HttpServletResponse response,
        final Set<ModelObject> editedObjects) throws IOException {
        final Element rootElement = new Element("saved");
        for (final Iterator iter = editedObjects.iterator(); iter.hasNext();) {
            final ModelObject object = (ModelObject) iter.next();
            final Element elem = new Element("object");
            elem.setAttribute("hook", object.get_Hook());
            rootElement.addContent(elem);
            for (final Iterator i2 = object.get_Values().keySet().iterator(); i2.hasNext();) {
                final String key = (String) i2.next();
                final String val = (String) object.get_Value(key);
                if (null != val) {
                    final Element att = new Element("attribute");
                    att.setAttribute("name", key);
                    att.setAttribute("value", val);
                    elem.addContent(att);
                }
            }
        }
        final Document xml = new Document(rootElement);
        response.setContentType("text/xml");
        final XMLOutputter xo = new XMLOutputter();
        xo.output(xml, response.getWriter());
    }

    /**
     * ${hook}:${fieldName}
     */
    public static final Pattern KEY = Pattern.compile("^([a-zA-Z.]+:\\d+):(\\w+)$");

    public static final String FETCHED = "_fetched";

    public static Map<ModelObject, Map<String, Object>> processRequest(final WritableVersion version,
        final Map<String, String[]> parms, final HttpSession session) throws ServletException,
        AccessException, ConstraintException, ParseException {

        long fetched = System.currentTimeMillis();
        if (null != parms.get(Update.FETCHED)) {
            fetched = Long.parseLong(parms.get(Update.FETCHED)[0]);
        }
        final Set<LabBookEntry> checked = new HashSet();

        final Map<ModelObject, Map<String, Object>> ret = new HashMap<ModelObject, Map<String, Object>>();
        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();

            //System.out.println("processRequest [" + key + ":" + (parms.get(key))[0] + "]");
            if (key.equalsIgnoreCase("isAJAX")) { //TODO _isAJAX
                continue; // we know about that, up in doPost
            }

            if (key.startsWith("p")) {
                continue; // TODO remove this - was temporary to work with Plate.jsp
            }

            final Matcher m = Update.KEY.matcher(key);
            if (!m.matches()) {
                continue; // _anchor
                //throw new ServletException("Invalid parameter name: " + key);
            }
            final String hook = m.group(1);
            final String name = m.group(2);

            final ModelObject object = version.get(hook);
            if (null == object) {
                throw new ServletException("Object not found: " + hook);
            }

            if (name.startsWith("_")) {
                final String val = (parms.get(key))[0];
                if (null != session) {
                    session.setAttribute(key, val);
                }
                if (object instanceof Sample && "_SEQUENCE".equals(name)) {
                    ((Sample) object).set_Value("sequence", val); // TODO setSequence(val)
                    Update.updateChangedMap(ret, val, name, object);
                }
                if (name.endsWith("measurement")) {
                    final Measurement measurement = Measurement.getMeasurement(val);
                    Update.setMeasurement(object, measurement, "amount");
                }
                continue; // allow special parms for future extensions
            }

            String value = null; // can use this method to set to null
            final String[] values = parms.get(key);
            if (1 == values.length && !"".equals(values[0])) {
                value = values[0];
            }
            //System.out.println("Update.processRequest [" + key + ":" + value + "]");
            if (1 < values.length) {
                throw new ServletException("Too many values for: " + key);
            }

            // frig for PiMS 778
            // in protocol editor (ccp.api.Protocol.RefInputSample)
            // ignore unit or value as these are concatenated into amount
            if (object.getClass().getName().equals(RefInputSample.class.getName()) || //"ccp.api.Protocol.RefInputSample"
                object.getClass().getName().equals(RefOutputSample.class.getName()) || //"ccp.api.Protocol.RefOutputSample"
                object.getClass().getName().equals(SampleComponent.class.getName()) || //"ccp.api.Sample.SampleComponent"
                object.getClass().getName().equals(Sample.class.getName())) {
                if (name.equals("unit") || name.equals("units") || name.equals("value")) {
                    continue;
                }
            }
            if (!checked.contains(object) && object instanceof LabBookEntry) {
                final LabBookEntry page = (LabBookEntry) object;
                Calendar date = page.getLastEditedDate();
                if (null == date) {
                    date = page.getCreationDate();
                }
                if (null != date && fetched < date.getTimeInMillis()) {
                    //TODO throw new ConcurrentUpdateException(page);
                }
                checked.add(page);
            }
            Update.updateValue(version, value, name, object);
            Update.updateChangedMap(ret, value, name, object);
        }
        return ret;
    }

    /**
     * @param ret
     * @param value
     * @param name
     * @param object
     * @return
     */
    public static void updateChangedMap(final Map<ModelObject, Map<String, Object>> ret, final String value,
        final String name, final ModelObject object) {
        Map<String, Object> changed = ret.get(object);
        if (null == changed) {
            changed = new HashMap<String, Object>();
            ret.put(object, changed);
        }
        changed.put(name, value);
    }

    /**
     * @param version
     * @param key
     * @param value
     * @param hook
     * @param name
     * @param object
     * @throws ConstraintException
     * @throws ParseException
     * @throws AccessException
     * @throws ServletException
     */
    public static void updateValue(final WritableVersion version, final String value, final String name,
        final ModelObject object) throws ConstraintException, ParseException, AccessException,
        ServletException {

        //System.out.println("Update.updateValue [" + object.get_MetaClass().getName() + ":" + name + ":"
        //    + value + "]");
        final String hook = object.get_Hook();

        if (object.get_MetaClass().getAttributes().containsKey(name)) {
            final Map<String, String> errors = new HashMap();
            final Object convertedValue =
                new ValueConverter(object.get_MetaClass(), errors).getConvertedValue(name, value);
            if (!errors.isEmpty()) {
                throw new RuntimeException(errors.values().iterator().next());
            }
            if (convertedValue instanceof Measurement) {
                Update.setMeasurement(object, (Measurement) convertedValue, name);
            } else {
                object.set_Value(name, convertedValue);
            }
            if (name.equalsIgnoreCase("name")) {
                MRUController.refreshObject(object);
            }
        } else if (object.get_MetaClass().getJavaClass() == Target.class && name.equals("aliases")) {
            if (null == value || "".equals(value.trim()) || "[]".equals(value.trim())) {
                ((Target) object).setAliasNames(Collections.EMPTY_LIST);
            } else {
                ((Target) object).setAliasNames(Update.clean(value));
            }

        } else if (object.get_MetaClass().getMetaRoles().containsKey(name)) {
            final MetaRole role = object.get_MetaClass().getMetaRole(name);
            if (1 < role.getHigh()) {
                throw new ServletException("Cannot update a multirole: " + name + " for: " + hook);
            }
            ModelObject other = null;
            if (!MRUController.NONE.equalsIgnoreCase(value)) {
                other = version.get(value);
                if (null == other) {
                    throw new ServletException("Not found: " + value + " for: " + hook + ":" + name);
                }
                object.set_Role(name, Collections.singleton(other));
            } else {
                object.set_Role(name, (Collection) Collections.emptySet());
            }

        } else if (object instanceof LabBookEntry && LabBookEntry.PROP_PAGE_NUMBER.equals(name)) {
            ((LabBookEntry) object).setPageNumber(value);
        } else {

            throw new ServletException("No such attribute: " + name + " for: " + hook);
        }
    }

    private static void setMeasurement(final ModelObject object, final Measurement measurement,
        final String attributeName) throws AccessException, ConstraintException {
        final String storageUnit =
            ServletUtil.getStorageUnitAttributeName(object.get_MetaClass().getMetaClassName(), attributeName);
        final String displayUnit =
            ServletUtil.getDisplayUnitAttributeName(object.get_MetaClass().getMetaClassName(), attributeName);

        //System.out.println("Update.setMeasurement [" + object.getClass() + ":" + measurement.getFloatValue()
        //    + ":" + measurement.getDisplayUnit() + ":" + measurement.getStorageUnit() + "]");

        object.set_Value(attributeName, measurement.getFloatValue());
        object.set_Value(displayUnit, measurement.getDisplayUnit());
        object.set_Value(storageUnit, measurement.getStorageUnit());
    }

    /**
     * @param value
     * @return
     */
    private static Object convertBoolean(final String value) {
        if (null == value) {
            return null;
        } else if ("yes".equalsIgnoreCase(value.trim())) {
            return Boolean.TRUE;
        } else if ("no".equalsIgnoreCase(value.trim())) {
            return Boolean.FALSE;
        } else if ("true".equalsIgnoreCase(value.trim())) {
            return Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(value.trim())) {
            return Boolean.FALSE;
        } else if ("1".equalsIgnoreCase(value.trim())) {
            return Boolean.TRUE;
        } else if ("0".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        return null;
    }

    // Make sure that values does not contain trailing and leading spaces
    static java.util.List<String> clean(final String value) {

        final String[] strs = value.trim().split(";");
        final List<String> list = new ArrayList<String>(strs.length);
        for (int i = 0; i < strs.length; i++) {
            if (strs[i] == null || strs[i].trim().equals("")) {
                continue;
            }
            list.add(strs[i].trim());
        }
        return list;
    }
/*
    private static final boolean isNumeric(final String s) {
        final char[] numbers = s.toCharArray();
        for (int x = 0; x < numbers.length; x++) {
            final char c = numbers[x];
            if (((c >= '0') && (c <= '9')) || c == '.') {
                continue;
            }
            return false; // invalid
        }
        return true; // valid
    } */

}
