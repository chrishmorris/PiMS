package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.bioinf.targets.MPSITargetName;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.InstrumentType;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.create.ValueConverter;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.holder.CreateContainer;

public class CreateTest extends TestCase {

    private final AbstractModel model;

    public CreateTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testNoValues() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(RefInputSample.class.getName());
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("other", new String[] {});
        Map<String, Object> values;
        try {
            values = Create.parseValues(null, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(0, values.size());
            Assert.assertEquals(0, errorMessages.size());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testBadField() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(RefInputSample.class.getName());
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put(RefInputSample.class.getName() + ":nonesuch", new String[] { "value" });
        try {
            Create.parseValues(null, parms, metaClass, errorMessages);
            Assert.fail("bad attribute name accepted");
        } catch (final IllegalArgumentException e) {
            // that's fine
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testOneAttr() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(RefInputSample.class.getName());
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put(RefInputSample.class.getName() + ":" + RefInputSample.PROP_NAME, new String[] { "name" });

        try {
            final Map<String, Object> values = Create.parseValues(null, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(1, values.size());
            Assert.assertTrue(values.containsKey(RefInputSample.PROP_NAME));
            Assert.assertEquals("name", values.get(RefInputSample.PROP_NAME));
            Assert.assertEquals(0, errorMessages.size());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testTwoAttrs() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(RefInputSample.class.getName());
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put(RefInputSample.class.getName() + ":" + RefInputSample.PROP_NAME, new String[] { "name" });
        parms.put(RefInputSample.class.getName() + ":" + RefInputSample.PROP_DISPLAYUNIT,
            new String[] { "ml" });

        try {
            final Map<String, Object> values = Create.parseValues(null, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(2, values.size());
            Assert.assertTrue(values.containsKey(RefInputSample.PROP_NAME));
            Assert.assertEquals("name", values.get(RefInputSample.PROP_NAME));
            Assert.assertTrue(values.containsKey(RefInputSample.PROP_DISPLAYUNIT));
            Assert.assertEquals("ml", values.get(RefInputSample.PROP_DISPLAYUNIT));
            Assert.assertEquals(0, errorMessages.size());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testRole() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(RefInputSample.class.getName());
        Assert.assertTrue(metaClass.getMetaRoles().containsKey(RefInputSample.PROP_PROTOCOL));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "protocol" + System.currentTimeMillis(), type);
            parms.put(RefInputSample.class.getName() + ":" + RefInputSample.PROP_PROTOCOL,
                new String[] { protocol.get_Hook() });
            final Map<String, Object> values = Create.parseValues(version, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(1, values.size());
            Assert.assertEquals(0, errorMessages.size());
            Assert.assertTrue(values.containsKey(RefInputSample.PROP_PROTOCOL));
            final Collection<ModelObject> protocols =
                (Collection<ModelObject>) values.get(RefInputSample.PROP_PROTOCOL);
            Assert.assertEquals(1, protocols.size());
            Assert.assertTrue(protocols.contains(protocol));
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    // &org.pimslims.model.protocol.ParameterDefinition%3Aname=asdfgdfdfsdfxcvvccvx&org.pimslims.model.protocol.ParameterDefinition%3AparamType=Float&org.pimslims.model.protocol.ParameterDefinition%3AisMandatory=true&org.pimslims.model.protocol.ParameterDefinition%3AisGroupLevel=false
    public final void testMandatoryMissing() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(ParameterDefinition.class.getName());
        Assert.assertTrue(metaClass.getMetaRoles().containsKey(ParameterDefinition.PROP_PROTOCOL));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "protocol" + System.currentTimeMillis(), type);
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_PROTOCOL,
                new String[] { protocol.get_Hook() });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_NAME,
                new String[] { "parm" });
            final Map<String, Object> values = Create.parseValues(version, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(4, errorMessages.size()); // paramType, isMandatory, isResult
            // and isGreoupLevel missing
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    // &org.pimslims.model.protocol.ParameterDefinition%3Aname=asdfgdfdfsdfxcvvccvx&org.pimslims.model.protocol.ParameterDefinition%3AparamType=Float&org.pimslims.model.protocol.ParameterDefinition%3AisMandatory=true&org.pimslims.model.protocol.ParameterDefinition%3AisGroupLevel=false
    public final void testMandatory() {
        final Map<String, String> errorMessages = new HashMap<String, String>();
        final MetaClass metaClass = this.model.getMetaClass(ParameterDefinition.class.getName());
        Assert.assertTrue(metaClass.getMetaRoles().containsKey(ParameterDefinition.PROP_PROTOCOL));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "protocol" + System.currentTimeMillis(), type);
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_PROTOCOL,
                new String[] { protocol.get_Hook() });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_NAME,
                new String[] { "test" });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_PARAMTYPE,
                new String[] { "String" });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_ISMANDATORY,
                new String[] { "Yes" });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_ISGROUPLEVEL,
                new String[] { "No" });
            parms.put(ParameterDefinition.class.getName() + ":" + ParameterDefinition.PROP_ISRESULT,
                new String[] { "No" });
            final Map<String, Object> values = Create.parseValues(version, parms, metaClass, errorMessages);
            Assert.assertNotNull(values);
            Assert.assertEquals(0, errorMessages.size());

        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testSuggestedName() {
        final ReadableVersion rv = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final String name = MPSITargetName.getTargetCommonName(rv, "me");
            Assert.assertNotNull(name);
            Assert.assertFalse("".equals(name));
            Assert.assertTrue(name.startsWith("T"));
            Integer.parseInt(name.substring(1)); //e.g. T000164
        } finally {
            rv.abort();
        }
    }

    public void testLength() {
        final MetaClass metaClass = this.model.getMetaClass(ExperimentType.class.getName());
        final Map<String, String> errorMessages = new HashMap();
        final ValueConverter vc = new ValueConverter(metaClass, errorMessages);
        vc.getConvertedValue(LabBookEntry.PROP_DETAILS, "testing name clash");
        Assert.assertEquals(0, errorMessages.size());
    }

    public final void testDoGet() throws ServletException, IOException {
        final Create servlet = new Create();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + Organisation.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final List<String> owners = (List<String>) request.getAttribute("owners");
        Assert.assertNotNull(owners);
        Assert.assertTrue(owners.contains(Access.REFERENCE));

        // was Assert.assertNotNull(request.getAttribute("helptext"));
        Assert.assertNotNull(request.getAttribute("metaclass"));
        Assert.assertNull(request.getAttribute("rolename"));
        Assert.assertNull(request.getAttribute("pathinfo"));
        // was Assert.assertEquals("", request.getAttribute("readOnly"));

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(0, reqRoles.size());
        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());

        /*TODO could test request.getAttribute("reqAttr");
        request.getAttribute("optAttr");
        request.getAttribute("optRoles");
        request.getAttribute("resetAction");
        request.getAttribute("errorMessages");
        request.getAttribute("javascript");
         */
    }

    public final void testDoGetSpecial() throws ServletException, IOException {
        final Create servlet = new CreateContainer();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final Collection<ModelObjectShortBean> types =
            (Collection<ModelObjectShortBean>) request.getAttribute("holderTypes");
        Assert.assertNotNull(types);

        Assert.assertNotNull(request.getAttribute("metaclass"));
        Assert.assertNull(request.getAttribute("rolename"));
        Assert.assertNull(request.getAttribute("pathinfo"));

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(0, reqRoles.size());
        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());

    }

    public final void testDoGetReqRoleMissing() throws ServletException, IOException {
        final Create servlet = new Create();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + WorkflowItem.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());

        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());
        Assert.assertEquals("experimentType", messages.get("notProvided"));
    }

    public final void testDoGetReqRoleSupplied() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {
        // set up
        String constructHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            constructHook = new Construct(version, CreateTest.UNIQUE + "c").get_Hook();
        } finally {
            version.commit();
        }
        final Create servlet = new Create();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(MoleculeFeature.PROP_MOLECULE, new String[] { constructHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + MoleculeFeature.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());

        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject modelObject = version.get(constructHook);
            modelObject.delete();
        } finally {
            version.commit();
        }
    }

    public static final Pattern VIEW = Pattern.compile(".*/View/(.*)");

    private static final String UNIQUE = "c" + System.currentTimeMillis();

    public final void testDoPost() throws ServletException, IOException, AccessException, ConstraintException {
        final Create servlet = new Create();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(Create.PARM_METACLASSNAME, new String[] { Organisation.class.getName() });
        parms.put(Organisation.class.getName() + ":" + Organisation.PROP_NAME,
            new String[] { CreateTest.UNIQUE });
        parms.put("_OWNER", new String[] { Access.REFERENCE });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + Organisation.class.getName());
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = CreateTest.VIEW.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        // test and tidy up
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation organisation = version.get(hook);
            Assert.assertEquals(CreateTest.UNIQUE, organisation.getName());
            Assert.assertEquals(Access.REFERENCE, organisation.get_Owner());
            organisation.delete();
        } finally {
            version.abort();
        }

    }

    /* METACLASSNAME=org.pimslims.model.reference.InstrumentType
     * &org.pimslims.model.reference.InstrumentType%3Aname=Purification+Column
     * &org.pimslims.model.reference.InstrumentType%3Adetails=&parametersString=
     * 
     * There was a problem with: rw.create(owner, metaClass.getJavaClass(), params);
     * */
    public final void testDoPostRefData() throws ServletException, IOException, AccessException,
        ConstraintException {
        final Create servlet = new Create();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(Create.PARM_METACLASSNAME, new String[] { InstrumentType.class.getName() });
        parms.put(InstrumentType.class.getName() + ":" + InstrumentType.PROP_NAME,
            new String[] { CreateTest.UNIQUE });
        // not for ref data parms.put("_OWNER", new String[] { Access.REFERENCE });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + InstrumentType.class.getName());
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = CreateTest.VIEW.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        // test and tidy up
        final WritableVersion version = this.model.getTestVersion();
        try {
            final PublicEntry entry = version.get(hook);
            Assert.assertEquals(CreateTest.UNIQUE, entry.getName());
            entry.delete();
        } finally {
            version.abort();
        }

    }

}
