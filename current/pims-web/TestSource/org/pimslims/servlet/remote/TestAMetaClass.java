/*
 * Created on 25-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.xml.sax.SAXException;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * Generic test class for the servlet interface TODO accept path parameter
 * 
 * @version 0.1
 */
public class TestAMetaClass extends RemoteTest {

    /**
     * Mock container for running the servlets
     */
    // private ServletRunner runner;
    /**
     * model type being tested
     */
    protected final String metaClassName;

    /**
     * model type being tested
     */
    protected final Class javaClass;

    /**
     * name => values, for use when creating an object of the MetaClass
     */
    private final Map createAttributes;

    /**
     * name => values, for use when updating an object of the MetaClass
     */
    private final Map editAttributes;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param metaClassName the name of model type to test
     * @param createAttributes map name => value to use to create
     * @param editAttributes map name => value to use to update
     * @param methodName name of test to run, null for all an instance of the MetaClass
     * @throws MalformedURLException
     */
    protected TestAMetaClass(final String metaClassName, final Map createAttributes,
        final Map editAttributes, final String methodName) throws MalformedURLException {
        super(methodName, new java.net.URL("http://localhost:8080/V2_0/"));
        try {
            this.javaClass = Class.forName(metaClassName);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        this.metaClassName = metaClassName;
        this.createAttributes = createAttributes;
        this.editAttributes = editAttributes;
    }

    /**
     * Check the title of the page
     * 
     * @param response
     */
    protected void checkTitle(final WebResponse response) {
        try {
            if (this.createAttributes.containsKey("name")) {
                final String name = (String) this.createAttributes.get("name");
                final String title = response.getTitle();
                Assert.assertFalse("name not in title: " + title + " " + name, -1 == title.indexOf(name));
            }
        } catch (final SAXException ex) {
            Assert.fail("parse error: " + ex.getMessage());
        }
    }

    /**
     * Check the list page
     * 
     * @throws IOException
     */
    public void testList() throws IOException {

        final WebResponse list = this.get("Search/" + this.metaClassName, java.util.Collections.EMPTY_MAP);
        final String url = list.getURL().toExternalForm();
        Assert.assertFalse("list page: " + url, -1 == url.indexOf("Search/"));
        try {
            this.checkPage(list);
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test the basic operations for objects of this type
     */
    public void test() {
        Map map = new HashMap();

        try {
            // create
            WebResponse response = this.get("Create/" + this.metaClassName, map);
            this.login(response);
            this.checkPage(response);
            map = new HashMap(this.createAttributes);
            map.put("METACLASSNAME", this.metaClassName);
            response = this.post("Create/" + this.metaClassName, map);
            String url = response.getURL().toExternalForm();
            if (url.contains("Create")) {
                System.out.print(response.getText());
                Assert.fail("error message returned");
            }
            Assert.assertTrue("not created: " + url, url.contains("View"));

            // check view page
            this.checkTitle(response);
            this.checkPage(response);
            final WebTable[] tables = response.getTables();
            Assert.assertEquals("not 1 table in view", 1, tables.length);
            /*
             * LATER assertTrue("must be some values", 0 < response.getElementsWithAttribute("class",
             * "data").length );
             */
            // TODO check the creation attributes can be read
            // check link to list page
            WebLink link = response.getLinkWith("List");
            Assert.assertNotNull("list link", link);
            WebResponse list = this.click(link.getRequest());
            url = list.getURL().toExternalForm();
            Assert.assertFalse("list page: " + url, -1 == url.indexOf("Search"));
            list = null; // now discard that page

            // get edit page
            WebForm form = null;
            link = response.getLinkWith("Edit");
            if (null == link) {
                Assert.fail("may not edit");
            }
            Assert.assertNotNull("edit link", link);
            response = this.click(link.getRequest());
            this.checkTitle(response);
            this.checkPage(response);

            /*
             * LATER check cancel button link = response.getLinkWith("Abandon"); assertNotNull("cancel link",
             * link); WebResponse cancelled = click(link, "Edit page"); url =
             * cancelled.getURL().toExternalForm(); assertFalse("next URL after edit:
             * "+url,-1==url.indexOf("View/")); cancelled = null; // now discard that page
             */

            // edit
            map = new HashMap(this.editAttributes);
            form = response.getForms()[1];
            url = form.getAction();
            response = this.post(form.getAction(), map);

            // check are back to view
            url = response.getURL().toExternalForm();
            Assert.assertFalse("next URL after edit: " + url, -1 == url.indexOf("View/"));
            // delete it
            form = response.getForms()[1];
            url = form.getAction();
            Assert.assertTrue("delete form action: " + url, url.contains("Delete?"));
            response = form.submit();
            final String title = response.getTitle();
            Assert.assertFalse("not deleted: " + title, -1 == title.indexOf("deleted"));
            // TODO checkPage(response);

        } catch (final SAXException ex) {
            Assert.fail(ex.getMessage());
        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }

    }
}
