/**
 * 
 */
package org.pimslims.bioinf;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.biojava.bio.program.sax.BlastLikeSAXParser;
import org.biojava.bio.program.ssbind.BlastLikeSearchBuilder;
import org.biojava.bio.program.ssbind.SeqSimilarityAdapter;
import org.biojava.bio.search.SearchContentHandler;
import org.biojava.bio.seq.db.DummySequenceDB;
import org.biojava.bio.seq.db.DummySequenceDBInstallation;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class based on BioJava cookbook example for parsing blast output to parse the raw output, NOT the xml
 * output, from a single search Parses output as a string not from a file and creates Blast beans
 * 
 * @author Susy Griffiths YSBL 12th June 2007
 */
public class PimsBlastParser {

    /**
     * 
     */
    @Deprecated
    // only tested, not used
    public PimsBlastParser() {
        super();
    }

    /**
     * Creates a List of Blast hits
     * 
     * @param blastOutput
     * @return hits
     */
    @Deprecated
    // only tested, not used
    public static List parseBlastString(final String blastOutput) {
        final List hits = new java.util.ArrayList();
        if ("".equals(blastOutput)) {
            return hits; // no data
        }
        try {
            // get the Blast output string
            final StringReader str = new StringReader(blastOutput);

            // make a BlastLikeSAXParser
            final BlastLikeSAXParser parser = new BlastLikeSAXParser();
            // stop parser from checking version of the Blast report
            parser.setModeLazy();

            // make the SAX event adapter that will pass events to a Handler.
            final SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();

            // set the parsers SAX event adapter
            parser.setContentHandler(adapter);

            // create the SearchContentHandler that will build
            // SeqSimilaritySearchResults
            // in the hits List
            final SearchContentHandler builder =
                new BlastLikeSearchBuilder(hits, new DummySequenceDB("queries"),
                    new DummySequenceDBInstallation());

            // register builder with adapter
            adapter.setSearchContentHandler(builder);

            // parse the file, after this the hits List will be populated with
            // SeqSimilaritySearchResults
            if (!(str == null)) {
                parser.parse(new InputSource(str));
            }

        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e); // should not happen
        }
        return hits;
    }

}
