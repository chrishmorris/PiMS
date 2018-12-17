/**
 * 
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import uk.ac.ox.oppf.www.wsplate.WSPlateError;

/**
 * WSPlateError subclass that provides extra constructors,
 * null-safe setters for details and message and some
 * pre-defined errors
 * 
 * @author Jon Diprose
 */
public class WSPlateErrorImpl extends WSPlateError {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8724773940974634996L;
	
	public static final int E_CODE_BARCODE_NOT_FOUND = 1;
    public static final int E_CODE_PLATEID_NOT_FOUND = 2;
    public static final int E_CODE_ROBOTID_NOT_FOUND = 3;
    public static final int E_CODE_PLATETYPEID_NOT_FOUND = 4;

    public static final int E_CODE_IMAGINGTASK_DATETOIMAGE_NOT_FOUND = 10;
    public static final int E_CODE_IMAGINGTASK_IMAGINGID_NOT_FOUND = 11;
    
    public static final int E_CODE_DATASTORE_UNAVAILABLE = 1000;
    public static final int E_CODE_DATA_ACCESS_ERROR = 1001;
    
    public static final String E_MSG_BARCODE_NOT_FOUND = "Barcode not found in data store";
    public static final String E_MSG_PLATEID_NOT_FOUND = "PlateID not found in data store";
    public static final String E_MSG_ROBOTID_NOT_FOUND = "RobotID not found in data store";
    public static final String E_MSG_PLATETYPEID_NOT_FOUND = "PlateTypeID not found in data store";
    
    public static final String E_MSG_IMAGINGTASK_DATETOIMAGE_NOT_FOUND = "ImagingTask not found for the specified PlateID and DateToImage";
    public static final String E_MSG_IMAGINGTASK_IMAGINGID_NOT_FOUND = "ImagingTask not found for the specified PlateID and ImagingID";
    
    public static final String E_MSG_DATASTORE_UNAVAILABLE = "Datastore unavailable";
    public static final String E_MSG_DATA_ACCESS_ERROR = "Error during data accesss";
    
	/**
	 * Empty constructor
	 */
	public WSPlateErrorImpl() {
		this(0, "", "");
	}

	/**
	 * Constructor specifying errorCode and message
	 */
	public WSPlateErrorImpl(int errorCode, String message) {
		this(errorCode, message, "");
	}

	/**
	 * Constructor specifying errorCode, message and details
	 */
	public WSPlateErrorImpl(int errorCode, String message, String details) {
		super();
		this.setDetails(details);
		this.setErrorCode(errorCode);
		this.setMessage(message);
	}
	
	/**
	 * setDetails(String) that replaces null with ""
	 */
	public void setDetails(String details) {
		if (details == null) super.setDetails("");
		else super.setDetails(details);
	}

	/**
	 * setMessage(String) that replaces null with ""
	 */
	public void setMessage(String message) {
		if (message == null) super.setMessage("");
		else super.setMessage(message);
	}

	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_BARCODE_NOT_FOUND
	 * and message E_MSG_BARCODE_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEBarcodeNotFound() {
		return new WSPlateErrorImpl(E_CODE_BARCODE_NOT_FOUND, E_MSG_BARCODE_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_PLATEID_NOT_FOUND
	 * and message E_MSG_PLATEID_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEPlateIDNotFound() {
		return new WSPlateErrorImpl(E_CODE_PLATEID_NOT_FOUND, E_MSG_PLATEID_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_ROBOTID_NOT_FOUND
	 * and message E_MSG_ROBOTID_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getERobotIDNotFound() {
		return new WSPlateErrorImpl(E_CODE_ROBOTID_NOT_FOUND, E_MSG_ROBOTID_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_PLATETYPEID_NOT_FOUND
	 * and message E_MSG_PLATETYPEID_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEPlateTypeIDNotFound() {
		return new WSPlateErrorImpl(E_CODE_PLATETYPEID_NOT_FOUND, E_MSG_PLATETYPEID_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_IMAGINGTASK_DATETOIMAGE_NOT_FOUND
	 * and message E_MSG_IMAGINGTASK_DATETOIMAGE_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEImagingTaskDateToImageNotFound() {
		return new WSPlateErrorImpl(E_CODE_IMAGINGTASK_DATETOIMAGE_NOT_FOUND, E_MSG_IMAGINGTASK_DATETOIMAGE_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_IMAGINGTASK_IMAGINGID_NOT_FOUND
	 * and message E_MSG_IMAGINGTASK_IMAGINGID_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEImagingTaskImagingIDNotFound() {
		return new WSPlateErrorImpl(E_CODE_IMAGINGTASK_IMAGINGID_NOT_FOUND, E_MSG_IMAGINGTASK_IMAGINGID_NOT_FOUND);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_DATASTORE_UNAVAILABLE_NOT_FOUND
	 * and message E_MSG_DATASTORE_UNAVAILABLE_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEDataStoreUnavailable() {
		return new WSPlateErrorImpl(E_CODE_DATASTORE_UNAVAILABLE, E_MSG_DATASTORE_UNAVAILABLE);
	}
	
	/**
	 * Get a WSPlateErrorImpl with errorCode = E_CODE_DATA_ACCESS_ERROR_NOT_FOUND
	 * and message E_MSG_DATA_ACCESS_ERROR_NOT_FOUND
	 * 
	 * @return The WSPlateErrorImpl
	 */
	public static WSPlateErrorImpl getEDataAccessError() {
		return new WSPlateErrorImpl(E_CODE_DATA_ACCESS_ERROR, E_MSG_DATA_ACCESS_ERROR);
	}
	
}
