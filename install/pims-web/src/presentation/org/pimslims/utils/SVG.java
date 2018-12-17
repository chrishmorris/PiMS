package org.pimslims.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVG implements Serializable {

    private final byte[] bytes;

    public SVG(String s) {
        bytes = s.getBytes();
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Document getDocument() {

        Document doc = null;
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            doc = f.createDocument(null, new ByteArrayInputStream(getBytes()));

            Element svgElement = doc.getDocumentElement();

            // Make the text look nice.
            svgElement.setAttributeNS(null, "text-rendering", "geometricPrecision");

            // Remove the xml-stylesheet PI.
            for (Node n = svgElement.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
                if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                    doc.removeChild(n);
                    break;
                }
            }

            // Remove the Batik sample mark 'use' element.
            for (Node n = svgElement.getLastChild(); n != null; n = n.getPreviousSibling()) {
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getLocalName().equals("use")) {
                    svgElement.removeChild(n);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Bad SVG document: " + e.getMessage());
            System.out.print(new String(this.bytes));
            throw new RuntimeException(e);
        }

        return doc;
    }

    public String getHeight() {
        Document doc = getDocument();
        Element svgElement = doc.getDocumentElement();
        return svgElement.getAttribute("height");
    }

    public String getWidth() {
        Document doc = getDocument();
        Element svgElement = doc.getDocumentElement();
        return svgElement.getAttribute("width");
    }

    @Override
    public String toString() {
        return new String(bytes);
    }

}
