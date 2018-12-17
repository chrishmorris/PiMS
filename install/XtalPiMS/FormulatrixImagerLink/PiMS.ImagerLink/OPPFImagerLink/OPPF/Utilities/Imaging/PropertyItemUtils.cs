using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;

namespace OPPF.Utilities.Imaging
{
    class PropertyItemUtils
    {

        // ---------------------------------------------------------------
        // Date      140607
        // Purpose   Convert Base64 code to Bitmap.
        // Entry     sBase64 - The Base64 code (as single string).
        // Return    The bitmap.
        // Comments  
        // ---------------------------------------------------------------
        private static Bitmap Base64_To_Bitmap(string sBase64)
        {
            Byte[] bytData = Convert.FromBase64String(sBase64);
            MemoryStream ms = new MemoryStream(bytData);
            Bitmap oB = new Bitmap(ms);
            ms.Close();
            ms.Dispose();
            return oB;
        }

        // Icon Base_8_8.png (8x8).
        private string Icon_Base_8_8 =
        "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAAAXNSR0IArs4c6QAAAARnQU1BAACx" +
        "jwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABZJREFU" +
        "KFNj/A8EDPgASAE+wDAsFAAAxQLjHkhOEUMAAAAASUVORK5CYIIAAAAAAAAAAAAAAAAAAAAAAAAA" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==";

        // ---------------------------------------------------------------
        // Date      110708
        // Purpose   Create a new PropertyItem from scratch.
        // Entry     iId - The PropertyItem ID.
        //           iType - The PropertyItem type.
        //           sValue - The PropertyItem value (as string).
        // Return    A new PropertyItem object.
        // Comments  The first PropertyItem from a dummy-image is used
        //           for making a new item.
        // ---------------------------------------------------------------
        public PropertyItem CreateNewPropertyItem(int iId, int iType, string sValue)
        {
            ASCIIEncoding textConverter = new ASCIIEncoding();
            Bitmap oBitmap = Base64_To_Bitmap(Icon_Base_8_8);
            PropertyItem[] propItems = oBitmap.PropertyItems;
            if (propItems.Length != 0)
            {
                propItems[0].Id = iId;                  // 0x010E = PropertyTagImageDescription.
                propItems[0].Type = (short)iType;       // Null-terminated ASCII string.
                propItems[0].Value = textConverter.GetBytes(sValue);
                propItems[0].Len = sValue.Length;       // Length (+1 ?).
                return propItems[0];
            }
            else
            {
                return null;
            }
        }

        public PropertyItem CreateNewPropertyItemByte(int id, byte[] value)
        {
            return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeByte, value);
        }

