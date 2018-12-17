/**
 * pims-web org.pimslims.servlet.ajax AjaxExistsTest.java
 * 
 * @author bl67
 * @date 12 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.1
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.ajax;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.AjaxExists;
import org.pimslims.test.AbstractTestCase;

/**
 * AjaxExistsTest
 * 
 */
public class AjaxExistsTest extends AbstractTestCase {

    String existedSampleName;

    String existedSampleHook;

    public void testDoGet() throws ServletException, IOException {
        final AjaxExists servlet = new AjaxExists();
        servlet.init(new MockServletConfig(AbstractTestCase.model));
        final String className = Sample.class.getName();
        final Map parameters = new HashMap<String, String[]>();
        parameters.put(AbstractSample.PROP_NAME, new String[] { this.existedSampleName });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", new MockHttpSession(Access.ADMINISTRATOR), parameters);
        request.setPathInfo("\\" + className);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doGet(request, response);
        Assert.assertTrue(response.getOutput().contains(this.existedSampleHook));

    }

    public void testDoPostErrorClass() throws ServletException, IOException {
        final AjaxExists servlet = new AjaxExists();
        servlet.init(new MockServletConfig(AbstractTestCase.model));
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", new MockHttpSession(Access.ADMINISTRATOR),
                Collections.EMPTY_MAP);
        final String badClassName = Sample.class.getName() + "_";
        request.setPathInfo("\\" + badClassName);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

    }

    @Override
    public void setUp() throws AbortedException, ConstraintException, AccessException {
        //create a sample
        this.wv = this.getWV();
        try {
            final Sample sample = this.create(Sample.class);
            this.existedSampleName = sample.getName();
            this.existedSampleHook = sample.get_Hook();
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();

            }
        }

    }

    @Override
    public void tearDown() throws Exception {

        //delete the sample created in setup
        this.wv = this.getWV();
        try {
            final Sample sample =
                this.wv.findFirst(Sample.class, AbstractSample.PROP_NAME, this.existedSampleName);
            Assert.assertNotNull(sample);
            sample.delete();
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();

            }
        }
        super.tearDown();

    }
}
