/**
 * pims-web org.pimslims.tag ViewButtonTest.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import java.util.Arrays;

import javax.servlet.jsp.JspException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockPageContext;

/**
 * ViewButtonTest
 * 
 */
public class InputTest extends TestCase {

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private final AbstractModel model;

    private final MetaClass metaClass;

    /**
     * @param methodName
     */
    public InputTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
        this.metaClass = this.model.getMetaClass(Organisation.class.getName());
    }

    /**
     * @throws JspException
     */
    public final void testList() throws JspException {
        final Input input = new Input();
        final MockPageContext mockPageContext = new MockPageContext();
        input.setPageContext(mockPageContext);
        final MetaAttribute attribute = this.metaClass.getAttribute(Organisation.PROP_ADDRESSES);
        input.setAttribute(attribute);
        input.setValue(Arrays.asList(new String[] { "aaa", "bbb" }));
        input.doStartTag();
        final String output = mockPageContext.getOutput();
        System.out.println(output + "*");
        Assert.assertTrue(output.startsWith("\n<textarea "));
        Assert.assertTrue(output.contains(" name=\"" + Organisation.PROP_ADDRESSES + "\""));
        Assert.assertTrue(output.contains("aaa\nbbb"));
        Assert.assertTrue(output.endsWith("</textarea>\n"));

    }

    /**
     * @throws JspException
     */
    public final void testText() throws JspException {
        final Input input = new Input();
        final MockPageContext mockPageContext = new MockPageContext();
        input.setPageContext(mockPageContext);
        final MetaAttribute attribute = this.metaClass.getAttribute(Organisation.PROP_CITY);
        input.setAttribute(attribute);
        input.setValue(InputTest.UNIQUE);
        input.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertTrue(output, output.startsWith("\n<input type=\"text\" name=\"city\" id=\"city\" "));
        Assert.assertTrue(output.contains(" value=\"" + InputTest.UNIQUE + "\""));
        Assert.assertTrue(output.contains(" name=\"" + Organisation.PROP_CITY + "\""));
        Assert.assertTrue(output.contains(" id=\"" + Organisation.PROP_CITY + "\""));
        Assert.assertTrue(output, output.endsWith("</span>\n"));
    }

    public final void testBoolean() throws JspException {
        final Input input = new Input();
        final MockPageContext mockPageContext = new MockPageContext();
        input.setPageContext(mockPageContext);
        final MetaAttribute attribute =
            this.model.getMetaClass(Sample.class.getName()).getAttribute(AbstractSample.PROP_ISACTIVE);
        input.setAttribute(attribute);
        input.setValue("No");
        input.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertTrue(output, output.startsWith("\n<select"));
        Assert.assertTrue(output.contains(" name=\"" + AbstractSample.PROP_ISACTIVE + "\""));
        Assert.assertTrue(output.contains(" id=\"" + AbstractSample.PROP_ISACTIVE + "\""));
        Assert
            .assertTrue(
                output,
                output
                    .contains("<option value=\"[not specified]\" label=\"[not specified]\" >[not specified]</option>"));
        Assert.assertTrue(output.contains("<option value=Yes label=Yes >Yes</option>"));
        Assert.assertTrue(output, output.contains("<option value=No label=No  selected >No</option>"));
        Assert.assertTrue(output, output.endsWith("</select><span class=\"selectnoedit\">No</span>\n"));
    }

    public final void testEscape() throws JspException {
        final Input input = new Input();
        final MockPageContext mockPageContext = new MockPageContext();
        input.setPageContext(mockPageContext);
        final MetaAttribute attribute = this.metaClass.getAttribute(Organisation.PROP_CITY);
        input.setAttribute(attribute);
        input.setValue("a\"b");
        input.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertTrue(output, output.contains(" value=\"a&quot;b\""));
    }

    public final void testNoAttribute() throws JspException {
        final Input input = new Input();
        final MockPageContext mockPageContext = new MockPageContext();
        input.setPageContext(mockPageContext);
        //MetaAttribute attribute = this.metaClass.getAttribute(Organisation.PROP_CITY);
        //input.setAttribute(attribute);
        input.setName(InputTest.UNIQUE);
        input.setValue("a\"b");
        input.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertTrue(output, output.contains(" value=\"a&quot;b\""));
    }
}
