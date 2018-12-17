using System;

using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Scheduling;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;
using System.Xml;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Drawing.Drawing2D;
// Import log4net classes.
using log4net;
using log4net.Config;
using System.Xml.Serialization;

/*
2010-04-16 09:05:32,674 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:63)  - DropNumber=1;ImagingID="441300245195-20100415-063644";PlatedID="441300245195";ProfileID="1";RegionID="0";RegionType=Drop;WellNumber=1
2010-04-16 09:05:32,752 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:98)  - SetCaptureInfo(captureInfo=;Location=Formulatrix.Integrations.ImagerLink.Imaging.ImageRect;Properties=[[ImagingCondenser=0],[ImagingPolarizer=0],[FixedBrightField=48.3870967741936],[WhiteBalance=1.875841,1,1.353144],[Gamma=0.8],[Gain=1],[FixedExposure=12.52034]]])
2010-04-16 09:05:33,486 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=1;ImageType=FocusLevel;IsZNull=False;Z=0])
2010-04-16 09:05:33,955 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=2;ImageType=FocusLevel;IsZNull=False;Z=59.9122058608242])
2010-04-16 09:05:34,252 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=3;ImageType=FocusLevel;IsZNull=False;Z=119.824411721648])
2010-04-16 09:05:34,533 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=4;ImageType=FocusLevel;IsZNull=False;Z=179.736617582473])
2010-04-16 09:05:34,815 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=5;ImageType=FocusLevel;IsZNull=False;Z=239.648823443297])
2010-04-16 09:05:35,111 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=6;ImageType=FocusLevel;IsZNull=False;Z=299.561029304121])
2010-04-16 09:05:35,393 [Image Processing Queue] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=7;ImageType=FocusLevel;IsZNull=False;Z=359.473235164945])
2010-04-16 09:05:35,799 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:107)  - SetImageInfo(imageInfo=[Image=[Flags=0 => None;FrameDimensionsList=System.Guid[];Height=960;HorizontalResolution=96;Palette=System.Drawing.Imaging.ColorPalette;PhysicalDimension={Width=1280, Height=960};PixelFormat=Format24bppRgb;PropertyIdList=System.Int32[];PropertyItems=System.Drawing.Imaging.PropertyItem[];RawFormat=MemoryBmp;Size={Width=1280, Height=960};Tag=;VerticalResolution=96;Width=1280];ImageIndex=-1;ImageType=ExtendedFocus;IsZNull=False;Z=179.736617582473])
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - ImagingCondenser=0
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - ImagingPolarizer=0
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - FixedBrightField=48.3870967741936
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - WhiteBalance=1.875841,1,1.353144
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - Gamma=0.8
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - Gain=1
2010-04-16 09:05:35,971 [Image Processor] INFO  (C:\Formulatrix\OPPFImagerLink\OPPF\Integrations\ImagerLink\Imaging\Processing\ImageProcessor2.cs:155)  - FixedExposure=12.52034
 */
namespace OPPF.Integrations.ImagerLink.Imaging.Processing
{
	/// <summary>
	/// Class to enable the our system to be notified that an image has been taken.
	/// </summary>
	/// <remarks>The interface is completely undocumented in the ImagerLink code.
	/// Khalid's comments:
	/// <c>IImageProcessorProvider</c> is a factory for biz objects that can save images.
	/// The RIP basically informs the Provider that the RIP has a stack of images for a
	/// Region (some X/Y in space) and a specific Profile (a set of optics/camera
	/// settings). The IImageProcessorProvider then returns a new IImageProcessor,
	/// upon which the RIP then calls SetCaptureInfo() once, and SetImageInfo() for
	/// however many images there are. Afterwards, IImageProcessor.Dispose() is
	/// called if you'd like to do any cleanup.
	/// </remarks>
	public class ImageProcessor2 : IImageProcessor
	{
		private string _regionName;
		private string _profileName;
		private ICaptureInfo _captureInfo;
        private DateTime _dateImaged;

        private int _lightPath;

		private Formulatrix.Integrations.ImagerLink.IRobot _robot;
        private IProcessingInfo _processingInfo;
        private IPlateInfo _plateInfo;
        private IPlateType _plateType;
        private readonly ILog _log;
		
