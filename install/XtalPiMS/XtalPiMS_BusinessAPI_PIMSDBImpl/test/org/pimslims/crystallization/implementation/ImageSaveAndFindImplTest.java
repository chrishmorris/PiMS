package org.pimslims.crystallization.implementation;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.crystallization.service.ImageSaveAndFindTest;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;

public class ImageSaveAndFindImplTest extends ImageSaveAndFindTest {

	public ImageSaveAndFindImplTest(String methodName) {
		super(methodName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	/**
	 * ImageSaveAndFindImplTest.createImager
	 * 
	 * @see org.pimslims.business.crystallization.service.ImageSaveAndFindTest#createImager()
	 */
	@Override
	protected Location createImager() throws BusinessException {
		try {
			String name = "imr" + UNIQUE;
			WritableVersion version = ((DataStorageImpl) this.dataStorage)
					.getWritableVersion();
			ImageType type = new ImageType(version, name);
			type.setUrl(URL);
			new Instrument(version, name).setDefaultImageType(type);
			return this.dataStorage.getLocationService().findByName(name);
		} catch (ConstraintException e) {
			throw new BusinessException(e);
		}
	}

}
