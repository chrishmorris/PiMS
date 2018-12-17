package org.pimslims.bioinf.targets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.biojava.bio.seq.io.ReferenceAnnotation;
import org.pimslims.bioinf.targets.PIMSClassesDescription.JournalCitationAttr;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;

/**
 * @author Petr Troshin aka pvt43 AbstractTargetImporter
 * 
 */
@Deprecated
public abstract class AbstractTargetImporter {

    class Citation {
        public ArrayList personsPrms = null; // Parameters for Persons

        public HashMap<String, Object> citationPrms = null;

        public String citationClassName;

        static final int BOOK_CITATION = 0;

        static final int JOURNAL_CITATION = 1;

        static final int THESIS_CITATION = 2;

        static final int CONFERENCE_CITATION = 3;

        public Citation() {
            this.personsPrms = new ArrayList();
            this.citationPrms = new HashMap<String, Object>();
        }

        public Citation(final int citationType, final ArrayList personsPrms) {
            this.personsPrms = personsPrms;
            this.citationPrms = new HashMap<String, Object>();
        }

        public void setCitationType(final int citationType) {
            switch (citationType) {
                case 0:
                    this.citationClassName = org.pimslims.model.core.BookCitation.class.getName();
                    break;
                case 1:
                    this.citationClassName = org.pimslims.model.core.JournalCitation.class.getName();
                    break;
                case 2:
                    this.citationClassName = org.pimslims.model.core.ThesisCitation.class.getName();
                    break;
                case 3:
                    this.citationClassName = org.pimslims.model.core.ConferenceCitation.class.getName();
                    break;
                default:
                    throw new AssertionError("Please specify correct citation type");
            }
        }
    } // Citation class end

    /**
     * @TODO finalize the Citation parsing
     * @param Object could be an ArrayList in case of multiple Citation available otherwise it is a single
     *            instance of ReferenceAnnotation.
     */
    void writeCitation(final Object clist) {

        final Collection list = Util.makeCollection(clist);
        // System.out.println("-------------");

        for (final Iterator iter = list.iterator(); iter.hasNext();) {
            final ReferenceAnnotation elem = (ReferenceAnnotation) iter.next();

            // The RN line start new Citation
            final Citation citationP = new Citation();

            // elem.getProperty("RC");

            if (elem.containsProperty("RT")) {
                citationP.citationPrms.put(JournalCitationAttr.title, ServletTargetCreator.makeString(elem.getProperty("RT"),
                    " "));
            }
            if (elem.containsProperty("RL")) {
                //System.out.println(elem.getProperty("RL"));
                try {
                    SwissProtToPIMS.parseRL((String) elem.getProperty("RL"), citationP);
                } catch (final UnsupportedOperationException e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (elem.containsProperty("RA")) {
                SwissProtToPIMS.parseRA(elem.getProperty("RA"), citationP);
            }
            // elem.getProperty("RP");
            // elem.getProperty("RX");

            // Old all keys
            /*Set s = elem.keys();
            for (Iterator iterator = s.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                if (elem.containsProperty(key)) {
                    // System.out.print("Key: " + key + " ");
                    // System.out.println("Value: " + elem.getProperty(key));
                }
            } */
            // Add ModelObject Citation (which already contains authors to the
            // TargetImporter citations List)
            this.citationsPrms.add(citationP);
        }
        // System.out.println("-------------");
    }

    ArrayList<Citation> citationsPrms = null;

    ArrayList<ModelObject> citations = null;

    HashMap<String, Object> targetPrms = null;

    HashMap<String, Object> moleculePrms = null;

    HashMap<String, Object> nucleotidesPrms = null;

    HashMap annotationPrms = null;

    HashMap<String, String> dbNamePrms = null;

    HashMap<String, Object> dbRefPrms = null;

    HashMap<String, Object> naturalSrcPrms = null;

    HashMap<String, Object> targetGroupPrms = null;

    HashMap<String, Object> prjPrms = null;

    HashMap<String, Object> molComponentPrms = null;

    String dataOwner = "";

    // Model object names
    // TODO search for created NaturalSource prior the creation of the new one
    ModelObject naturalSource = null;

    ModelObject protein = null;

    Collection<ModelObject> nucleotides = null;

    ModelObject target = null;

    ModelObject annotation = null;

    ModelObject dbName = null;

    ModelObject dbRef = null;

    @Deprecated
    ModelObject project = null;

    ModelObject targetStatus = null;

    ModelObject targetGroup = null;

    ModelObject molComponent = null;

    ModelObject person = null;

    public AbstractTargetImporter(final String dataOwner) {
        this.dataOwner = dataOwner;
        this.citationsPrms = new ArrayList<Citation>();
        this.citations = new ArrayList<ModelObject>();
        this.targetPrms = new HashMap<String, Object>();
        this.moleculePrms = new HashMap<String, Object>();
        this.nucleotidesPrms = new HashMap<String, Object>();
        this.annotationPrms = new HashMap();
        this.dbNamePrms = new HashMap<String, String>();
        this.dbRefPrms = new HashMap<String, Object>();
        this.naturalSrcPrms = new HashMap<String, Object>();
        this.prjPrms = new HashMap<String, Object>();
        this.targetGroupPrms = new HashMap<String, Object>();
        this.molComponentPrms = new HashMap<String, Object>();
    }

    public void cleanAllValues() {
        // Clear one-to-many object holders
        this.citationsPrms.clear();
        this.citations.clear();

        // Clear hashmap values
        this.targetPrms.clear();
        this.moleculePrms.clear();
        this.nucleotidesPrms.clear();
        this.annotationPrms.clear();
        this.dbRefPrms.clear();
        this.dbNamePrms.clear();
        this.naturalSrcPrms.clear();
        this.prjPrms.clear();
        this.targetGroupPrms.clear();
        this.molComponentPrms.clear();
        // Delete references
        this.naturalSource = null;
        this.protein = null;
        this.nucleotides = null;
        this.target = null;
        this.annotation = null;
        this.dbName = null;
        this.dbRef = null;
        this.project = null;
        this.targetStatus = null;
        this.targetGroup = null;
        this.molComponent = null;
        this.person = null;
    }

    protected abstract String putToDb();
}