        /// <summary>
        /// Construct an IImageProcessor instance for the specified IRobot and IProcessingInfo.
        /// </summary>
        /// <param name="robot">The Robot that took the images to be processed by this IImageProcessor instance</param>
        /// <param name="processingInfo">The description of the images to be processed by this IImageProcessor instance</param>
		public ImageProcessor2(IRobot robot, IProcessingInfo processingInfo, IPlateInfo plateInfo, IPlateType plateType)
		{
            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

            _dateImaged = DateTime.MinValue;
            _lightPath = 0;

            _robot = robot;
            _processingInfo = processingInfo;
            _plateInfo = plateInfo;
            _plateType = plateType;

            _captureInfo = null;

            SetRegionName(_processingInfo.RegionID);
			SetProfileName(_processingInfo.ProfileID);

            _log.Info(
                "DropNumber=" + processingInfo.DropNumber +
                ";ImagingID=\"" + processingInfo.ImagingID + "\"" +
                ";PlatedID=\"" + processingInfo.PlateID + "\"" +
                ";ProfileID=\"" + processingInfo.ProfileID + "\"" +
                ";RegionID=\"" + processingInfo.RegionID + "\"" +
                ";RegionType=" + processingInfo.RegionType.ToString() +
                ";WellNumber=" + processingInfo.WellNumber +
                ";Robot.Name=\"" + _robot.Name + "\"");
        }

		#region IImageProcessor Members

        string IImageProcessor.RegionName
		{
			get
			{
				return _regionName;
			}
		}

        string IImageProcessor.ProfileName
		{
			get
			{
				return _profileName;
			}
		}

        /// <summary>
        /// Called once per lifetime before any calls to SetImageInfo.
        /// </summary>
        /// <param name="captureInfo">The ICaptureInfo to use</param>
        void IImageProcessor.SetCaptureInfo(ICaptureInfo captureInfo)
		{
			_captureInfo = captureInfo;
            _log.Info("SetCaptureInfo(captureInfo=" + CaptureInfo.ToString(captureInfo) + ")");

            ICaptureInfoExtendedProperties iceip = captureInfo as ICaptureInfoExtendedProperties;
            if (null != iceip)
            {
                _log.Debug("captureInfo is an ICaptureInfoExtendedProperties!");
                this._dateImaged = iceip.DateImaged.ToUniversalTime();
                this._lightPath = iceip.LightPath;
                this._plateInfo = iceip.PlateInfo;
                this._plateType = iceip.PlateType;
            }

            else
            {

                // JMD Had to change the default
                //int lightPath = 0;
                int lightPath = 1;
                foreach (IProperty p in _captureInfo.Properties)
                {
                    //_log.Info(p.Name + "=" + p.Value);

                    // JMD Broken as of 31/03/2011
                    //if (p.Name.EndsWith("UV"))
                    //{
                    //    lightPath = 1;
                    //    break;
                    //}

                    // Try again looking for a visible-only property
                    if (("ImagingCondenser".Equals(p.Name)) || ("ImagingPolarizer".Equals(p.Name)))
                    {
                        lightPath = 0;
                        break;
                    }

                }

                _lightPath = lightPath;
            }

        }

