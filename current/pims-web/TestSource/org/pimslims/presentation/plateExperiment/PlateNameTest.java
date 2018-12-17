/**
 * pims-web org.pimslims.presentation.plateExperiment OPPFPlateNameTest.java
 * 
 * @author Marc Savitsky
 * @date 5 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.plateExperiment;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * OPPFPlateNameTest
 * 
 */
public class PlateNameTest extends AbstractTestCase {

    private final String holderNamePrefix = "PCR123";

    private final String protocolNamePrefix = "Protocol";

    private final String protocolNameSuffix = "Protocol";

    /**
     * Constructor for OPPFExperimentNameTest
     * 
     * @param name
     */
    public PlateNameTest(final String methodName) {
        super(methodName);
    }

    /**
     * OPPFExperimentNameTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();
    }

    /**
     * OPPFExperimentNameTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testSuggestOPPFPlateName1() throws ConstraintException, AccessException {

        try {

            final Holder holder =
                POJOFactory.create(this.wv, Holder.class, AbstractHolder.PROP_NAME, this.holderNamePrefix
                    + " holder");
            final Protocol protocol =
                POJOFactory.create(this.wv, Protocol.class, Protocol.PROP_NAME, this.protocolNamePrefix
                    + " - " + this.protocolNameSuffix);

            final PlateNameFactory enf =
                PropertyGetter.getInstance("Plate.Name.Factory", OPPFPlateName.class);

            final Holder myHolder = POJOFactory.create(this.wv, Holder.class);
            myHolder.setName(enf.suggestPlateName(this.wv, holder, protocol));

            System.out.println("Base experiment name [" + myHolder.getName() + "]");
            Assert.assertEquals(this.holderNamePrefix + " " + this.protocolNameSuffix + " 1", myHolder
                .getName());

            final String name = enf.suggestPlateName(this.wv, holder, protocol);
            System.out.println("Got  experiment name [" + name + "]");
            Assert.assertEquals(this.holderNamePrefix + " " + this.protocolNameSuffix + " 2", name);

        } finally {
            this.wv.abort();
        }
    }

    public final void testSuggestOPPFPlateName2() throws ConstraintException, AccessException {

        try {

            final Holder holder =
                POJOFactory.create(this.wv, Holder.class, AbstractHolder.PROP_NAME, this.holderNamePrefix
                    + "holder");
            final Protocol protocol =
                POJOFactory.create(this.wv, Protocol.class, Protocol.PROP_NAME, this.protocolNamePrefix + " "
                    + this.protocolNameSuffix);

            final PlateNameFactory enf =
                PropertyGetter.getInstance("Plate.Name.Factory", OPPFPlateName.class);

            final Holder myHolder = POJOFactory.create(this.wv, Holder.class);
            myHolder.setName(enf.suggestPlateName(this.wv, holder, protocol));

            System.out.println("Base experiment name [" + myHolder.getName() + "]");
            Assert.assertEquals(this.holderNamePrefix + " " + this.protocolNamePrefix + " "
                + this.protocolNameSuffix + " 1", myHolder.getName());

            final String name = enf.suggestPlateName(this.wv, holder, protocol);
            System.out.println("Got  experiment name [" + name + "]");
            Assert.assertEquals(this.holderNamePrefix + " " + this.protocolNamePrefix + " "
                + this.protocolNameSuffix + " 2", name);

        } finally {
            this.wv.abort();
        }
    }

    public final void testSuggestDefaultPlateName1() throws ConstraintException, AccessException {

        try {

            final Holder holder =
                POJOFactory.create(this.wv, Holder.class, AbstractHolder.PROP_NAME, this.holderNamePrefix
                    + " holder");
            final Protocol protocol =
                POJOFactory.create(this.wv, Protocol.class, Protocol.PROP_NAME, this.protocolNamePrefix + " "
                    + this.protocolNameSuffix);

            final PlateNameFactory enf =
                PropertyGetter.getInstance("Plate.Name.Factory", DefaultPlateName.class);

            final Holder myHolder = POJOFactory.create(this.wv, Holder.class);
            myHolder.setName(enf.suggestPlateName(this.wv, holder, protocol));

            System.out.println("Base experiment name [" + myHolder.getName() + "]");
            Assert.assertEquals(this.protocolNamePrefix + " " + this.protocolNameSuffix + " 1", myHolder
                .getName());

            final String name = enf.suggestPlateName(this.wv, holder, protocol);
            System.out.println("Got  experiment name [" + name + "]");
            Assert.assertEquals(this.protocolNamePrefix + " " + this.protocolNameSuffix + " 2", name);

        } finally {
            this.wv.abort();
        }
    }

    public final void testSuggestDefaultPlateName2() throws ConstraintException, AccessException {

        try {

            final Holder holder =
                POJOFactory.create(this.wv, Holder.class, AbstractHolder.PROP_NAME, this.holderNamePrefix
                    + "holder");
            final Protocol protocol =
                POJOFactory.create(this.wv, Protocol.class, Protocol.PROP_NAME, this.protocolNamePrefix + " "
                    + this.protocolNameSuffix);

            final PlateNameFactory enf =
                PropertyGetter.getInstance("Plate.Name.Factory", DefaultPlateName.class);

            final Holder myHolder = POJOFactory.create(this.wv, Holder.class);
            myHolder.setName(enf.suggestPlateName(this.wv, holder, protocol));

            System.out.println("Base experiment name [" + myHolder.getName() + "]");
            Assert.assertEquals(this.protocolNamePrefix + " " + this.protocolNameSuffix + " 1", myHolder
                .getName());

            final String name = enf.suggestPlateName(this.wv, holder, protocol);
            System.out.println("Got  experiment name [" + name + "]");
            Assert.assertEquals(this.protocolNamePrefix + " " + this.protocolNameSuffix + " 2", name);

        } finally {
            this.wv.abort();
        }
    }
}
