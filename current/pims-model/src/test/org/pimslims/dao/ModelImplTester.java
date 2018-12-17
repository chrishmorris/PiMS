package org.pimslims.dao;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractTestModel;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.PublicEntry;

public class ModelImplTester extends AbstractTestModel {

    public ModelImplTester(String methodName) {
        super(ModelImpl.getModel(), methodName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestModel#testRequiredLater()
     */
    @Override
    public void testRequiredLater() {
        // LATER super.testRequiredLater();
    }

    private static final javax.naming.Context testContext = new javax.naming.Context() {

        @Override
        public Object addToEnvironment(String propName, Object propVal) throws NamingException {

            return null;
        }

        @Override
        public void bind(Name name, Object obj) throws NamingException {

        }

        @Override
        public void bind(String name, Object obj) throws NamingException {

        }

        @Override
        public void close() throws NamingException {

        }

        @Override
        public Name composeName(Name name, Name prefix) throws NamingException {

            return null;
        }

        @Override
        public String composeName(String name, String prefix) throws NamingException {

            return null;
        }

        @Override
        public Context createSubcontext(Name name) throws NamingException {

            return null;
        }

        @Override
        public Context createSubcontext(String name) throws NamingException {

            return null;
        }

        @Override
        public void destroySubcontext(Name name) throws NamingException {

        }

        @Override
        public void destroySubcontext(String name) throws NamingException {

        }

        @Override
        public Hashtable<?, ?> getEnvironment() throws NamingException {

            return null;
        }

        @Override
        public String getNameInNamespace() throws NamingException {

            return null;
        }

        @Override
        public NameParser getNameParser(Name name) throws NamingException {

            return null;
        }

        @Override
        public NameParser getNameParser(String name) throws NamingException {

            return null;
        }

        @Override
        public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {

            return null;
        }

        @Override
        public NamingEnumeration<NameClassPair> list(String name) throws NamingException {

            return null;
        }

        @Override
        public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {

            return null;
        }

        @Override
        public NamingEnumeration<Binding> listBindings(String name) throws NamingException {

            return null;
        }

        @Override
        public Object lookup(Name name) throws NamingException {

            return null;
        }

        @Override
        public Object lookup(String name) throws NamingException {
            if ("db.url".equals(name)) {
                return "test.url";
            }
            return null;
        }

        @Override
        public Object lookupLink(Name name) throws NamingException {

            return null;
        }

        @Override
        public Object lookupLink(String name) throws NamingException {

            return null;
        }

        @Override
        public void rebind(Name name, Object obj) throws NamingException {

        }

        @Override
        public void rebind(String name, Object obj) throws NamingException {

        }

        @Override
        public Object removeFromEnvironment(String propName) throws NamingException {

            return null;
        }

        @Override
        public void rename(Name oldName, Name newName) throws NamingException {

        }

        @Override
        public void rename(String oldName, String newName) throws NamingException {

        }

        @Override
        public void unbind(Name name) throws NamingException {

        }

        @Override
        public void unbind(String name) throws NamingException {

        }
    };

    public void TODOtestGetModel() {
        Context context = testContext;
        new ModelImpl(context);
        assertNotNull(ModelImpl.getInstallationProperties());
    }

    public void testgetByHook() throws AccessException, ConstraintException {
        //ModelImpl impl = (ModelImpl) model;
        WritableVersion wv = this.model.getTestVersion();
        assertNotNull(wv);
        try {
            String hook = org.pimslims.model.people.Person.class.getName() + ":" + wv.getUniqueID();
            assertNull(wv.get(hook));
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            wv.abort();
        }
    }

    public void testClassNames() {
        AbstractModel model = ModelImpl.getModel();
        assertTrue(model.getClassNames().contains(Experiment.class.getName()));
        assertTrue(model.getClassNames().contains(PublicEntry.class.getName()));

    }
}
