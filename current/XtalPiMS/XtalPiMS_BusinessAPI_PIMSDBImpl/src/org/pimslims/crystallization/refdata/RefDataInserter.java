/**
 * 
 */
package org.pimslims.crystallization.refdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom.JDOMException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.GenericRefLoader;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;

/**
 * @author Jon Diprose TODO no, make all this ref data part of standard load in
 *         tools/
 */
public class RefDataInserter {

	private static AbstractModel model;

	String dirName = ".//data//";

	public RefDataInserter(String fileDir) {
		dirName = fileDir;
	}

	/**
	 * <p>
	 * Insert all necessary reference data items on which the xtalPiMS
	 * implementation depends.
	 * </p>
	 * 
	 * @param wv
	 * @throws ConstraintException
	 * @throws JDOMException
	 * @throws AccessException
	 * @throws IOException
	 */
	public void insert(final WritableVersion wv) throws ConstraintException,
			AccessException, JDOMException, IOException {
		String filename = null;

		// load new ref-data file next

		System.out.println("\nStart loading new ref-data into DB...");

		System.out.println("-Loading new Image Types");
		GenericRefLoader.loadFile(wv, ImageType.class.getName(), dirName
				+ "OPPFImageTypes.csv");

		System.out.println("-Loading new Instruments");
		GenericRefLoader.loadFile(wv, Instrument.class.getName(), dirName
				+ "OPPFInstruments.csv");

		/*
		 * These are now part of standard PiMS reference data
		 * System.out.println("-Loading new ExperimentTypes"); filename =
		 * dirName + "ExperimentTypes.csv"; final java.io.Reader reader = new
		 * java.io.FileReader(filename); ExperimentTypesUtility.loadFile(wv,
		 * reader);
		 * 
		 * System.out.println("-Loading new sample categories"); filename =
		 * dirName + "SampleCats.csv"; SampleCatsUtility.loadFile(wv, filename);
		 * 
		 * System.out.println("-Loading new holder categories"); filename =
		 * dirName + "HolderCats.csv"; HolderCatsUtility.loadFile(wv, filename);
		 * 
		 * filename = dirName + "ScoreScheme.csv"; GenericRefLoader.loadFile(wv,
		 * ScoringScheme.class.getName(), filename); filename = dirName +
		 * "Scores.csv"; GenericRefLoader.loadFile(wv, Score.class.getName(),
		 * filename); System.out.println("-Loading new Protocols");
		 * 
		 * final Collection<String> protocolFileNames = new LinkedList<String>();
		 * protocolFileNames.add("/protocols/PIMS_CrystalTrial.xml");
		 * 
		 * for (final String protocolFileName : protocolFileNames) { filename =
		 * dirName + protocolFileName; //System.out.println(protocolFileName);
		 * try { final java.io.Reader reader2 = new
		 * java.io.FileReader(filename); new
		 * ProtocolUtility(wv).loadFile(reader2); } catch (final IOException e) {
		 * System.out.println(filename + " is ignored as can not be
		 * found/loaded!"); } }
		 */
		System.out
				.println("\nFinish ref-data loading for xtalPiMS successfully!!!");

	}

	/**
	 * <p>
	 * Insert all necessary reference data items on which the xtalPiMS
	 * implementation depends.
	 * </p>
	 * 
	 * @param args
	 * @throws JDOMException
	 * @throws AccessException
	 * @throws IOException
	 */
	public static void main(final String[] args) throws AccessException,
			JDOMException, IOException {
		if (args.length < 2) {
			System.out.println("provide arguments in order: ");
			System.out
					.println(" 1) propertieFile: the file defined the DB connection info same as the one used in upgrader!");
			System.out
					.println(" 2) RefDirectory: a directory which contrains all ref-data!");
			return;
		}
		// AbstractLoader.silent = false;
		RefDataInserter.initModel(args[0]);
		final String fileDir = args[1];

		final WritableVersion wv = model
				.getWritableVersion(AbstractModel.SUPERUSER);
		final RefDataInserter rdi = new RefDataInserter(fileDir);
		try {
			rdi.insert(wv);
			wv.commit();
		} catch (final ConstraintException e) {
			e.printStackTrace();
			wv.abort();
		} catch (final AbortedException e) {
			e.printStackTrace();
		} finally {
			wv.close();
		}
	}

	/**
	 * @param string
	 * @throws FileNotFoundException
	 */
	private static void initModel(final String propertyFileName)
			throws FileNotFoundException {

		// start from propertyFile if provided
		System.out.println("loading DB connection info from: "
				+ propertyFileName);
		final File properties = new java.io.File(propertyFileName);
		if (!properties.exists()) {
			System.out.println("file does NOT exist:" + propertyFileName);
		} else if (!properties.isFile()) {
			System.out.println("please give a file NOT a directory: "
					+ propertyFileName);
		}
		RefDataInserter.model = org.pimslims.dao.ModelImpl.getModel(properties);

	}

}
