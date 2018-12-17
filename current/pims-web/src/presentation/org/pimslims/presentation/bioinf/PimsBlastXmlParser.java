/**
 * 
 */
package org.pimslims.presentation.bioinf;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pimslims.bioinf.BioinfUtility;
import org.pimslims.bioinf.BlastHeaderBean;
import org.pimslims.bioinf.BlastHitBean;

/**
 * Class to parse the xml returned from ebi Blast web services This is for a single sequence search. TODO
 * modify for multiple sequence results Creates BlastBeans for use in Blast servlet and jsp
 * 
 * @author Susy Griffiths YSBL 8th June 2007
 * 
 */
public class PimsBlastXmlParser {

    /**
     * 
     */
    @Deprecated
    // only tested, not used
    public PimsBlastXmlParser() {
        super();
    }

    /**
     * xml string parser, returns an ArrayList of BlastBeans First bean is BlastHeaderBean rest are
     * BlastHitsBeans
     * 
     * @param xmlString
     * @return
     */
    public static List parseXmlString(final String xmlString) {

        List headerAndSSSR = new java.util.ArrayList();
        final List beans = new java.util.ArrayList();
        List allHits = new java.util.ArrayList();
        List hitList = new java.util.ArrayList();

        try {
            final StringReader str = new StringReader(xmlString);
            final SAXBuilder builder = new SAXBuilder();
            builder.setIgnoringElementContentWhitespace(true);
            final Document doc = builder.build(str);
            final Element rootElement = doc.getRootElement();
            assert "EBIApplicationResult".equals(rootElement.getName());
            headerAndSSSR = rootElement.getChildren();

            // ******Process the header to make BlastHeaderBean********
            final Element headerElement = (Element) headerAndSSSR.get(0);
            assert "Header".equals(headerElement.getName());
            final BlastHeaderBean headerBean = BioinfUtility.makeHeaderBean(headerElement);
            // assert "pdb".equals(headerBean.getDatabaseSearched());
            assert !"".equals(headerBean.getSearchDate());
            // System.out.println("The date is: "+headerBean.getSearchDate());
            beans.add(headerBean);

            // ******Process the hits
            // ******Make one BlastHitBean per SequenceSimilaritySearchResult
            final Element sSSRElement = (Element) headerAndSSSR.get(1);
            assert "SequenceSimilaritySearchResult".equals(sSSRElement.getName());
            allHits = sSSRElement.getChildren();
            final Element hitsElement = (Element) allHits.get(0);
            assert "hits".equals(hitsElement.getName());

            // If search returns no hits do nothing, otherwise, make
            // BlastHitBeans
            final int numHits = Integer.parseInt(hitsElement.getAttributeValue("total"));
            if (numHits > 0) {

                // iterate through the list
                hitList = hitsElement.getChildren();
                for (final Iterator i = hitList.iterator(); i.hasNext();) {
                    final Element hitElement = (Element) i.next();
                    assert "hit".equals(hitElement.getName());
                    final BlastHitBean hitBean = BioinfUtility.makeBlastHitBean(hitElement);
                    beans.add(hitBean);
                }
                assert beans.size() == (numHits + 1) : "Hits: " + numHits + " beans: " + beans.size();
            }

        } catch (final JDOMException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
