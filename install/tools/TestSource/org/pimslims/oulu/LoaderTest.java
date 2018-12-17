/**
 * tools org.pimslims.oulu LoaderTest.java
 * 
 * @author cm65
 * @date 21 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.oulu;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;

/**
 * LoaderTest
 * 
 */
public class LoaderTest extends TestCase {

    private final AbstractModel model;

    /**
     * Constructor for LoaderTest
     * 
     * @param name
     */
    public LoaderTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    private static final String UNIQUE = "ol" + System.currentTimeMillis();

    /**
     * CONSTRUCT_NAME String
     */
    private static final String CONSTRUCT_NAME = "co" + UNIQUE;

    /**
     * Test method for {@link org.pimslims.oulu.Loader#getVector(java.lang.String)}.
     * 
     * @throws ConstraintException
     */
    public void testGetVector() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Loader loader = new Loader(version);
            Construct vector = loader.getVector(UNIQUE);
            assertEquals(UNIQUE + " Vector", vector.getName());
            Construct vector2 = loader.getVector(UNIQUE);
            assertEquals(vector, vector2);
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for {@link org.pimslims.oulu.Loader#getSpecies(java.lang.String)}.
     * 
     * @throws ConstraintException
     */
    public void testGetSpecies() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Loader loader = new Loader(version);
            Organism species = loader.getSpecies(UNIQUE);
            assertEquals(UNIQUE, species.getName());
            Organism species2 = loader.getSpecies(UNIQUE);
            assertEquals(species, species2);
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.oulu.Loader#getTarget(java.lang.String, org.pimslims.model.reference.Organism, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ConstraintException
     */
    public void testGetTarget() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Loader loader = new Loader(version);
            Organism species = loader.getSpecies(UNIQUE);
            Target target = loader.getTarget(UNIQUE, species, "CATG");
            assertEquals(UNIQUE, target.getName());
            assertEquals(species, target.getSpecies());
            assertEquals(1, target.getNucleicAcids().size());
            //TODO is this right?
            assertEquals("CATG", target.getNucleicAcids().iterator().next().getSeqString());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.oulu.Loader#getTarget(java.lang.String, org.pimslims.model.reference.Organism, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ConstraintException
     */
    public void testGetTargetTwice() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Loader loader = new Loader(version);
            Target target = loader.getTarget(UNIQUE, null, "CATG");
            Target target2 = loader.getTarget(UNIQUE, null, "CATG");
            assertEquals(target, target2);
        } finally {
            version.abort();
        }
    }

    public void testGetConstruct() throws ConstraintException, AccessException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Loader loader = new Loader(version);
            Organism species = loader.getSpecies(UNIQUE);
            Target target = loader.getTarget(UNIQUE, species, "ATGCATGCATGCATG");
            ResearchObjective construct =
                loader.saveConstruct("KKK", target, CONSTRUCT_NAME, "mutations", "FL no Met", "", "comments",
                    "ATGCATGCATGCATG");

            // test results
            assertEquals(CONSTRUCT_NAME, construct.getName());
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("mutations\nFL no Met", bean.getDescription());
            assertEquals("comments", bean.getComments());
            assertEquals("KKK", bean.getExpressedProt());
            assertEquals(new Integer(5), bean.getTargetProtEnd());
            assertEquals(new Integer(2), bean.getTargetProtStart());

        } finally {
            version.abort();
        }
    }

    private static final String LABELS =
        "ID,Experimental Amino Acid Sequence,Experimental Nucleotide Sequence,Title,Target,Organism,Mutations,Insertion,Truncations,Vector,Comments,Experimental MW,Experimental MW sans HA,Item Type\n";

    public void testLoadNone() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs = Loader.load(version, new StringReader(LABELS));
            assertEquals(0, constructs.size());
        } finally {
            version.abort();
        }
    }

    private static final String NO_SEQS =
        "1,?,NONE,title,ADORA3,,WT,,FL,p2228,A1,18.01524,-1797.28476,Item\n";

    public void testLoadNoSeqs() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs = Loader.load(version, new StringReader(LABELS + NO_SEQS));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            assertEquals("title", construct.getName());
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\nFL", bean.getDescription());

        } finally {
            version.abort();
        }
    }

    public void testInsertion() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS
                    + "1,?,NONE,title,ADORA3,H.Sapiens,WT,T4L,FL no Met"
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\nFL no Met\nT4L", bean.getDescription());

        } finally {
            version.abort();
        }
    }

    public void testLoadTruncated() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS + "1,?,NONE,title,ADORA3,H.Sapiens,WT,, 215-254"
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\n 215-254", bean.getDescription());
            assertEquals(new Integer(215), bean.getTargetProtStart());
            assertEquals(new Integer(254), bean.getTargetProtEnd());

        } finally {
            version.abort();
        }
    }

    public void testOtherTruncation() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS
                    + "1,?,NONE,title,ADORA3,H.Sapiens,WT,,delta 191K"
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\ndelta 191K", bean.getDescription());
            assertEquals(new Integer(1), bean.getTargetProtStart());
            assertEquals(null, bean.getTargetProtEnd());

        } finally {
            version.abort();
        }
    }

    public void testTruncationAndDelta() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS
                    + "1,?,NONE,title,ADORA3,H.Sapiens,WT,,1-348 (delta 245-259)"
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\n1-348 (delta 245-259)", bean.getDescription());
            assertEquals(new Integer(1), bean.getTargetProtStart());
            assertEquals(new Integer(348), bean.getTargetProtEnd());

        } finally {
            version.abort();
        }
    }

    public void testCDelta() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS
                    + "1,?,NONE,title,ADORA3,H.Sapiens,WT,,C-term delta341-380"
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\nC-term delta341-380", bean.getDescription());
            assertEquals(new Integer(1), bean.getTargetProtStart());
            assertEquals(new Integer(340), bean.getTargetProtEnd());

        } finally {
            version.abort();
        }
    }

    public void testNDelta() throws ConstraintException, AccessException, IOException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            List<ResearchObjective> constructs =
                Loader.load(version, new StringReader(LABELS
                    + "1,?,NONE,title,ADORA3,H.Sapiens,WT,,\"Ndelta1-10, Cdelta376-380\""
                    + ",p2228,A1,18.01524,-1797.28476,Item\n"

                ));
            assertEquals(1, constructs.size());
            ResearchObjective construct = constructs.iterator().next();
            ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            assertEquals("WT\nNdelta1-10, Cdelta376-380", bean.getDescription());
            assertEquals(new Integer(375), bean.getTargetProtEnd());
            assertEquals(new Integer(11), bean.getTargetProtStart());

        } finally {
            version.abort();
        }
    }

}
