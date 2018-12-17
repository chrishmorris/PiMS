package org.pimslims.servlet.protocol;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.model.protocol.RefOutputSample;

public class CreateRefOutputSample extends CreateWithSampleCategory {

    @Override
    protected String getMetaClassName(HttpServletRequest request) {
        return RefOutputSample.class.getName();
    }

}
