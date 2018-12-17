/**
 * implementation RhombixVersion.java
 * 
 * @author cm65
 * @date 17 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.ResultSet;

/**
 * Version
 * 
 * Represents a particular version of the Maufacturer's  database
 * 
 */
public abstract class ManufacturerVersion {

    public static final ManufacturerVersion OULU = new ManufacturerVersion() {

        @Override
        public String getImageSelectStatement() {
            return OULU_SELECT;
        }

//        @Override
//        public String getPathPrefix() {
//        	//TODO Get this from the database. ImageStore.BasePath + (back?)slash
//            return OULU_PATH_PREFIX;
//        }

//        public String getMappedPrefix(String formulatrixImageStorePath) {
//        	//TODO hashtable(?) of possible image store values to drive letters/paths
//        	return "Z:";
//        }
    };


    /**
     * Select statement for images, in Oulu
     */
    private static final String OULU_SELECT =
    	/* Only import the whole-well, extended-focus images, no regions or z-slices for now */
    		"SELECT " +
    			/* //biok106009/RockMakerStorage */	" REPLACE(ImageStore.BasePath,'\\','/') AS FormulatrixPathPrefix, " +
    			"(" +
	    			/* /WellImages/220/plateID_220 */	"  '/WellImages/'+ CAST((Plate.ID % 1000) AS varchar) +'/plateID_'+ CAST(Plate.ID AS varchar) " +
	    			/* /batchID_77/wellNum_1 */			"+ '/batchID_'+ CAST(ImageBatch.ID AS varchar) +'/wellNum_'+ CAST(Well.WellNumber AS varchar) " +
	    			/* /profileID_1 */					"+ '/profileID_'+ CAST(CaptureProfileVersion.CaptureProfileID AS varchar) " + 
	    			/* /d1_r510_ef.jpg */ 				"+ '/d'+ CAST(WellDrop.DropNumber AS varchar) +'_r'+ CAST(Region.ID AS varchar) +'_ef.jpg'" +
    			") AS ImagePath, Image.ID AS ImageID, CaptureProfileVersion.CaptureProfileID AS CaptureProfileID, " +
    			" Region.YHeight AS Height, Region.XWidth AS Width, Plate.Barcode AS PlateBarcode,  " +
    			" Image.PixelSize AS MicronsPerPixel, CONVERT(varchar(25),ImagingTask.DateImaged,121) AS DateImaged, " +
    			" CONVERT(varchar(25),ImagingTask.DateToImage,121) AS DateToImage, " +
    			" Well.RowLetter AS RowLetter, Well.ColumnNumber AS ColumnNumber, WellDrop.DropNumber AS DropNumber " +
    		" FROM Image,  ImageBatch, ImageStore, ImagingTask, " +
    			" ExperimentPlate, Plate, Well, WellDrop, Region, CaptureResult, CaptureProfileVersion " +
    		" WHERE Region.WellDropID=WellDrop.ID AND WellDrop.WellID=Well.ID AND Well.PlateID=Plate.ID  " +
    			" AND ImageBatch.ImagingTaskID=ImagingTask.ID AND ImagingTask.ExperimentPlateID=ExperimentPlate.ID " + 
    			" AND ExperimentPlate.PlateID=Plate.ID  " +
    			" AND Image.ImageStoreID=ImageStore.ID AND Image.CaptureResultID=CaptureResult.ID  " +
    			" AND CaptureResult.RegionID=Region.ID AND CaptureResult.ImageBatchID=ImageBatch.ID AND CaptureResult.CaptureProfileVersionID=CaptureProfileVersion.ID" +
    			" AND Image.ImageTypeID=2 " +
				" AND Region.RegionTypeID=1" + // Drop - only import full, not part, images
    			" AND Plate.ID=?";
    		
//    private static final String OULU_PATH_PREFIX = "//SQLPLAYGROUND/RockMakerStorage";

    
    public abstract String getImageSelectStatement();

    /**
     * Version.getPathPrefix
     * 
     * @return Obsolete
     */
//    public abstract String getPathPrefix();
}
