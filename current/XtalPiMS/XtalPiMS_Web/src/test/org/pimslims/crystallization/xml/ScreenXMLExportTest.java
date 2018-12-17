package org.pimslims.crystallization.xml;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.pimslims.business.exception.BusinessException;

public class ScreenXMLExportTest extends TestCase {

    public void testExportMorpheusScreen() throws BusinessException, JAXBException {

        final String exportString = ScreenExportUtil.export("Morpheus");
        System.out.println(exportString);
        //general screen info
        assertTrue(exportString.contains("<screenName>Morpheus</screenName>"));
        assertTrue(exportString.contains("<screenSupplier>Molecular Dimensions</screenSupplier>"));
        assertTrue(exportString.contains("<screenDetails>A 96 condition"));
        assertTrue(exportString.contains("<screenCatNum>MD1-47</screenCatNum>"));
        assertTrue(exportString.contains("<screenNumberOfSolutions>96</screenNumberOfSolutions>"));
        //conditions
        assertTrue(exportString.contains("<solutions>"));
        assertTrue(exportString.contains("<solution>"));
        assertTrue(exportString.contains("<solutionId>1-1</solutionId>"));
        assertTrue(exportString.contains("<localNum>A01</localNum>"));
        //components
        assertTrue(exportString.contains("<components>"));
        assertTrue(exportString.contains("name=\"Magnesium Chloride\""));
        assertTrue(exportString.contains("type=\"additive\""));
        assertTrue(exportString.contains("type=\"buffer\""));
        assertTrue(exportString.contains("concentration=\"0.1\""));
        assertTrue(exportString.contains("concentrationUnits=\"M\""));
        assertTrue(exportString.contains("pH=\"6.5"));
    }
}
