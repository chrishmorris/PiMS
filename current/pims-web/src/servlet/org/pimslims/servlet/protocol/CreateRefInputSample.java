package org.pimslims.servlet.protocol;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.model.protocol.RefInputSample;

public class CreateRefInputSample extends CreateWithSampleCategory {

    @Override
    protected String getMetaClassName(HttpServletRequest request) {
        return RefInputSample.class.getName();
    }

}
