/*
 * Created on 20-Jul-2005 @author: Chris Morris
 */
package org.pimslims.lab;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.constraint.Constraint;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;

/**
 * Test lab methods for protocl
 * 
 * @version 0.2
 */
public class TestProtocol extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName the name of hte test method to run
     */
    public TestProtocol(final String methodName) {
        super(methodName);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    public void test() {

        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ExperimentType experimentType =
                ProtocolFactory.createExperimentType(wv, "test type " + new java.util.Date().getTime(),
                    Collections.EMPTY_MAP);
            final Protocol protocol =
                ProtocolFactory.createProtocol(wv, "my protocol" + new java.util.Date().getTime(),
                    experimentType, Collections.EMPTY_MAP);
            ProtocolFactory.createSampleCategory(wv, "test category " + new java.util.Date().getTime(),
                Collections.EMPTY_MAP);

            final java.util.Map attributes = new java.util.HashMap();
            attributes.put("name", "ParameterDefinition created " + new java.util.Date().getTime());
            attributes.put(ParameterDefinition.PROP_PARAMTYPE, "String");
            ProtocolFactory.createParameterDefinition(wv, protocol, attributes);

            final ModelObject protocolObject = protocol;
            Assert.assertEquals("experimentType", experimentType.getName(), ProtocolFactory
                .getExperimentType(protocolObject));
            protocol.delete();
            wv.abort();
        } catch (final ConstraintException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } catch (final AccessException ex) {
            Assert.fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test successful
    }

    private static final java.util.Map CRITERIA = new java.util.HashMap();
    static {
        TestProtocol.CRITERIA.put(Protocol.PROP_NAME, "PiMS PCR"); // reference protocol
    }

    /*
     * public void testFindReferenceProtocol() { ReadableVersion version =
     * model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR); try { java.util.Collection
     * protocols = version.findAll(Protocol.class, Collections.EMPTY_MAP) ; assertTrue("installed", 0<protocols.size());
     * protocols = version.findAll(Protocol.class, CRITERIA) ; assertTrue("found", 0<protocols.size()); }
     * finally { version.abort(); } }
     */

    public void testMetaData() {
        final MetaClass pd = this.model.getMetaClass(ParameterDefinition.class.getName());
        final MetaAttribute type = pd.getAttribute("paramType");
        final Constraint constraint = type.getConstraint();
        Constraint.EnumerationConstraint enumConstraint = null;
        if (constraint instanceof Constraint.EnumerationConstraint) {
            enumConstraint = (Constraint.EnumerationConstraint) constraint;
        } else if (constraint instanceof Constraint.And) {
            for (final Constraint aConstraint : ((Constraint.And) constraint).getSubConstraints()) {
                if (aConstraint instanceof Constraint.EnumerationConstraint) {
                    enumConstraint = (Constraint.EnumerationConstraint) aConstraint;
                }
            }
        }

        Assert.assertNotNull("enumeration", enumConstraint);
        // could System.out.println(constraint); //
        // "one_of(Float,Double,Int,Long,String,Boolean)"
        final java.util.Collection values = (enumConstraint).allowedValues;
        Assert.assertTrue("six", 6 <= values.size());
    }
}