        public PropertyItem CreateNewPropertyItemASCII(int id, string value)
        {
            ASCIIEncoding textConverter = new ASCIIEncoding();
            // TODO: Confirm textConverter.GetBytes is null terminated
            return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeASCII, textConverter.GetBytes(value));
        }

        public PropertyItem CreateNewPropertyItemShort(int id, short value)
        {
            // TODO: Implement
            throw new System.NotImplementedException();
            //return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeASCII, value);
        }

        public PropertyItem CreateNewPropertyItemLong(int id, long value)
        {
            // TODO: Implement
            throw new System.NotImplementedException();
            //return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeLong, value);
        }

        public PropertyItem CreateNewPropertyItemRational(int iId, string sValue)
        {
            // TODO: Implement
            throw new System.NotImplementedException();
            //return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeRational, value);
        }

        public PropertyItem CreateNewPropertyItemUndefined(int id, byte[] value)
        {
            return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeUndefined, value);
        }

        public PropertyItem CreateNewPropertyItemSLONG(int id, long value)
        {
            // TODO: Implement
            throw new System.NotImplementedException();
            //return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeSLONG, value);
        }

        public PropertyItem CreateNewPropertyItemSRational(int id, string value)
        {
            // TODO: Implement
            throw new System.NotImplementedException();
            //return CreateNewPropertyItem(id, PropertyTagType.PropertyTagTypeSRational, value);
        }

        public PropertyItem CreateNewPropertyItem(int id, PropertyTagType tagType, byte[] value)
        {
            Bitmap oBitmap = Base64_To_Bitmap(Icon_Base_8_8);
            PropertyItem[] propItems = oBitmap.PropertyItems;
            if (propItems.Length != 0)
            {
                propItems[0].Id = id;                  // 0x010E = PropertyTagImageDescription.
                propItems[0].Type = (short)tagType;       // Null-terminated ASCII string.
                propItems[0].Value = value;
                propItems[0].Len = value.Length;       // Length (+1 ?).
                return propItems[0];
            }
            else
            {
                return null;
            }
        }

        // ---------------------------------------------------------------
        // Date      110708
        // Purpose   Get value of the PropertyItem with 
        //           ID = PropertyTagImageDescription (0x010E) of an image.
        // Entry     oBitmap - The bitmap from the image.
        // Return    The vallue from the PropertyItem (as string).
        // Comments  PropertyTagImageDescription. 0x010E = 270.
        // ---------------------------------------------------------------
        public string GetImageDescription(Bitmap oBitmap)
        {
            // int iH = 270;
            // MessageBox.Show(iH.ToString("x2"));

            int iID = 270;          // 0x010E = PropertyTagImageDescription.
            ASCIIEncoding textConverter = new ASCIIEncoding();

            string sValue = "";
            PropertyItem[] propItems = oBitmap.PropertyItems;
            for (int i = 0; i < propItems.Length; i++)
            {
                if (propItems[i].Id == iID)
                {
                    sValue = textConverter.GetString(propItems[i].Value);
                }
            }
            return sValue;
        }

        // ---------------------------------------------------------------
        // Date      110708
        // Purpose   Set the value of the PropertyItem with 
        //           ID = PropertyTagImageDescription for an image.
        // Entry     oBitmap - The bitmap from the image.
        //           sImageDescription - The value for the PropertyItem.
        // Return    None (Bitmap is returned by ref)
        // Comments  When the PropertyItem with ID = PropertyTagImageDescription
        //           exists, the value of the item is replaced.
        //           When the PropertyItem does not exist, it is created and
        //           added to the image.
        // ---------------------------------------------------------------
        public void SetImageDescription(ref Bitmap oBitmap, string sImageDescription)
        {

            int iID = 270;          // 0x010E = PropertyTagImageDescription.
            bool bItemExists = false;

            PropertyItem[] propItems = oBitmap.PropertyItems;

            // Set the value for an existing PropertyItem with 
            // ID = PropertyTagImageDescription.
            for (int i = 0; i < propItems.Length; i++)
            {
                if (propItems[i].Id == iID)
                {
                    byte[] bytValue;
                    ASCIIEncoding textConverter = new ASCIIEncoding();
                    bytValue = textConverter.GetBytes(sImageDescription);
                    propItems[i].Value = bytValue;
                    oBitmap.SetPropertyItem(propItems[i]);
                    bItemExists = true;
                    break;
                }
            }

            // Create a new PropertyItem with ID = PropertyTagImageDescription (0x010E)
            // when such an item does not exist.
            if (bItemExists == false)
            {
                int iType = 2;              // = ASCII string.
                string sValue = sImageDescription;
                PropertyItem oItem = CreateNewPropertyItem(iID, iType, sValue);
                // Add this new item to the image.
                oBitmap.SetPropertyItem(oItem);
            }
        }

        // ---------------------------------------------------------------
        // Date      110708
        // Purpose   Get info from all the property-items in an image.
        // Entry     oBitmap - The bitmap from the image.
        // Return    A single string with the property-items info.
        // Comments  The PropertyItem Value bytes are converted to string. 
        // ---------------------------------------------------------------
        public string Image_GetPropertyItemsInfo(Bitmap oBitmap)
        {
            string s = "";
            string r = "\r\n";
            string sID;

            PropertyItem[] propItems = oBitmap.PropertyItems;

            s += "Number of items: " + propItems.Length.ToString() + r;
            s += r;
            for (int i = 0; i < propItems.Length; i++)
            {
                s += "Item: " + i.ToString() + r;        // Item number in PropertyItem[].
                sID = propItems[i].Id.ToString("x2");                // Hex.
                if (sID.Length == 1) sID = "0x000" + sID;
                if (sID.Length == 2) sID = "0x00" + sID;
                if (sID.Length == 3) sID = "0x0" + sID;
                if (sID.Length == 4) sID = "0x" + sID;
                s += "ID: " + sID + r;                               // ID, format 0xHHHH.
                s += "Type: " + propItems[i].Type.ToString() + r;    // Type (1..10).
                s += "Length: " + propItems[i].Len.ToString() + r;   // Length (bytes).
                s += r;
            }
            return s;
        }
    }

    /// <summary>
    /// PropertyTagType constants
    /// </summary>
    public enum PropertyTagType
    {
        PropertyTagTypeByte = 1,
        PropertyTagTypeASCII = 2,
        PropertyTagTypeShort = 3,
        PropertyTagTypeLong = 4,
        PropertyTagTypeRational = 5,
        PropertyTagTypeUndefined = 7,
        PropertyTagTypeSLONG = 9,
        PropertyTagTypeSRational = 10
    }

    public enum ImagePropertyIDTags
    {

        // Added Manually
        // The following items correspond to the EXIF IDs that
        // are set by windows file explorer when edited in the
        // properties dialog summary page.
        FileExplorerTitle = 0x9c9b,
        FileExplorerSubject = 0x9c9f,
        FileExplorerKeywords = 0x9c9e,
        FileExplorerComments = 0x9c9c,

        // Taken from GDIConstants.h
        ExifIFD = 0x8769,
        GpsIFD = 0x8825,

        NewSubfileType = 0x00FE,
        SubfileType = 0x00FF,
        ImageWidth = 0x0100,
        ImageHeight = 0x0101,
        BitsPerSample = 0x0102,
        Compression = 0x0103,
        PhotometricInterp = 0x0106,
        ThreshHolding = 0x0107,
        CellWidth = 0x0108,
        CellHeight = 0x0109,
        FillOrder = 0x010A,
        DocumentName = 0x010D,
        ImageDescription = 0x010E,
        EquipMake = 0x010F,
        EquipModel = 0x0110,
        StripOffsets = 0x0111,
        Orientation = 0x0112,
        SamplesPerPixel = 0x0115,
        RowsPerStrip = 0x0116,
        StripBytesCount = 0x0117,
        MinSampleValue = 0x0118,
        MaxSampleValue = 0x0119,
        XResolution = 0x011A,   // Image resolution in width direction,
        YResolution = 0x011B,   // Image resolution in height direction,
        PlanarConfig = 0x011C,   // Image data arrangement,
        PageName = 0x011D,
        XPosition = 0x011E,
        YPosition = 0x011F,
        FreeOffset = 0x0120,
        FreeByteCounts = 0x0121,
        GrayResponseUnit = 0x0122,
        GrayResponseCurve = 0x0123,
        T4Option = 0x0124,
        T6Option = 0x0125,
        ResolutionUnit = 0x0128,   // Unit of X and Y resolution,
        PageNumber = 0x0129,
        TransferFuncition = 0x012D,
        SoftwareUsed = 0x0131,
        DateTime = 0x0132,
        Artist = 0x013B,
        HostComputer = 0x013C,
        Predictor = 0x013D,
        WhitePoint = 0x013E,
        PrimaryChromaticities = 0x013F,
        ColorMap = 0x0140,
        HalftoneHints = 0x0141,
        TileWidth = 0x0142,
        TileLength = 0x0143,
        TileOffset = 0x0144,
        TileByteCounts = 0x0145,
        InkSet = 0x014C,
        InkNames = 0x014D,
        NumberOfInks = 0x014E,
        DotRange = 0x0150,
        TargetPrinter = 0x0151,
        ExtraSamples = 0x0152,
        SampleFormat = 0x0153,
        SMinSampleValue = 0x0154,
        SMaxSampleValue = 0x0155,
        TransferRange = 0x0156,

        JPEGProc = 0x0200,
        JPEGInterFormat = 0x0201,
        JPEGInterLength = 0x0202,
        JPEGRestartInterval = 0x0203,
        JPEGLosslessPredictors = 0x0205,
        JPEGPointTransforms = 0x0206,
        JPEGQTables = 0x0207,
        JPEGDCTables = 0x0208,
        JPEGACTables = 0x0209,

        YCbCrCoefficients = 0x0211,
        YCbCrSubsampling = 0x0212,
        YCbCrPositioning = 0x0213,
        REFBlackWhite = 0x0214,

        ICCProfile = 0x8773,   // This TAG is defined by ICC,
        // for embedded ICC in TIFF,
        Gamma = 0x0301,
        ICCProfileDescriptor = 0x0302,
        SRGBRenderingIntent = 0x0303,

        ImageTitle = 0x0320,
        Copyright = 0x8298,

        // Extra TAGs (Like Adobe Image Information tags etc.)

        ResolutionXUnit = 0x5001,
        ResolutionYUnit = 0x5002,
        ResolutionXLengthUnit = 0x5003,
        ResolutionYLengthUnit = 0x5004,
        PrintFlags = 0x5005,
        PrintFlagsVersion = 0x5006,
        PrintFlagsCrop = 0x5007,
        PrintFlagsBleedWidth = 0x5008,
        PrintFlagsBleedWidthScale = 0x5009,
        HalftoneLPI = 0x500A,
        HalftoneLPIUnit = 0x500B,
        HalftoneDegree = 0x500C,
        HalftoneShape = 0x500D,
        HalftoneMisc = 0x500E,
        HalftoneScreen = 0x500F,
        JPEGQuality = 0x5010,
        GridSize = 0x5011,
        ThumbnailFormat = 0x5012,  // 1 = JPEG, 0 = RAW RGB,
        ThumbnailWidth = 0x5013,
        ThumbnailHeight = 0x5014,
        ThumbnailColorDepth = 0x5015,
        ThumbnailPlanes = 0x5016,
        ThumbnailRawBytes = 0x5017,
        ThumbnailSize = 0x5018,
        ThumbnailCompressedSize = 0x5019,
        ColorTransferFunction = 0x501A,
        ThumbnailData = 0x501B, // RAW thumbnail bits in,
        // JPEG format or RGB format,
        // depends on,
        // ThumbnailFormat,

        // Thumbnail related TAGs,

        ThumbnailImageWidth = 0x5020,  // Thumbnail width,
        ThumbnailImageHeight = 0x5021,  // Thumbnail height,
        ThumbnailBitsPerSample = 0x5022,  // Number of bits per,
        // component,
        ThumbnailCompression = 0x5023,  // Compression Scheme,
        ThumbnailPhotometricInterp = 0x5024, // Pixel composition,
        ThumbnailImageDescription = 0x5025,  // Image Tile,
        ThumbnailEquipMake = 0x5026,  // Manufacturer of Image,
        // Input equipment,
        ThumbnailEquipModel = 0x5027,  // Model of Image input,
        // equipment,
        ThumbnailStripOffsets = 0x5028,  // Image data location,
        ThumbnailOrientation = 0x5029,  // Orientation of image,
        ThumbnailSamplesPerPixel = 0x502A,  // Number of components,
        ThumbnailRowsPerStrip = 0x502B,  // Number of rows per strip,
        ThumbnailStripBytesCount = 0x502C,  // Bytes per compressed,
        // strip,
        ThumbnailResolutionX = 0x502D,  // Resolution in width,
        // direction,
        ThumbnailResolutionY = 0x502E,  // Resolution in height,
        // direction,
        ThumbnailPlanarConfig = 0x502F,  // Image data arrangement,
        ThumbnailResolutionUnit = 0x5030,  // Unit of X and Y,
        // Resolution,
        ThumbnailTransferFunction = 0x5031,  // Transfer function,
        ThumbnailSoftwareUsed = 0x5032,  // Software used,
        ThumbnailDateTime = 0x5033,  // File change date and,
        // time,
        ThumbnailArtist = 0x5034,  // Person who created the,
        // image,
        ThumbnailWhitePoint = 0x5035,  // White point chromaticity,
        ThumbnailPrimaryChromaticities = 0x5036,
        // Chromaticities of,
        // primaries,
        ThumbnailYCbCrCoefficients = 0x5037, // Color space transforma-
        // tion coefficients,
        ThumbnailYCbCrSubsampling = 0x5038, // Subsampling ratio of Y,
        // to C,
        ThumbnailYCbCrPositioning = 0x5039,  // Y and C position,
        ThumbnailRefBlackWhite = 0x503A,  // Pair of black and white,
        // reference values,
        ThumbnailCopyRight = 0x503B,  // CopyRight holder,

        LuminanceTable = 0x5090,
        ChrominanceTable = 0x5091,

        FrameDelay = 0x5100,
        LoopCount = 0x5101,

        PixelUnit = 0x5110,  // Unit specifier for pixel/unit,
        PixelPerUnitX = 0x5111,  // Pixels per unit in X,
        PixelPerUnitY = 0x5112,  // Pixels per unit in Y,
        PaletteHistogram = 0x5113,  // Palette histogram,

        // EXIF specific tag,

        ExifExposureTime = 0x829A,
        ExifFNumber = 0x829D,

        ExifExposureProg = 0x8822,
        ExifSpectralSense = 0x8824,
        ExifISOSpeed = 0x8827,
        ExifOECF = 0x8828,

        ExifVer = 0x9000,
        ExifDTOrig = 0x9003, // Date & time of original,
        ExifDTDigitized = 0x9004, // Date & time of digital data generation,

        ExifCompConfig = 0x9101,
        ExifCompBPP = 0x9102,

        ExifShutterSpeed = 0x9201,
        ExifAperture = 0x9202,
        ExifBrightness = 0x9203,
        ExifExposureBias = 0x9204,
        ExifMaxAperture = 0x9205,
        ExifSubjectDist = 0x9206,
        ExifMeteringMode = 0x9207,
        ExifLightSource = 0x9208,
        ExifFlash = 0x9209,
        ExifFocalLength = 0x920A,
        ExifMakerNote = 0x927C,
        ExifUserComment = 0x9286,
        ExifDTSubsec = 0x9290,  // Date & Time subseconds,
        ExifDTOrigSS = 0x9291,  // Date & Time original subseconds,
        ExifDTDigSS = 0x9292,  // Date & TIme digitized subseconds,

        ExifFPXVer = 0xA000,
        ExifColorSpace = 0xA001,
        ExifPixXDim = 0xA002,
        ExifPixYDim = 0xA003,
        ExifRelatedWav = 0xA004,  // related sound file,
        ExifInterop = 0xA005,
        ExifFlashEnergy = 0xA20B,
        ExifSpatialFR = 0xA20C,  // Spatial Frequency Response,
        ExifFocalXRes = 0xA20E,  // Focal Plane X Resolution,
        ExifFocalYRes = 0xA20F,  // Focal Plane Y Resolution,
        ExifFocalResUnit = 0xA210,  // Focal Plane Resolution Unit,
        ExifSubjectLoc = 0xA214,
        ExifExposureIndex = 0xA215,
        ExifSensingMethod = 0xA217,
        ExifFileSource = 0xA300,
        ExifSceneType = 0xA301,
        ExifCfaPattern = 0xA302,

        GpsVer = 0x0000,
        GpsLatitudeRef = 0x0001,
        GpsLatitude = 0x0002,
        GpsLongitudeRef = 0x0003,
        GpsLongitude = 0x0004,
        GpsAltitudeRef = 0x0005,
        GpsAltitude = 0x0006,
        GpsGpsTime = 0x0007,
        GpsGpsSatellites = 0x0008,
        GpsGpsStatus = 0x0009,
        GpsGpsMeasureMode = 0x00A,
        GpsGpsDop = 0x000B,  // Measurement precision,
        GpsSpeedRef = 0x000C,
        GpsSpeed = 0x000D,
        GpsTrackRef = 0x000E,
        GpsTrack = 0x000F,
        GpsImgDirRef = 0x0010,
        GpsImgDir = 0x0011,
        GpsMapDatum = 0x0012,
        GpsDestLatRef = 0x0013,
        GpsDestLat = 0x0014,
        GpsDestLongRef = 0x0015,
        GpsDestLong = 0x0016,
        GpsDestBearRef = 0x0017,
        GpsDestBear = 0x0018,
        GpsDestDistRef = 0x0019,
        GpsDestDist = 0x001A,

    }

}
