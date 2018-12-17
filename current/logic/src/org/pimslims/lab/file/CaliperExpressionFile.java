/**
 * pims-web org.pimslims.lab.file CaliperExpressionFile.java
 * 
 * @author Marc Savitsky
 * @date 12 May 2010
 * 
 *       Protein Information Management System
 * @version: 4.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

import org.jdom.JDOMException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.Protocol;

/**
 * CaliperExpressionFile Process the Caliper result file from a protein chip
 * 
 */
public class CaliperExpressionFile extends CaliperFile {

    protected static String OPPF_EXPRESSION = "OPPF Small Scale Expression";
    /**
     * org.pimslims.lab.file.IFileTypeListener Class.forName(CaliperExpressionFile.class.getName());
     */
    static {
        FileFactory.register(new CaliperExpressionFile());
    }

    public CaliperExpressionFile() {
        // Empty constructor
    }

    @Override
    public String getTypeName() {
        return "CaliperExpression";
    }

    @Override
    public boolean isOfThisType(final ReadableVersion version, final String fileName) throws IFileException {
        final Matcher m = CaliperFile.FILE_CALIPER.matcher(fileName);
        if (!m.matches()) {
            return false;
        }

        final ExperimentGroup group =
            CaliperFile.findFromExperimentGroup(version, CaliperFile.getBarcode(fileName));
        if (!group.getExperiments().isEmpty()) {
            final Experiment experiment = group.getExperiments().iterator().next();
            final Protocol protocol = experiment.getProtocol();

            if (!CaliperExpressionFile.OPPF_EXPRESSION.equals(protocol.getName())) {
                return true;
            }
        }

        return false;
    }

    public CaliperExpressionFile(final String name, final InputStream inputStream) throws JDOMException,
        IOException {

        this.fileName = name;

        final BufferedReader d = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = d.readLine()) != null) {
            sb.append(line + "\n");
        }
        this.inputString = sb.toString();
    }

    @Override
    public CaliperExpressionFile getInstance(final String name, final InputStream inputStream)
        throws JDOMException, IOException {
        return new CaliperExpressionFile(name, inputStream);
    }

}
