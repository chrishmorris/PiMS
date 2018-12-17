/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.target.Target;

/**
 * Tests generated class for Location
 */
public class BookCitationTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test BookCitation
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrBookCitation());

    /**
     * Values for creating an invalid test BookCitation
     */
    public static final HashMap<String, Object> BAD_ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getBadAttrBookCitation());

    public BookCitationTester() {
        super(org.pimslims.model.core.BookCitation.class.getName(), ModelImpl.getModel(),
            "testing implementation of BookCitation class", ATTRIBUTES);
    }

    public void testInvalidAttributes() {
        WritableVersion wv = ModelImpl.getModel().getWritableVersion("administrator");
        try {
            new BookCitation(wv, BAD_ATTRIBUTES);
            fail("Should raise a ConstraintException");
        } catch (ConstraintException e) {
            // A citation must have a title or a year with an author.
            // System.out.println(e.getLocalizedMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    @Override
    protected Map getAdditionalProperties(WritableVersion wv) {
        Map additional = new HashMap();
        try {
            additional.put(Attachment.PROP_PARENTENTRY, create(wv, Target.class));
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (ConstraintException e) {
            throw new RuntimeException(e);
        }
        return additional;
    }

}
