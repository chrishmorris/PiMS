/**
 * ExtendedTestModel org.pimslims.hibernate ModelObjectTest.java
 * 
 * @author bl67
 * @date 20-Jun-2006
 * 
 *       Protein Information Management System
 * @version: 0.5
 * 
 *           Copyright (c) 2006 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.dao;

import org.pimslims.metamodel.AbstractTestModelObject;
import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.people.Organisation;

/**
 * ModelObjectTest
 * 
 */
public class ModelObjectTester extends AbstractTestModelObject {

    /**
     * @param model
     * @param name
     * @param metaClassName
     * @param attributes
     */
    public ModelObjectTester() {

        super(ModelImpl.getModel(), "testing a model object", Organisation.class.getName(),
            OrganisationTester.ATTRIBUTES);

    }

    /**
     * @see org.pimslims.metamodel.AbstractTestModelObject#getTestAttribute()
     */
    @Override
    public String getTestAttribute() {

        return OrganisationTester.getTestAttribute();
    }

    /**
     * @see org.pimslims.metamodel.AbstractTestModelObject#getNewValue()
     */
    @Override
    public Object getNewValue() {
        return OrganisationTester.getNewValue();
    }

}
