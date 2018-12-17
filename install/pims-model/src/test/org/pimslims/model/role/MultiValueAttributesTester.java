/**
 * org.pimslims.generatedApi.role MultiValueAttributesTester.java
 * 
 * @date 26-Sep-2006 21:08:48
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
package org.pimslims.model.role;

import java.util.ArrayList;
import java.util.Collections;

import org.hibernate.stat.Statistics;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.persistence.HibernateUtilTester;

/**
 * MultiValueAttributesTester
 * 
 */
public class MultiValueAttributesTester extends org.pimslims.test.AbstractTestCase {
    String hook;

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setPossibleValues(new ArrayList(Collections.singleton("new")));
            hook = pd.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testMultiValueAttributesIsNotChanged() throws ConstraintException {
        final WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final ParameterDefinition pd = (ParameterDefinition) wv.get(hook);
            final Statistics stats = HibernateUtilTester.getStatistics(model);
            final long beforeSave = stats.getCloseStatementCount();

            // no changes and should have no query
            pd.flush();
            assertEquals(beforeSave, stats.getCloseStatementCount());
            wv.save(pd);
            assertEquals(beforeSave, stats.getCloseStatementCount());
            wv.flush();
            assertEquals(beforeSave, stats.getCloseStatementCount());

            wv.abort();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        //final Statistics stats = HibernateUtil.getStatistics();
        //final long beforeSave = stats.getQueryExecutionCount();
    }

}
