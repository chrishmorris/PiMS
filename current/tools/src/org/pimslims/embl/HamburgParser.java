package org.pimslims.embl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HamburgParser {

    private static final String MG_PER_ML = "mg/ml";

    public static Collection<HamburgResearchObjectiveBean> parse(InputStream input) throws IOException {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            // now do some magic to handle the fact that the DTD is missing
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder parser = factory.newDocumentBuilder();
            parser.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId)
                    throws SAXException, java.io.IOException {
                    if (null == publicId) {
                        // return dummy dtd
                        return new InputSource(new ByteArrayInputStream(
                            "<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
                    } else {
                        throw new RuntimeException("Cant resolve: " + publicId);
                    }
                }
            });

            // Parse the file into a DOM Document (org.w3c.dom)
            Document doc = parser.parse(input);
            Element targetsElement = doc.getDocumentElement();
            assert "targets".equals(targetsElement.getTagName());
            Collection<HamburgResearchObjectiveBean> ret = new ArrayList();
            for (Node childNode = targetsElement.getFirstChild(); childNode != null; childNode =
                childNode.getNextSibling()) {
                if (childNode instanceof Text) {
                    // for some reason the set ignoring white space above fails
                    continue;
                }
                HamburgResearchObjectiveBean bean = processRO((Element) childNode);
                ret.add(bean);
            }
            return ret;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new AssertionError("Bad document:" + e.getMessage());
        }

    }

    // process a Hamburg "target" element
    private static HamburgResearchObjectiveBean processRO(Element target) {
        assert "target".equals(target.getTagName()) : "Expected target, found:" + target.getNodeType()
            + target.getTagName();
        Collection<HamburgOrfBean> orfs = new HashSet();
        Collection<HamburgProjectBean> projects = new HashSet();
        String name = "unnamed research objective";
        String description = "no description";
        String workpackage = "";
        DnaSequence dnaSequence = null;
        for (Node childNode = target.getFirstChild(); childNode != null; childNode =
            childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            String content = element.getTextContent().trim();
            if ("HASH".equals(content) || "".equals(content)) {
                // no action needed
            } else if ("ID".equals(tagName)) {
                name = content;
            } else if ("description".equals(tagName)) {
                description = content;
            } else if ("project".equals(tagName)) {
                HamburgProjectBean project = processProject(element);
                if (null == project) {
                    System.out.println("Ignoring internal project for target: " + name);
                } else {
                    projects.add(project);
                }
            } else if ("genesequence".equals(tagName)) {
                // this tag is logically misplaced, should be in an orf
                try {
                    dnaSequence = new DnaSequence(content);
                } catch (IllegalArgumentException e) {
                    System.out.println("Not a DNA sequence: " + content);
                }
            } else if ("annotationUrl".equals(tagName)) {
                assert "(gene sequence)".equals(content);
            } else if ("workpackage".equals(tagName)) {
                workpackage = content;
            } else if ("orf".equals(tagName)) {
                orfs.add(processOrf(element));
            } else {
                throw new AssertionError("Unexpected tag: " + tagName);
            }

        }
        if (null != dnaSequence) {
            if (1 == orfs.size()) {
                orfs.iterator().next().setDnaSequence(dnaSequence.getSequence());
            } else {
                System.out.println("DNA sequence specified, but more than one ORF");
            }
        }
        HamburgResearchObjectiveBean ret =
            new HamburgResearchObjectiveBean(name, description, orfs, projects, workpackage);
        return ret;
    }

    private static HamburgOrfBean processOrf(Element orf) {
        String species = "unknown species";
        String sequence = "";
        String name = "unknown orf";
        String accession = "unknown accession";
        String dbname = null;

        for (Node childNode = orf.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            String content = element.getTextContent().trim();
            if ("specie".equals(tagName)) {
                species = content;
            } else if ("taxoid".equals(tagName)) {
                assert "".equals(content);
            } else if ("sequence".equals(tagName)) {
                if (!"(protein sequence)".equals(content) && !"(PROTEINSEQUENCE)".equals(content)) {

                    sequence = new ProteinSequence(content).getSequence();

                }
            } else if ("name".equals(tagName)) {
                name = content.trim();
            } else if ("dbaccnum".equals(tagName)) {
                accession = content.trim();
            } else if ("clond".equals(tagName)) {
                assert "HASH".equals(content);
            } else if ("dbname".equals(tagName)) {
                dbname = content.trim();
            } else {
                throw new AssertionError("Unexpected tag: " + tagName);
            }
        }
        return new HamburgOrfBean(name, sequence, species, dbname, accession);
    }

    private static HamburgProjectBean processProject(Element project) {
        String description = "no description";
        String id = project.getAttribute("id");
        HamburgStatusBean status = null;
        HamburgExperimentBean experiment = null;
        String contact = null;
        Collection<DbLinkBean> dblinks = new HashSet();
        for (Node childNode = project.getFirstChild(); childNode != null; childNode =
            childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            if ("dblink".equals(tagName)) {
                dblinks.add(processDbLink(element));
            } else if ("experiment".equals(tagName)) {
                assert null == experiment : "Too many experiment elements";
                experiment = processExperiment(element);
            } else if ("status".equals(tagName)) {
                assert null == status : "Too many status elements";
                status = processStatus(element);
            } else {
                String content = element.getTextContent();
                if ("description".equals(tagName)) {
                    description = content.trim();
                } else if ("access".equals(tagName)) {
                    if ("Internal".equals(content)) {
                        return null;
                    }
                    assert "Public".equals(content) : "Access: " + content;
                } else {
                    throw new AssertionError("Unexpected tag: " + tagName);
                }
            }
        }
        HamburgProjectBean ret =
            new HamburgProjectBean(id, status.getTask(), status.getLabID(), status.getLab(), status.getPi(),
                "Public", description, status.getContact(), status.getRemarks(), experiment, dblinks);
        return ret;
    }

    private static DbLinkBean processDbLink(Element dbLink) {
        String accession = null;
        String dbName = null;
        for (Node childNode = dbLink.getFirstChild(); childNode != null; childNode =
            childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            String content = element.getTextContent().trim();
            if ("".equals(content) || "?".equals(content)) {
                // no action required
            } else if ("dbname".equals(tagName)) {
                dbName = content;
            } else if ("dbaccnum".equals(tagName)) {
                accession = content;
            }
        }
        return new DbLinkBean(dbName, accession);
    }

    private static HamburgStatusBean processStatus(Element status) {
        String task = "unknown task";
        String location = "unknown location";
        String pi = "unknown pi";
        String contact = null;
        String remarks = "";
        for (Node childNode = status.getFirstChild(); childNode != null; childNode =
            childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            String content = element.getTextContent().trim();
            if ("".equals(content) || "?".equals(content) || "not available".equals(content)) {
                // no action required
            } else if ("contact".equals(tagName)) {
                contact = content;
            } else if ("lab".equals(tagName)) {
                assert "EMBL Hamburg".equals(content);
            } else if ("task".equals(tagName)) {
                task = content;
            } else if ("PI".equals(tagName)) {
                pi = content;
            } else if ("labID".equals(tagName)) {
                location = content;
            } else if ("remarks".equals(tagName)) {
                remarks = content;
            } else {
                throw new AssertionError("Unexpected tag: " + tagName);
            }
        }
        return new HamburgStatusBean(task, pi, location, "EMBL Hamburg", contact, remarks);

    }

    private static HamburgExperimentBean processExperiment(Element experiment) {
        String quality = "unknown expression quality";
        Float level = null;
        String strain = "unknown expression strain";
        String vector = "unknown expression vector";
        String constructDescription = "";
        String crystalSize = "";
        String resolution = "";
        String ligand = "";
        String condition = "";
        String crystalForm = "";
        for (Node childNode = experiment.getFirstChild(); childNode != null; childNode =
            childNode.getNextSibling()) {
            if (childNode instanceof Text) {
                // for some reason the set ignoring white space above fails
                continue;
            }
            Element element = (Element) childNode;
            String tagName = element.getTagName();
            String content = element.getTextContent().trim();
            if ("".equals(content) || "not available".equals(content)) {
                // no action needed
            } else if ("available as attachment".equals(content)) {
                System.out.println("Unable to save: " + tagName + " content is: \"" + content + "\"");
            } else if ("exprv".equals(tagName)) {
                vector = content;
            } else if ("xtldm".equals(tagName)) {
                crystalSize = content;
            } else if ("xtlrs".equals(tagName)) {
                resolution = content;
            } else if ("xtalc".equals(tagName)) {
                condition += content;
            } else if ("exprc".equals(tagName)) {
                condition += content;
            } else if ("xtlsh".equals(tagName)) {
                crystalForm = content;
            } else if ("lignd".equals(tagName)) {
                ligand = content;
            } else if ("exprs".equals(tagName)) {
                strain = content;
            } else if ("clond".equals(tagName)) {
                constructDescription = content;
            } else if ("expry".equals(tagName)) {
                quality = content;
                try {
                    if (quality.endsWith(MG_PER_ML)) {
                        level = Float.valueOf(quality.substring(0, quality.length() - MG_PER_ML.length()));
                        quality = "OK";
                    }
                } catch (NumberFormatException e) {
                    // can ignore this, content will be saved in "quality"
                }
            } else {
                throw new AssertionError("Unexpected tag: " + tagName + " content: " + content);
            }
        }
        return new HamburgExperimentBean(quality, level, strain, vector, constructDescription, crystalSize,
            resolution, ligand, condition, crystalForm);

    }

    // to make it easier to test
    public static Collection<HamburgResearchObjectiveBean> parse(String string) throws IOException {
        InputStream input = new ByteArrayInputStream(string.getBytes("ISO-8859-1"));
        return parse(input);
    }
}
