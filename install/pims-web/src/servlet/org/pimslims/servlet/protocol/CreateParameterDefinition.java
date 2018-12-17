package org.pimslims.servlet.protocol;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.model.protocol.ParameterDefinition;

public class CreateParameterDefinition extends CreateWithSampleCategory {

    @Override
    protected String getMetaClassName(HttpServletRequest request) {
        return ParameterDefinition.class.getName();
    }

}
