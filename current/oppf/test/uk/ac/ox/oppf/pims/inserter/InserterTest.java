package uk.ac.ox.oppf.pims.inserter;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.hibernate.Session;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanWriter;

import uk.ac.ox.oppf.optic.model.Construct;
import uk.ac.ox.oppf.optic.model.FwdTag;
import uk.ac.ox.oppf.optic.model.GenbankInfo;
import uk.ac.ox.oppf.optic.model.OpticGroup;
import uk.ac.ox.oppf.optic.model.RevTag;
import uk.ac.ox.oppf.optic.model.Species;
import junit.framework.TestCase;

/**
 * @author cm65
 * Tests the interface to PiMS of the Optic loader.
 * Does not test the access to the optic database.
 */
public class InserterTest extends TestCase {
	
// wraps the dependencies on pims-web
    public static final class ConstructWriter implements ConstructWriterI {

        @Override
        public void createMolComp(final WritableVersion version, final String category, final String seq,
            final String molType, final String id) throws AccessException, ConstraintException {
            ConstructBeanWriter.createMolComp(version, category, seq, molType, id);
        }

        @Override
        public void createPrimerDesignExperiment(final WritableVersion version, final Object cb,
            final ResearchObjective ro) throws AccessException, ConstraintException {
            ConstructBeanWriter.createPrimerDesignExperiment(version, (ConstructBean) cb, ro);

        }
    }

	
	private static final String UNIQUE = "inserter"+System.currentTimeMillis();
	private final AbstractModel model;

	
	
	public InserterTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}
	
	public void testNoTargets() throws AccessException, ClassNotFoundException, IllegalAlphabetException, IllegalSymbolException, SQLException {

		WritableVersion version = this.model.getTestVersion();
		try {
			Inserter inserter = new Inserter();
			Collection<String> targets = Collections.EMPTY_SET;
			inserter.insertTargets(version, targets);

		} finally {version.abort();}
	}


	public void TODOtestNoSuchTarget() throws AccessException, ClassNotFoundException, IllegalAlphabetException, IllegalSymbolException, SQLException {

		WritableVersion version = this.model.getTestVersion();
		try {
			Inserter inserter = new Inserter();
			Collection<String> targets = Collections.singleton("nonesuch");
			inserter.insertTargets(version, targets);

		} finally {version.abort();}
	}
	

	public void testOneTarget() throws  ConstraintException, AccessException, IllegalSymbolException, IllegalAlphabetException, ClassNotFoundException, SQLException {

		WritableVersion version = this.model.getTestVersion();
		try {
			Inserter inserter = new Inserter();
			GenbankInfo bean = getGenbankInfo();

			inserter.insertTarget(version, bean, new ConstructWriter());
			
			//TODO check results

		} finally {version.abort();}
	}

	/**
	 * @return
	 */
	private GenbankInfo getGenbankInfo() {
		GenbankInfo bean = new GenbankInfo();
		bean.setId(1);
		bean.setOpticGroups(Collections.EMPTY_SET);
		bean.setConstructs(Collections.EMPTY_SET);
		bean.setSpecies(getSpeciesBean());
		bean.setProteinSequence("QWERTY");
		bean.setDnaSequence("ATGCCCGGG");
		return bean;
	}
	

	
	public void testOpticGroup() throws  ConstraintException, AccessException, IllegalSymbolException, IllegalAlphabetException, ClassNotFoundException, SQLException {

		WritableVersion version = this.model.getTestVersion();
		try {
			
			Inserter inserter = new Inserter();
			GenbankInfo bean = getGenbankInfo();
			OpticGroup group = new OpticGroup();
			group.setName(UNIQUE);
			new TargetGroup(version, group.getName());
			bean.setOpticGroups(Collections.singleton(group));

			Target target = inserter.insertTarget(version, bean, new ConstructWriter());
			assertEquals(1, target.getTargetGroups().size());
			assertEquals(group.getName(), target.getTargetGroups().iterator().next().getName());

		} finally {version.abort();}
	}
	
	

	public void testConstruct() throws  ConstraintException, AccessException, IllegalSymbolException, IllegalAlphabetException, ClassNotFoundException, SQLException {

		WritableVersion version = this.model.getTestVersion();
		try {
			Person person = new Person(version, UNIQUE);
			new User(version, UNIQUE).setPerson(person);
			
			Inserter inserter = new Inserter();
			GenbankInfo bean = getGenbankInfo();
			Construct construct = makeOpticConstruct(bean);
			construct.setPickedBy(person.getFamilyName());
			bean.setConstructs(Collections.singleton(construct ));
			inserter.insertTarget(version, bean, new ConstructWriter());

		} finally {version.abort();}
	}

	/**
	 * @param bean
	 * @return
	 */
	private Construct makeOpticConstruct(GenbankInfo bean) {
		Construct construct = new Construct();
		construct.setAuth(new Byte("1"));
		construct.setAuthBy(null);
		construct.setStart(1);
		construct.setStop(5);
		construct.setFwdAnnealLen(3);
		construct.setRevAnnealLen(3);
		construct.setGenbankInfo(bean);
		construct.setConstructId(-1);
		construct.setPickedAt(new Date()); //TODO change to Calendar
		
		FwdTag fwdTag = new FwdTag();
		fwdTag.setConstructs(Collections.singleton(construct));
		fwdTag.setFwdTagSeq("AAA");
		construct.setFwdTag(fwdTag);
		RevTag revTag = new RevTag();
		revTag.setConstructs(Collections.singleton(construct));
		revTag.setRevTagSeq("TTT");
		construct.setRevTag(revTag);
		return construct;
	}


	/**
	 * @return
	 */
	private Species getSpeciesBean() {
		Species species = new Species();
		species.setLineage("");
		species.setSpeciesId(1);
		species.setScientific(UNIQUE);
		return species;
	}


}
