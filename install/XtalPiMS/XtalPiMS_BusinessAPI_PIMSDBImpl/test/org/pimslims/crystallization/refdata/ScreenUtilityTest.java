package org.pimslims.crystallization.refdata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.pimslims.access.Access;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.molecule.Molecule;

public class ScreenUtilityTest extends TestCase {

    private static final String UNIQUE = "su" + System.currentTimeMillis();

    private static final String SCREEN =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<screen>"
            + "<screenName>"
            + UNIQUE
            + "</screenName>"
            + "<solutions><solution>\r\n"
            + "   <solutionId>1</solutionId>\r\n"
            + "   <localNum>A01</localNum>\r\n"
            + "    <components>\r\n"
            + "     <component name=\"Imidazole malate\" type=\"buffer\" concentration=\"0.2\" concentrationUnits=\"M\" pH=\"5.5\"/>\r\n"
            + "                <component concentrationUnits=\"v/v\" concentration=\"15.0%\" type=\"precipitant\" "
            + "name=\"" + UNIQUE + "\"" + "/>\r\n" + "    </components>\r\n" + "   </solution></solutions>"
            + "</screen>\r\n";

    private final AbstractModel model;

    /**
     * Constructor for ScreenUtilityTest
     * 
     * @param name
     */
    public ScreenUtilityTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testLoadFile() throws ConstraintException, AccessException, JDOMException, IOException,
        BusinessException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ScreenUtility utility = new ScreenUtility(version);
            String string = SCREEN;
            final InputStream input = new ByteArrayInputStream(string.getBytes("UTF-8"));
            RefHolder screen = utility.loadFile(new InputStreamReader(input));
            assertTrue(2 == screen.getHolderCategories().size());

            // now check that it can be found
            DataStorageImpl dataStorage = new DataStorageImpl(version);
            ScreenService service = dataStorage.getScreenService();
            BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ScreenView.PROP_NAME, UNIQUE, true));
            Collection<ScreenView> views = service.findViews(criteria);
            assertEquals(1, views.size());
            Molecule component = version.findFirst(Molecule.class, Molecule.PROP_NAME, UNIQUE);
            assertNotNull(component);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetExistingComponentAlias() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<String> synonyms = Collections.singletonList(UNIQUE + "alias");
            new Molecule(version, "other", UNIQUE).setSynonyms(synonyms);
            ScreenUtility utility = new ScreenUtility(version);
            List<Element> elements = new ArrayList();
            Element element = new Element("component");
            element.setAttribute("name", UNIQUE + "alias");
            element.setAttribute("concentration", "0.001");
            element.setAttribute("concentrationUnits", "w/w");
            elements.add(element);
            List<ComponentQuantity> components = utility.getComponents(elements);
            assertEquals(1, components.size());
            ComponentQuantity cq = components.iterator().next();
            assertEquals(UNIQUE, cq.getComponent().getChemicalName());
        } finally {
            version.abort(); // not testing persistence
        }
    }
}
