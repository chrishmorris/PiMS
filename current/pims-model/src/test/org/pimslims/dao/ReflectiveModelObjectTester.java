/*
 * Created on 15-Oct-2004
 */
package org.pimslims.dao;

import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.people.Organisation;

/**
 * @author cm65
 * 
 */
public class ReflectiveModelObjectTester extends org.pimslims.metamodel.AbstractTestModelObject {

    /**
     * 
     */
    public ReflectiveModelObjectTester() {
        super(ModelImpl.getModel(), "testing ReflectiveModelObject", Organisation.class.getName(),
            OrganisationTester.ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testV0_1Constraints() {
        super.testV0_1Constraints();
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
    /**
     * Test the getProject method
     */
    // public void testGetProject() {
    //
    // WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
    // try {
    // ModelObject testObject = wv.create(null,
    // metaClass.getJavaClass(),attributes);
    //            
    // ccp.api.Implementation.Project memopsProject
    // = ((ReflectiveModelObject)testObject).getProject();
    //            
    // assertEquals("memops project", ((ModelImpl)model).getMemopsProject(),
    // memopsProject);
    //            
    // } catch (final ModelException ex) {
    // ex.printStackTrace();
    // fail(ex.toString());
    // } catch (final ApiException ex) {
    // ex.printStackTrace();
    // fail(ex.toString());
    // } finally {
    // wv.abort(); // not testing persistence here
    // }
    // }
    //    
}
