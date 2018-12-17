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
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.JournalCitation;
import org.pimslims.model.target.Target;

/**
 * Tests generated class n
 */
public class JournalCitationTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test instance
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>();
    static {
        ATTRIBUTES.put(Citation.PROP_TITLE, "pims title");
    }

    public JournalCitationTester() {
        super(org.pimslims.model.core.JournalCitation.class.getName(), ModelImpl.getModel(),
            "testing implementation of BookCitation class", ATTRIBUTES);
    }

    /**
     * @see org.pimslims.metamodel.TestAMetaClass#doTestVersion(org.pimslims.dao.WritableVersion)
     */
    @Override
    protected ModelObject doTestVersion(WritableVersion wv) throws ConstraintException, AccessException {
        Citation citation = (JournalCitation) super.doTestVersion(wv);

        // check it uses the year
        citation.setYear(2006);
        System.out.println(citation.get_Name());
        assertTrue(citation.get_Name().endsWith("2006"));

        citation.setAuthors("Morris");
        System.out.println(citation.get_Name());
        assertEquals("Morris 2006", citation.get_Name());

        return citation;
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
