using System;
using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Imaging;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;

namespace OPPF.Utilities
{
	/// <summary>
	/// Provides functions for creating filenames for the images.
	/// </summary>
	public abstract class FileUtils
	{

        /// <summary>
        /// Old-style filename generator.
        /// DEPRECATED. See GetFilename(IImageInfo, IProcessingInfo, IPlateInfo, IPlateType).
        /// </summary>
        /// <param name="imageInfo"></param>
        /// <param name="iProcessingInfo"></param>
        /// <param name="imageFormat"></param>
        /// <returns></returns>
        /// <see cref="GetFilename(IImageInfo, IProcessingInfo, IPlateInfo, IPlateType)"/>
        public static string GetFilename(IImageInfo imageInfo, IProcessingInfo iProcessingInfo, System.Drawing.Imaging.ImageFormat imageFormat)
        {
            string filename = "";
            filename += iProcessingInfo.PlateID + "-";


            //filename += iProcessingInfo.WellNumber + "-";
            int col = iProcessingInfo.WellNumber % 12;
            if (col == 0) col = 12;
            int row = (int)((double)iProcessingInfo.WellNumber / 12.0);
            if (col == 12) row--;
            char rowChar = (char)(row + 65);

            string wellRef = "" + rowChar + String.Format("{0:00}", col);
            filename += wellRef + "-";


            filename += iProcessingInfo.DropNumber + "-";

            if (imageInfo.IsZNull)
            {
                filename += "z0-";
            }
            else
            {
                filename += "z" + imageInfo.Z + "-";
            }
            filename += iProcessingInfo.RegionID + "-"; // 0
            filename += iProcessingInfo.RegionType + "-"; // Drop
            filename += iProcessingInfo.ProfileID + "-"; // 1
            filename += imageInfo.ImageType + "-";
            //Assuming the imageID is plateID-DATE-TIME format.... (with no dashes in the plateID!)
            string imagingID = iProcessingInfo.ImagingID;
            string dateTime = imagingID.Remove(0, imagingID.IndexOf("-") + 1);
            filename += dateTime;
            filename += "." + imageFormat.ToString().ToLower();

            return filename;
        }

        /// <summary>
        /// This should result in %BARCODE-%WELL-%DROP-R%R%RTP%P-%IT[%II]-z%Z-%DATE-%TIME, eg:
        /// 441312000010-A01-2-R0DRP1-EF-z20-20100316-083621
        /// 441312000010-A01-2-R0DRP1-FL1-z20-20100316-083621
        /// </summary>
        /// <param name="imageInfo"></param>
        /// <param name="processingInfo"></param>
        /// <param name="plateInfo"></param>
        /// <param name="plateType"></param>
        /// <returns></returns>
        public static string GetFilename(IImageInfo imageInfo, IProcessingInfo processingInfo, IPlateInfo plateInfo, IPlateType plateType)
        {
            string filename = "";
            filename += processingInfo.PlateID + "-";
            filename += OPPF.Integrations.ImagerLink.PlateType.WellNumberToString(plateType, processingInfo.WellNumber) + "-";
            filename += processingInfo.DropNumber + "-";

            filename += "R" + processingInfo.RegionID; // Usually 0

            switch (processingInfo.RegionType)  // Usually Drop
            {
                case RegionType.Drop:
                    filename += "DR";
                    break;
                case RegionType.Overview:
                    filename += "OV";
                    break;
                case RegionType.RegionOfInterest:
                    filename += "RI";
                    break;
            }

            filename += "P" + processingInfo.ProfileID + "-"; // Usually 1

            switch (imageInfo.ImageType)
            {
                case ImageType.BestFocus:
                    filename += "BF";
                    break;
                case ImageType.DropLocation:
                    filename += "DL";
                    break;
                case ImageType.ExtendedFocus:
                    filename += "EF";
                    break;
                case ImageType.FocusLevel:
                    filename += "FL" + imageInfo.ImageIndex;
                    break;
            }

            if (imageInfo.IsZNull)
            {
                filename += "-zN-";
            }
            else
            {
                filename += "-z" + System.Convert.ToInt32(imageInfo.Z) + "-";
            }

            // Assuming ImagingID is plateID-DATE-TIME format.... (with no dashes in the plateID!)
            filename += processingInfo.ImagingID.Remove(0, processingInfo.ImagingID.IndexOf("-") + 1);

            return filename;
        }

        /// <summary>
        /// Build the directory for web images from the specified imageInfo and processingInfo.
        /// Special substrings %date% and %plateid% will be replaced with the values from the
        /// specified processingInfo.
        /// </summary>
        /// <param name="imageInfo">The imageInfo (currently unused)</param>
        /// <param name="processingInfo">The processingInfo from which to obtain %date% and %plateid%</param>
        /// <returns>The directory name for web images</returns>
        public static string GetDirectory(IImageInfo imageInfo, IProcessingInfo processingInfo, IRobot robot)
        {
            return GetDirectory(OPPF.Utilities.OPPFConfigXML.GetWebDirectory(), imageInfo, processingInfo, robot);
        }

        /// <summary>
        /// Build a directory from the specified directoryTemplate, imageInfo and processingInfo.
        /// Special substrings %date% and %plateid% will be replaced with the values from the
        /// specified processingInfo.
        /// </summary>
        /// <param name="directoryTemplate">The template for the directory</param>
        /// <param name="imageInfo">The imageInfo (currently unused)</param>
        /// <param name="processingInfo">The processingInfo from which to obtain %date% and %plateid%</param>
        /// <returns>The directory name</returns>
        public static string GetDirectory(string directoryTemplate, IImageInfo imageInfo, IProcessingInfo processingInfo, IRobot robot)
        {
            string directory = directoryTemplate;
            string imagingID = processingInfo.ImagingID;
            string dateTime = imagingID.Remove(0, imagingID.IndexOf("-") + 1);
            string date = dateTime.Remove(dateTime.IndexOf("-"), dateTime.Length - dateTime.IndexOf("-"));
            if (directory.IndexOf("%date%") >= 0)
            {
                directory = directory.Replace("%date%", date);
            }
            if (directory.IndexOf("%plateid%") >= 0)
            {
                directory = directory.Replace("%plateid%", processingInfo.PlateID);
            }
            if (directory.IndexOf("%robot%") >= 0)
            {
                directory = directory.Replace("%robot%", robot.Name);
            }
            if (!((directory.EndsWith("/") || (directory.EndsWith("\\")))))
            {
                directory += "/";
            }

            return directory;
        }

	}
}
