/**
 * current-pims-web org.pimslims.command RefDataLoaderTest.java
 * 
 * @author bl67
 * @date 4 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command;

import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.jdom.JDOMException;
import org.pimslims.AllToolsTests;
import org.pimslims.data.GenericRefLoader;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.test.AbstractTestCase;

/**
 * RefDataLoaderTest TODO this data is only for Leeds. But we need a test for the generic loader.
 */
public class GenericRefLoaderTest extends AbstractTestCase {
    String holderFilename = AllToolsTests.TestingDataPath + "/Holders.csv";

    //this is the test for refdata-modifs 20071003 and 20071004
    public void testLoaderHolder() throws AccessException, ConstraintException, IOException, SQLException,
        JDOMException, ClassNotFoundException {
        this.wv = this.getWV();
        try {
            AbstractModelObject holder = this.wv.findFirst(Holder.class, AbstractHolder.PROP_NAME, "card");
            if (holder != null) {
                holder.delete();
            }

            GenericRefLoader.loadFile(this.wv, Holder.class.getName(), this.holderFilename);

            holder = this.wv.findFirst(Holder.class, AbstractHolder.PROP_NAME, "card");
            Assert.assertNotNull(holder);
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

}
