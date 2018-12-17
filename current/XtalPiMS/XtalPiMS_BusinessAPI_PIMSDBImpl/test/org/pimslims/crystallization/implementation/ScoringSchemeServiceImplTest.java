/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;

/**
 * 
 * @author ian
 */
public class ScoringSchemeServiceImplTest extends TestCase {

    // unique string used to avoid name clashes
    protected static final String UNIQUE = "_test" + System.currentTimeMillis();

    protected final DataStorageImpl dataStorage;

    ScoreValue scoreValue1;

    ScoreValue scoreValue2;

    ScoringScheme scoringScheme;

    public ScoringSchemeServiceImplTest(final String testName) {
        super(testName);
        dataStorage = DataStorageFactory.getDataStorageFactory().getDataStorage();
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(ScoringSchemeServiceImplTest.class);

        return suite;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //create 2 ScoreValues
        scoreValue1 = new ScoreValue();
        scoreValue1.setColour(Color.blue);
        scoreValue1.setValue(1);
        scoreValue1.setDescription("score1 Description");
        scoreValue2 = new ScoreValue();
        scoreValue2.setColour(Color.RED);
        scoreValue2.setValue(2);
        scoreValue2.setDescription("score2 Description");

        //create a ScoringScheme
        scoringScheme = new ScoringScheme();
        scoringScheme.setName("name" + System.currentTimeMillis());
        scoringScheme.setDescription("scoringScheme description");
        scoringScheme.setVersion("version");
        //add score to ScoringScheme
        final List<ScoreValue> scores = new LinkedList<ScoreValue>();
        scores.add(scoreValue1);
        scores.add(scoreValue2);
        scoringScheme.setScores(scores);
    }

    /**
     * Test of create ScoringScheme
     */
    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version = this.dataStorage.getWritableVersion();

            //create pims ScoringScheme
            final ScoringSchemeService service = this.dataStorage.getScoringSchemeService();
            scoringScheme.setName("name" + System.currentTimeMillis());
            service.create(scoringScheme);
            final Long pimsID = scoringScheme.getId();

            //verify pims ScoringScheme
            assertNotNull(pimsID);
            final org.pimslims.model.crystallization.ScoringScheme pimsObject = version.get(pimsID);
            assertNotNull(pimsObject);
            assertEquals(scoringScheme.getDescription(), pimsObject.getDetails());
            assertEquals(scoringScheme.getName(), pimsObject.getName());
            assertEquals(scoringScheme.getVersion(), pimsObject.getVersion());

            //verify scoreValue
            assertEquals(scoringScheme.getScores().size(), pimsObject.getScores().size());
            final Long scoreID = scoringScheme.getScores().iterator().next().getId();
            assertNotNull(scoreID);
            final org.pimslims.model.crystallization.Score pScore = version.get(scoreID);
            assertNotNull(pScore);
            assertEquals(scoreValue1.getValue(), pScore.getValue().intValue());
            assertEquals(scoreValue1.getDescription(), pScore.getName());
            assertEquals("" + Color.blue.getRGB(), pScore.getColor());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testFindByName() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version = this.dataStorage.getWritableVersion();

            //create pims ScoringScheme
            final ScoringSchemeService service = this.dataStorage.getScoringSchemeService();
            scoringScheme.setName("name" + System.currentTimeMillis());
            service.create(scoringScheme);
            //find ScoringScheme by name
            final ScoringScheme scoringScheme2 = service.findByName(scoringScheme.getName());

            //verify ScoringScheme found
            assertNotNull(scoringScheme2);
            assertNotNull(scoringScheme2.getId());
            assertEquals(scoringScheme.getDescription(), scoringScheme2.getDescription());
            assertEquals(scoringScheme.getName(), scoringScheme2.getName());
            assertEquals(scoringScheme.getVersion(), scoringScheme2.getVersion());

            //verify scoreValue
            assertEquals(scoringScheme.getScores().size(), scoringScheme2.getScores().size());
            ScoreValue scoreValue1_2 = null;
            for (final ScoreValue scoreValue : scoringScheme2.getScores()) {
                if (scoreValue.getValue() == 1) {
                    scoreValue1_2 = scoreValue;
                    break;
                }
            }

            assertNotNull(scoreValue1_2);
            assertNotNull(scoreValue1_2.getId());
            assertEquals(scoreValue1.getValue(), scoreValue1_2.getValue());
            assertEquals(scoreValue1.getDescription(), scoreValue1_2.getDescription());
            assertEquals(scoreValue1.getColour(), scoreValue1_2.getColour());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
