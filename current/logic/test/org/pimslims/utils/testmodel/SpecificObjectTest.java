/*
 * Created on 15.07.2005 - Code Style - Code Templates
 */
package org.pimslims.utils.testmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.JournalCitation;
import org.pimslims.model.people.Person;
import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin
 */
@Deprecated
// obsolete
public class SpecificObjectTest {

    private final AbstractModel model = org.pimslims.dao.ModelImpl.getModel();

    private final WritableVersion rw = this.model.getWritableVersion(AbstractModel.SUPERUSER);

    /**
     * @throws ConstraintException
     * 
     */
    public SpecificObjectTest() throws ConstraintException {
        this.testJournalCitation();
        this.testTarget();
    }

    void testJournalCitation() {

        // Trying work of specific object methods
        final ModelObjectsTest mot = new ModelObjectsTest(ModelObjectsTest.logLevelNull);
        final ModelObject mobj = mot.constructModelObject("org.pimslims.model.core.JournalCitation", true);

        final JournalCitation jc = (JournalCitation) mobj;
        final Set<Person> authors = new HashSet<Person>();
        final HashMap prp1 = new HashMap();
        prp1.put("familyName", "AAAA");
        final HashMap prp2 = new HashMap();
        prp2.put("familyName", "BBBB");
        try {
            final Person auth1 = (this.rw.create(Person.class, prp1));
            final Person auth2 = (this.rw.create(Person.class, prp1));
            authors.add(auth1);
            authors.add(auth2);
            // System.out.println(auth1.getFamilyName());
            // TODO CHECK MODEL CHANGE: authors are stored as String in Citation
            jc.setAuthors("Author names");
            // ArrayList res = jc.findAllAuthors("authors", auth1);
            // System.out.println(res);

        } catch (final ConstraintException cex) {
            throw new RuntimeException(cex);
        } catch (final AccessException aex) {
            throw new RuntimeException(aex);
        }

    }

    void testTarget() throws ConstraintException {
        final ModelObjectsTest mot1 = new ModelObjectsTest(ModelObjectsTest.logLevelNull);
        mot1.skippedObjects.add("org.pimslims.model.core.AccessObject");
        mot1.skippedObjects.add("org.pimslims.model.reference.TargetStatus"); // no method
        // setStatus(List)
        mot1.skippedObjects.add("org.pimslims.model.target.TargetDbRef");// no method
        // setTargetDBrefs(List)
        final ModelObject mcitation = mot1.constructModelObject(JournalCitation.class.getName(), true);
        final ModelObject mtarget = mot1.constructModelObject(Target.class.getName(), true, true, 1);
        // ModelObject mobj = mot1.constructModelObject(Status.class.getName(),
        // true);

        final Target t = (Target) mtarget;
        final JournalCitation jc = (JournalCitation) mcitation;
        // TODO CHECK MODEL CHANGE: citation must have a parent
        t.addAttachment(jc);

        // System.out.println(fcit);

        // mobj.getValues();
        // t.
        // ArrayList authors = new ArrayList();
        // HashMap prp1 = new HashMap();
        // HashMap prp2 = new HashMap();

    }

    public static void main(final String[] args) throws ConstraintException {
        new SpecificObjectTest();
    }
}
