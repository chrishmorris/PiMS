/**
 * pims-web org.pimslims.lab.experiment ExperimentAkta.java
 * 
 * @author Marc Savitsky
 * @date 6 May 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.properties.PropertyGetter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ExperimentAkta
 * 
 */
@Deprecated
// the import does not work
public class AktaFile implements IFileType {

    static {
        FileFactory.register(new AktaFile());
    }

    enum aktaParameter {
        Fractions, Volume, Yield, Protein, Unicorn,
    }

    private static Document mapping;

    public static InputStream inputStream = null;

    private static final Pattern FILE_EXPERIMENT_AKTA = Pattern.compile("^(.+).akta$");

    /**
     * Constructor for AktaFile
     */
    public AktaFile() {
        // 
    }

    public AktaFile(final InputStream inputStream) throws JDOMException, IOException {

        final java.io.File file = PropertyGetter.getFileProperty("AktaImportMapping", "conf/AktaMapping.xml");
        final SAXBuilder builder = new SAXBuilder();

        AktaFile.mapping = builder.build(file);
        AktaFile.inputStream = inputStream;
    }

    /**
     * 
     * CaliperResultFile.isCaliperResultFile
     * 
     * @param name
     * @return
     */
    public boolean isOfThisType(final ReadableVersion version, final String name) {
        System.out.println("AktaFile.isOfThisType [" + name + "]");
        final Matcher m = AktaFile.FILE_EXPERIMENT_AKTA.matcher(name);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    public void processAsAttachment(final ModelObject object) {
        /* System.out.println("AktaFile.processAsAttachment");
        try {
            if (object instanceof Experiment) {

                final Experiment experiment = (Experiment) object;
                final GZIPInputStream gs = new GZIPInputStream(AktaFile.inputStream);
                final ObjectInputStream ois = new ObjectInputStream(gs);
                final OPCHDAExperiment opchdaExperiment = (OPCHDAExperiment) ois.readObject();
                ois.close();
                AktaFile.inputStream.close();

                final Collection<Parameter> parameters = experiment.getParameters();

                for (final Parameter parameter : parameters) {
                    System.out.println("parameter [" + parameter.getName() + "]");
                    final MappingElement mElement = this.getMapping(parameter.getName());

                    if (null != mElement) {
                        System.out.println("MappingElement [" + mElement.getPiMSParameter() + ":"
                            + mElement.getAktaParameter() + ":" + mElement.getPool() + "]");
                        String pooledFraction;

                        switch (aktaParameter.valueOf(mElement.getAktaParameter())) {

                            case Fractions:
                                pooledFraction = opchdaExperiment.getPooledFraction();
                                if (null != pooledFraction) {
                                    parameter.setValue(AktaFile.getFractions(pooledFraction,
                                        mElement.getPool()));
                                }
                                break;

                            case Protein:
                                pooledFraction = opchdaExperiment.getPooledFraction();
                                if (null != pooledFraction) {
                                    parameter
                                        .setValue(AktaFile.getProtein(pooledFraction, mElement.getPool()));
                                }
                                break;

                            case Unicorn:
                                parameter.setValue(AktaFile.getUnicorn(opchdaExperiment));
                                break;

                            case Volume:
                                pooledFraction = opchdaExperiment.getPooledFraction();
                                if (null != pooledFraction) {
                                    parameter
                                        .setValue(AktaFile.getVolume(pooledFraction, mElement.getPool()));
                                }
                                break;

                            case Yield:
                                pooledFraction = opchdaExperiment.getPooledFraction();
                                if (null != pooledFraction) {
                                    parameter.setValue(AktaFile.getYield(pooledFraction, mElement.getPool()));
                                }
                                break;

                            default:
                                System.out.println("ExperimentAkta.setParameterValues Default value ["
                                    + mElement.getAktaParameter() + "]");
                        }
                    }
                }
            }

        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } */

    }

    public static String getFractions(final String pooledFraction, final int pool)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        System.out.println("getFractions [" + pooledFraction + ":" + pool + "]");
        final Reader reader = new StringReader(pooledFraction);
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        final DocumentBuilder builder = domFactory.newDocumentBuilder();
        final org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(reader));
        final NodeList nodes = doc.getElementsByTagName("Pool");

