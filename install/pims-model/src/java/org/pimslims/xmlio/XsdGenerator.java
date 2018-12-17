/**
 * pims-web org.pimslims.xmlio XsdGenerator.java
 * 
 * @author pajanne
 * @date Jan 12, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * XsdGenerator
 * 
 */
public class XsdGenerator {

    public static void pojoToXsdFile(final Class xmlBeanClass) throws JAXBException, IOException {
        class MySchemaOutputResolver extends SchemaOutputResolver {
            File baseDir = new File("data/xml/");

            @Override
            public Result createOutput(final String namespaceUri, final String suggestedFileName)
                throws IOException {
                return new StreamResult(new File(this.baseDir, suggestedFileName));
            }
        }
        final JAXBContext context = JAXBContext.newInstance(xmlBeanClass);
        context.generateSchema(new MySchemaOutputResolver());
    }

    public static void main(final String[] args) throws JAXBException, IOException {
        XsdGenerator.pojoToXsdFile(ProtocolXmlBean.class);
    }
}
