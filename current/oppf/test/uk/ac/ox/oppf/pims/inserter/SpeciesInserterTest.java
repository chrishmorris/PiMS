package uk.ac.ox.oppf.pims.inserter;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.Organism;

import uk.ac.ox.oppf.optic.model.Species;

import junit.framework.TestCase;

public class SpeciesInserterTest extends TestCase {
	
	private static final String UNIQUE = "si"+System.currentTimeMillis();
	private final AbstractModel model;

	
	
	public SpeciesInserterTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}



	public void test() throws ConstraintException, AccessException {
		WritableVersion version = this.model.getTestVersion();
		try {
		SpeciesInserter inserter = new SpeciesInserter(version );
		Species s = new Species();
		s.setLineage("");
		s.setSpeciesId(1);
		s.setScientific(UNIQUE);
		Organism organism = (Organism) inserter.insert(s);
		assertEquals("1", organism.getNcbiTaxonomyId());
		} finally {version.abort();}
	}

}
