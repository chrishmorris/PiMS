/**
 * Rhombix_Impl org.pimslims.rhombix.implementation RhombixVersion.java
 * 
 * @author cm65
 * @date 17 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

/**
 * RhombixVersion
 * 
 * Represents a particular version of the Rhombix database
 * 
 */
public abstract class RhombixVersion {

    public static final RhombixVersion HELSINKI = new RhombixVersion() {

        @Override
        public String getImageSelectStatement() {
            return HELSINKI_SELECT;
        }

        @Override
        public String getPathPrefix() {
            return HELSINKI_PATH_PREFIX;
        }
    };

    public static final RhombixVersion MPL = new RhombixVersion() {

        @Override
        public String getImageSelectStatement() {
            return MPL_SELECT;
        }

        @Override
        public String getPathPrefix() {
            return "\\\\DC1512086\\Images_Fdrv\\";
        }
    };

    /**
     * Select statement for images, in Helsinki
     */
    private static final String HELSINKI_SELECT =
        "select image.*,  "
            + "image.filename as image_path, "
            + "      row_num, column_num, LU_COMPARTMENT_ID, plate_id"
            + " from   image "
            + " join (select row_num, column_num, LU_COMPARTMENT_ID, well_compartment_id as wc_id, plate_id, well.site_id as s_id\r\n"
            + "    from well_compartment join well on well_compartment.well_id = well.well_id and well_compartment.site_id = well.site_id  \r\n"
            + "    where plate_id = ? " + ") "
            + " on image.well_compartment_id = wc_id and image.site_id = s_id";

    /* 
     where we
     */

    /**
     * Select statement for images, in MPL It seems like the IMAGE_PATH table was added while the system was
     * running.
     */
    private static final String MPL_SELECT =
        "select  nvl(image_path.path, '')||image.filename as image_path,  \r\n"
            + "        image.*, row_num, column_num, LU_COMPARTMENT_ID, plate_id\r\n"
            + "from image  \r\n"
            + "join (\r\n"
            + "    select row_num, column_num, LU_COMPARTMENT_ID, well_compartment_id as wc_id, plate_id, well.site_id as s_id\r\n"
            + "    from well_compartment join well on well_compartment.well_id = well.well_id and well_compartment.site_id = well.site_id  \r\n"
            + "    where plate_id = ?" + ")  on image.well_compartment_id = wc_id and image.site_id=s_id\r\n"
            + "left join image_path on image.image_path_id = image_path.image_path_id";

    private static final String HELSINKI_PATH_PREFIX = "\\\\cdbmaster\\";

    public abstract String getImageSelectStatement();

    /**
     * RhombixVersion.getPathPrefix
     * 
     * @return Obsolete
     */
    public abstract String getPathPrefix();
}
