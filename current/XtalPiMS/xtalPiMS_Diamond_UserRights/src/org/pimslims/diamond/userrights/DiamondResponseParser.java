package org.pimslims.diamond.userrights;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DiamondResponseParser {
	

	private final Document doc;
	private final String[] keywords;

	public DiamondResponseParser(String string)  {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(string.getBytes("UTF-8"));
			this.doc = db.parse(is);
			this.doc.getDocumentElement().normalize();
			is.close();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.keywords = this.doGetKeywords();

	}

	public Map<String, Object>[] getMap() {;
		NodeList rows = doc.getElementsByTagNameNS("http://www.diamond.ac.uk/services/genericws", "row");
		Map<String, Object>[] ret = new Map[rows.getLength()];
		for(int i=0; i<rows.getLength(); i++){
			  Node row = rows.item(i);
			  String content = row.getTextContent();
			  String[] values = content.split("#SEP#");
			  ret[i] = new HashMap(this.keywords.length); 
			  for (int j = 0; j < this.keywords.length; j++) {
				ret[i].put(this.keywords[j], values[j]);
			}
		}
		return ret;
	}

	public String[] getKeywords() {
		return this.keywords;
	}

	private String[] doGetKeywords() {
		NodeList columns = doc.getElementsByTagNameNS("http://www.diamond.ac.uk/services/genericws", "columnNames");
		String[] ret = new String[columns.getLength()];
		for(int i=0; i<columns.getLength(); i++){
			  Node column = columns.item(i);
			  String content = column.getTextContent();
			  ret[i] = content; 
		}
		return ret;
	}

}