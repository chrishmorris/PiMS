package org.pimslims.lab;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

public class RecipeUtilityTest extends TestCase {

	private static final String UNIQUE = "ru" + System.currentTimeMillis();
	private final AbstractModel model;

	public RecipeUtilityTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}

	public void test() throws ConstraintException, AccessException {
		WritableVersion version = this.model.getTestVersion();
		try {
			LabNotebook book = new LabNotebook(version, UNIQUE);
			RefSample recipe = new RefSample(version, UNIQUE);
			Substance substance = new Molecule(version, "DNA", UNIQUE);
			SampleComponent sc = new SampleComponent(version, substance, recipe);
			version.setDefaultOwner(book);
			RefSample duplicate = RecipeUtility.duplicate(recipe, version);
			assertTrue(duplicate.getName().startsWith(recipe.getName()));
			assertEquals(book, duplicate.getAccess());
			assertEquals(1, duplicate.getSampleComponents().size());
			SampleComponent dsc = duplicate.getSampleComponents().iterator()
					.next();
			assertEquals(book, dsc.getAccess());
			assertEquals(book, dsc.getRefComponent().getAccess());
		} finally {
			version.abort();
		}
	}

}
