package org.pimslims.crystallization.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class XmlLoader {

    protected final DataStorage dataStorage;

    protected final Element document;

    protected static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public XmlLoader(DataStorage dataStorage, Element order) {
        super();
        this.dataStorage = dataStorage;
        this.document = order;
    }

    protected float getFloatContent(Node sample, String childName) {
        float volume = 0;
        try {
            String v = getChildContent((Element) sample, childName);
            volume = Float.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return volume;
    }

    protected int getIntAttribute(Element element, String name) {
        try {
            String i = element.getAttribute(name);
            return Integer.valueOf(i);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Expected an integer for: " + element.getLocalName() + "." + name
                + ", found: " + element.getAttribute(name), e);
        }
    }

    protected String getChildContent(Element parent, String childName) {
        Element type = getChildElement(parent, childName);
        return type.getTextContent().trim();
    }

    /**
     * XmlLoader.save
     * 
     * @return the name of an object to view, to see the new data
     * @throws BusinessException
     */
    public abstract String save() throws BusinessException;

    protected Element getChildElement(Element order, String name) {
        NodeList children = order.getElementsByTagNameNS(getNameSpace(), name);
        assert 0 < children.getLength() : "Expected an element called: " + name;
        Node node = children.item(0);
        return (Element) node;
    }

    protected abstract String getNameSpace();

    protected Iterator<Element> getChildrenIterator(Element element, String name) {
        return new org.pimslims.crystallization.tools.ChildrenIterator(element, name);

    }

    protected static Element parse(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            // now do some magic to handle a missing DTD
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
                        throw new RuntimeException("Cant find DTD: " + publicId);
                    }
                }
            });

            // Parse the file into a DOM Document (org.w3c.dom)

            Document doc = parser.parse(input);
            Element targetsElement = doc.getDocumentElement();
            return targetsElement;
        } catch (SAXException e) {
            throw new RuntimeException("Bad xml file", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}