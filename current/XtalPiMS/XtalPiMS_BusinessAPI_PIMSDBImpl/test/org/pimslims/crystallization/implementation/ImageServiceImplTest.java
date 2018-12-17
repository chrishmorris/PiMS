/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.business.crystallization.model.Image;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;

/**
 * This does not test the link between images and the wells
 * 
 * @author ian
 */
@Deprecated
public class ImageServiceImplTest extends TestCase {

	// unique string used to avoid name clashes
	private static final String UNIQUE = "test" + System.currentTimeMillis();

	private static final Timestamp NOW = new Timestamp(
			System.currentTimeMillis());

	protected final DataStorageImpl dataStorage;

	private final int colourDepth = 1024;

	private final Calendar imageDate = Calendar.getInstance();

	private final String imagePath = "../path/images/";

	private final String imageFileName = "image1" + UNIQUE;

	private final int sizeX = 256;

	private final int sizeY = 128;

	private final double xLengthPerPixel = 0.1;

	private final double yLengthPerPixel = 0.2;

	Image xtImage;

	public ImageServiceImplTest(final String testName) {
		super(testName);
		dataStorage = DataStorageFactory.getDataStorageFactory()
				.getDataStorage();
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite(ImageServiceImplTest.class);

		return suite;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// create a xtalImage
		xtImage = new Image();
		xtImage.setColourDepth(colourDepth);
		xtImage.setImageDate(imageDate);
		xtImage.setImagePath(imagePath + imageFileName);
		xtImage.setSizeX(sizeX);
		xtImage.setSizeY(sizeY);
		xtImage.setXLengthPerPixel(xLengthPerPixel);
		xtImage.setYLengthPerPixel(yLengthPerPixel);

	}

}