        if (pool <= nodes.getLength()) {
            final Node node = nodes.item(pool - 1);
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xpath = factory.newXPath();
            final String leftFrac = xpath.evaluate("LeftFrac", node);
            final String rightFrac = xpath.evaluate("RightFrac", node);
            return leftFrac + "-" + rightFrac;
        }

        return null;
    }

/*
    private static String getUnicorn(final OPCHDAExperiment opchdaExperiment) {

        return opchdaExperiment.getResultName();
    } */

    public static String getVolume(final String pooledFraction, final int pool)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        final Reader reader = new StringReader(pooledFraction);
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        final DocumentBuilder builder = domFactory.newDocumentBuilder();
        final org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(reader));
        final NodeList nodes = doc.getElementsByTagName("Pool");

        if (pool <= nodes.getLength()) {
            final Node node = nodes.item(pool - 1);
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xpath = factory.newXPath();
            final String volume = xpath.evaluate("Volume", node);
            //final String unit = xpath.evaluate("Volume/@Unit", node);
            return volume;
        }

        return null;
    }

    public static String getYield(final String pooledFraction, final int pool)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        final Reader reader = new StringReader(pooledFraction);
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        final DocumentBuilder builder = domFactory.newDocumentBuilder();
        final org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(reader));
        final NodeList nodes = doc.getElementsByTagName("Pool");

        if (pool <= nodes.getLength()) {
            final Node node = nodes.item(pool - 1);
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xpath = factory.newXPath();
            return xpath.evaluate("Amount", node);
        }

        return null;
    }

    public static String getProtein(final String pooledFraction, final int pool)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        final Reader reader = new StringReader(pooledFraction);
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        final DocumentBuilder builder = domFactory.newDocumentBuilder();
        final org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(reader));
        final NodeList nodes = doc.getElementsByTagName("Pool");

        if (pool <= nodes.getLength()) {
            final Node node = nodes.item(pool - 1);
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xpath = factory.newXPath();
            return xpath.evaluate("Conc", node);
        }

        return null;
    }

    private MappingElement getMapping(final String parameter) {

        for (final Iterator it = AktaFile.mapping.getDescendants(new ElementFilter("Parameter")); it
            .hasNext();) {
            final Element element = (Element) it.next();
            if (element.getTextTrim().equals(parameter)) {
                return new MappingElement(element);
            }
        }
        return null;
    }

    class MappingElement {

        private final String pimsParameter;

        private final String aktaParameter;

        private int pool = 0;

        MappingElement(final Element element) {
            this.pimsParameter = element.getTextTrim();
            this.aktaParameter = element.getAttributeValue("akta");
            if (null != element.getAttributeValue("pool")) {
                this.pool = Integer.parseInt(element.getAttributeValue("pool"));
            }
        }

        public String getAktaParameter() {
            return this.aktaParameter;
        }

        public String getPiMSParameter() {
            return this.pimsParameter;
        }

        public int getPool() {
            return this.pool;
        }
    }

    /**
     * IFile.processToPiMS
     * 
     * @see org.pimslims.lab.file.IFile#processToPiMS(org.pimslims.dao.WritableVersion, java.io.InputStream)
     */
    public ModelObject process() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * IFileType.process
     * 
     * @see org.pimslims.lab.file.IFileType#process(org.pimslims.dao.WritableVersion)
     */
    public ModelObject process(final WritableVersion version) throws IFileException, ConstraintException,
        UnsupportedEncodingException, IOException, AccessException {
        // TODO Auto-generated method stub
        return null;
    }

    public void close() throws IOException {
        AktaFile.inputStream.close();
    }

    /**
     * IFileType.getInstance
     * 
     * @see org.pimslims.lab.file.IFileType#getInstance(java.lang.String, java.io.InputStream)
     */
    public IFileType getInstance(final String name, final InputStream inputStream) throws JDOMException,
        IOException {
        System.out.println("AktaFile.getInstance [" + name + "]");
        return new AktaFile(inputStream);
    }

    /**
     * IFileType.getTypeName
     * 
     * @see org.pimslims.lab.file.IFileType#getTypeName()
     */
    public String getTypeName() {
        return "Akta";
    }

}