        /// <summary>
        /// Called once per image in the stack
        /// </summary>
        /// <param name="imageInfo"></param>
        void IImageProcessor.SetImageInfo(IImageInfo imageInfo)
		{
            _log.Info("SetImageInfo(imageInfo=" + ImageInfo.ToString(imageInfo) + ")");
			try 
			{

                // Get filename - NB no suffix - save routines to add suffix
                string filename = OPPF.Utilities.FileUtils.GetFilename(imageInfo, _processingInfo, _plateInfo, _plateType);

                // The image format is probably irrelevant - jpg for web, tiff or png for archive
                // TODO: remove this
                //ImageFormat imgFormat = OPPF.Utilities.ImageFile.getImageFormat(imageFormat);

                string imageDir = OPPF.Utilities.FileUtils.GetDirectory(OPPF.Utilities.OPPFConfigXML.GetWebDirectory(), imageInfo, _processingInfo, _robot);
                if (!Directory.Exists(imageDir))
                {
                    Directory.CreateDirectory(imageDir);
                }

                string xmlDir = OPPF.Utilities.FileUtils.GetDirectory(OPPF.Utilities.OPPFConfigXML.GetXmlDirectory(), imageInfo, _processingInfo, _robot);
                if (!Directory.Exists(xmlDir))
                {
                    Directory.CreateDirectory(xmlDir);
                }

                // Handle all images for archiving as tiffs (unless archiveDir is "FALSE")
                // TODO: embed metadata
                // TODO: write image - as png or tiff?
                string rawArchiveDir = OPPF.Utilities.OPPFConfigXML.GetArchiveDirectory().Trim();
                if (!"FALSE".Equals(rawArchiveDir, StringComparison.InvariantCultureIgnoreCase))
                {
                    string archiveDir = OPPF.Utilities.FileUtils.GetDirectory(rawArchiveDir, imageInfo, _processingInfo, _robot);
                    if (!Directory.Exists(archiveDir))
                    {
                        Directory.CreateDirectory(archiveDir);
                    }
                    OPPF.Utilities.Imaging.ImageUtils.saveAsTiff(imageInfo.Image, Path.Combine(archiveDir, filename));
                }

                if (ImageType.ExtendedFocus.Equals(imageInfo.ImageType))
                {
                    // Handle EFI image for web site
                    // TODO: Do we also need to handle ImageType.BestFocus?

                    // TODO: downscale
                    // TODO: set max size
                    Size maxSize = Size.Empty;
                    Image webImage = OPPF.Utilities.Imaging.ImageUtils.resizeImage(imageInfo.Image, maxSize);

                    /*
                     * TODO: Embed metadata, eg from logging below:
                     *   ImagingCondenser=0
                     *   ImagingPolarizer=0
                     *   FixedBrightField=48.3870967741936
                     *   WhiteBalance=1.875841,1,1.353144
                     *   Gamma=0.8
                     *   Gain=1
                     *   FixedExposure=12.52034
                     *
                    foreach (IProperty p in _captureInfo.Properties)
                    {
                        _log.Info(p.Name + "=" + p.Value);
                        // TODO: Map to System.Imaging.PropertyItems
                        // TODO: what does Image.Tag do?
                    }
                     */

                    // Write jpeg direct to web site
                    // TODO: Can I do this with webdav? Would I want to?
                    // TODO: Quality should be configurable
                    OPPF.Utilities.Imaging.ImageUtils.saveAsJpeg(webImage, imageDir + filename, 85L);

                    // Prepare xml for async db update - what info do I need?
                    // - URL
                    // - ImagingID
                    // - Date/Time
                    // - Well
                    // - Drop
                    // - Mag
                    // - Resolution
                    OPPF.XML.ImageInfo xml = new OPPF.XML.ImageInfo();

                    xml.Drop = PlateType.WellNumberToString(_plateType, _processingInfo.WellNumber) + "." + _processingInfo.DropNumber;

                    if (DateTime.MinValue.Equals(this._dateImaged))
                    {
                        xml.ImagedAt = this.GetDoneFileLastWriteTimeUtc();
                    }
                    else
                    {
                        xml.ImagedAt = this._dateImaged;
                    }

                    xml.Imager = _robot.Name;

                    if (ImageType.ExtendedFocus.Equals(imageInfo.ImageType))
                    {
                        xml.ImageType = OPPF.XML.ImageInfoImageType.C;
                    }
                    else
                    {
                        xml.ImageType = OPPF.XML.ImageInfoImageType.S;
                    }

                    xml.ImagingId = _processingInfo.ImagingID;

                    xml.PlateId = _processingInfo.PlateID;

                    OPPF.XML.DoubleSize microns = new OPPF.XML.DoubleSize();
                    microns.Height = _captureInfo.Location.Height;
                    microns.Width = _captureInfo.Location.Width;
                    xml.SizeInMicrons = microns;

                    OPPF.XML.Size pixels = new OPPF.XML.Size();
                    pixels.Height = webImage.Height;
                    pixels.Width = webImage.Width;
                    xml.SizeInPixels = pixels;


                    // TODO Make configurable!
                    xml.Url = OPPF.Utilities.OPPFConfigXML.GetImageBaseUrl() + _processingInfo.PlateID + "/" + filename + ".jpg"; // TODO: complete this

                    if (imageInfo.IsZNull)
                    {
                        xml.Z = 0;
                    }
                    else
                    {
                        xml.Z = imageInfo.Z;
                    }

                    // TODO: where can I get the zoom from? Actually unavailable!
                    // Need to know field of view at zoom = 1
                    // Different lightpaths have different fields!
                    if (1 == _lightPath)
                    {
                        // Fixed zoom on UV lightpath
                        xml.Zoom = 3.6;
                    }
                    else
                    {
                        // QImaging.config says pixel size is 2.994
                        // Need to know binning settings, which I can get from the image size v camera size
                        // So:
                        double cameraPixelWidth = 2.994;
                        int cameraWidthInPixels = 2560;
                        int bin = cameraWidthInPixels / pixels.Width;
                        double binnedCameraPixelWidth = cameraPixelWidth * bin;
                        double imagePixelWidth = _captureInfo.Location.Width / pixels.Width; // Might round microns.Width
                        double zoom = binnedCameraPixelWidth / imagePixelWidth;
                        xml.Zoom = Math.Round(zoom, 3);
                    }

                    // Write XML
                    XmlSerializer serializer = new XmlSerializer(xml.GetType());
                    TextWriter writer = new StreamWriter(xmlDir + filename + ".xml");
                    serializer.Serialize(writer, xml);
                    writer.Close();
                }


			} 
			catch (Exception e) 
			{
                string msg = "Error saving image from Formulatrix: " + imageInfo.ImageType + " for drop " + _processingInfo.DropNumber + " in plate " + _processingInfo.PlateID + ", imaging id " + _processingInfo.ImagingID + ": " + e.Message;
                _log.Error(msg, e);
                throw new ProcessingException(msg, e);
			}
			
		}

