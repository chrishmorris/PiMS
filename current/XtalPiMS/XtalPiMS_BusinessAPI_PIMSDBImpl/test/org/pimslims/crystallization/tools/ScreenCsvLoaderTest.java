package org.pimslims.crystallization.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;

public class ScreenCsvLoaderTest extends AbstractXtalTest {

	private static final String PIMS_CSV = "\"Well\",\"Name\",\"Concentration\",\"Concentration Unit\",Type\r\n"
			+ "\"B2\",\"" + UNIQUE + "\",\"0.050\",\"M\",salt\r\n";

	/**
	 * CSV A line from an actual PiSampler CSV file Note there is a problem with
	 * this format: the memGold screen, for example, has five conditions in some
	 * wells. The PiMS format uses one row for each condition - see testTwoRows
	 */
	private static final String PI_SAMPLER_CSV = "\"Condition Reference\",\"First Component Name\",\"First Component Concentration\",\"First Component Concentration Unit\",\"Second Component Name\",\"Second Component Concentration\",\"Second Component Concentration Unit\",\"Third Component Name\",\"Third Component Concentration\",\"Third Component Concentration Unit\"\r\n"
			+ "\"B2\",\"acetate pH 4.8\",\"0.050\",\"M\",\"PEG 200\",\"22.500\",\"% v/v\",\"PEG MME 2000\",\"0.000\",\"% w/v\"\r\n"
			+ "";

	private static final String RHOMBIX_CSV = "Rhombix Import Template,Version 1.0,5/17/2011  12:01:15 ,\"Exports screens of choice from database...\",User Comment:,\"Data Positions:     Screen name value: B7;\r\n"
			+ "            \",User comment: B9; Custom or shared value: B8; Grid - Start Conc. Values: A12 - E12,,,,,,,,,\r\n"
			+ "New Screen Description,Screen Name:,Helsinki  random II,Custom or Shared,custom,text indicator,User Comment for Screen:,http://www.biocenter.helsinki.fi/bi/xray/automation/,text indicator,Chemical Distribution,Grid,Chemical,Distribution Scope,Plate or (Grid) Coordinate,Start Conc.,Step Value\r\n"
			+ "A01:H12,\"Ammonium Sulphate (3.00M), Salt\",Well,A01,2.000,,,,,,,,,,,\r\n"
			+ "A01:H12,\"PEG 3350 (50.00%), Precipitant"
			+ "\",Well,A01,0.100,,,,,,,,,,,\r\n"
			+ "A01:H12,\"Ammonium Sulphate (3.00M), Salt\",Well,B01,2.000,,,,,,,,,,,\r\n"
			+ "A01:H12,\"Sodium Potassium Phosphate (Buffered) (1.40M pH 5.60), Precipitant\",Well,B05,1.400,,,,,,,,,,,\r\n"
			+ "1,,,,,,,,,,,,,,,"; // strange last line

	public ScreenCsvLoaderTest(final String testName) {
		super(testName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	public void testConcentration() throws BusinessException, IOException {
		this.dataStorage.openResources("administrator");
		try {
			final InputStream input = new ByteArrayInputStream(
					PIMS_CSV.getBytes("UTF-8"));
			ScreenCsvLoader loader = ScreenCsvLoader.getLoader(
					this.dataStorage, input, UNIQUE);
			Screen screen = loader.load();
			assertNotNull(screen);
			WellPosition well = new WellPosition("B02");
			Condition condition = screen.getCondition(well);
			assertNotNull(condition);
			List<ComponentQuantity> components = condition.getComponents();
			assertEquals(1, components.size());
			ComponentQuantity cq = components.iterator().next();
			assertEquals("M", cq.getUnits());
			assertEquals(0.050d, cq.getQuantity());
			assertEquals("salt", cq.getComponentType());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testTwoRows() throws BusinessException, IOException {
		this.dataStorage.openResources("administrator");
		try {
			String csv = PIMS_CSV + "\"B2\",\"" + UNIQUE
					+ "two\",\"0.1\",\"M\"\r\n";
			;
			final InputStream input = new ByteArrayInputStream(
					csv.getBytes("UTF-8"));

			ScreenCsvLoader loader = ScreenCsvLoader.getLoader(
					this.dataStorage, input, UNIQUE);
			Screen screen = loader.load();
			assertNotNull(screen);
			WellPosition well = new WellPosition("B02");
			Condition condition = screen.getCondition(well);
			assertNotNull(condition);
			List<ComponentQuantity> components = condition.getComponents();
			assertEquals(2, components.size());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testAlias() throws BusinessException, IOException,
			ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			List<String> synonyms = Collections.singletonList(UNIQUE + "alias");
			WritableVersion version = ((DataStorageImpl) this.dataStorage)
					.getWritableVersion();
			new Molecule(version, "other", UNIQUE).setSynonyms(synonyms);
			final InputStream input = new ByteArrayInputStream(
					PIMS_CSV.getBytes("UTF-8"));
			ScreenCsvLoader loader = ScreenCsvLoader.getLoader(
					this.dataStorage, input, UNIQUE);
			Screen screen = loader.load();

			WellPosition well = new WellPosition("B02");
			Condition condition = screen.getCondition(well);
			assertNotNull(condition);
			List<ComponentQuantity> components = condition.getComponents();
			ComponentQuantity cq = components.iterator().next();
			assertEquals(UNIQUE, cq.getComponent().getChemicalName());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testRhombixRegex() {
		assertTrue(Pattern.matches(".*?", "1.00M pH 3.50"));
		Matcher m = RhombixCsvLoader.COMPONENT_DESCRIPTION
				.matcher("Ammonium Sulphate (3.00M), Salt");
		assertTrue(m.matches());
		m = RhombixCsvLoader.COMPONENT_DESCRIPTION
				.matcher("Citric Acid (1.00M pH 3.50), Buffer");
		assertTrue(m.matches());
	}

	public void testRhombix() throws BusinessException, IOException {
		this.dataStorage.openResources("administrator");
		try {
			final InputStream input = new ByteArrayInputStream(
					RHOMBIX_CSV.getBytes("UTF-8"));
			ScreenCsvLoader loader = ScreenCsvLoader.getLoader(
					this.dataStorage, input, UNIQUE);
			Screen screen = loader.load();
			assertNotNull(screen);
			WellPosition well = new WellPosition("A01");
			Condition condition = screen.getCondition(well);
			assertNotNull(condition);
			List<ComponentQuantity> components = condition.getComponents();
			assertEquals(2, components.size());
			ComponentQuantity cq = components.iterator().next();

			assertEquals("M", cq.getUnits());
			assertEquals(3.00d, cq.getQuantity());
			assertEquals("Salt", cq.getComponentType());

			screen.setName(UNIQUE);
			this.dataStorage.getScreenService().create(screen);
			this.dataStorage.flush();
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testPiSampler() throws BusinessException, IOException {
		this.dataStorage.openResources("administrator");
		try {
			final InputStream input = new ByteArrayInputStream(
					PI_SAMPLER_CSV.getBytes("UTF-8"));
			ScreenCsvLoader loader = ScreenCsvLoader.getLoader(
					this.dataStorage, input, UNIQUE);
			Screen screen = loader.load();
			assertNotNull(screen);
			WellPosition well = new WellPosition("B02");
			Condition condition = screen.getCondition(well);
			assertNotNull(condition);
			List<ComponentQuantity> components = condition.getComponents();
			assertEquals(3, components.size());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

}