		#endregion

		#region IDisposable Members

		/// <summary>
		/// This method is intended to cause any resources that are no
		/// longer necessary to be released
		/// </summary>
		void IDisposable.Dispose()
		{
			_regionName = null;
			_profileName = null;
			_captureInfo = null;
			_robot = null;
            _processingInfo = null;
            _plateInfo = null;
            _plateType = null;
        }

		#endregion

        #region Set methods for interface properties

        public void SetRegionName(string regionName)
        {
            _regionName = regionName;
        }

        public void SetProfileName(string profileName)
        {
            _profileName = profileName;
        }

        #endregion

        /// <summary>
		/// Get the IRobot attached to this ImageProcessor at creation time
		/// </summary>
		public Formulatrix.Integrations.ImagerLink.IRobot Robot
		{
			get
			{
				return _robot;
			}
		}

		/// <summary>
		/// Get the IProcessingInfo attached to this ImageProcessor at creation time
		/// </summary>
		public IProcessingInfo ProcessingInfo
		{
			get
			{
				return _processingInfo;
			}
		}


        private DateTime GetDoneFileLastWriteTimeUtc()
        {
            //return File.GetLastWriteTimeUtc(donePath);
            DateTime lastWritten;
            String donePath = Path.Combine(OPPF.Utilities.OPPFConfigXML.GetDoneDirectory(), GetDoneFileName());
            _log.Info("Done file should be: " + donePath);
            if (File.Exists(donePath))
            {
                _log.Info("Done file exists at: " + donePath);
                lastWritten = File.GetLastWriteTimeUtc(donePath);
            }
            else
            {
                _log.Info("Done file missing at: " + donePath);
                lastWritten = DateTime.Now.ToUniversalTime();
            }
            _log.Info("Returning: " + lastWritten.ToString());
            return lastWritten;
        }

        private string GetDoneFileName()
        {
               return _processingInfo.PlateID + "_" + _processingInfo.ImagingID + "_" + _processingInfo.WellNumber + "_" + _processingInfo.DropNumber + "_" + _lightPath + ".done";
        }

    }
}
