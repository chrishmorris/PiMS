using System;

using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;
using System.Xml;
using System.IO;
// Import log4net classes.
using log4net;
using log4net.Config;

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
	public class ImageProcessor : IImageProcessor
	{
		private string _regionName;
		private string _profileName;
		private ICaptureInfo _captureInfo;
		private IImageInfo _imageInfo;

		private IRobot _robot;
		private IProcessingInfo _processingInfo;
		private readonly ILog _log;
		
		public ImageProcessor(Formulatrix.Integrations.ImagerLink.IRobot robot, IProcessingInfo processingInfo)
		{
            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

			SetRegionName("");
			SetProfileName("");
			_captureInfo = null;
			_imageInfo = null;
			_robot = robot;
			_processingInfo = processingInfo;
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

        void IImageProcessor.SetCaptureInfo(ICaptureInfo captureInfo)
		{
			_captureInfo = captureInfo;
		}

        void IImageProcessor.SetImageInfo(IImageInfo imageInfo)
		{
			_imageInfo = imageInfo;
			try 
			{
				_log.Debug("Starting setImageInfo.");
				//Get the required format to save the image...
				string imageFormat = OPPF.Utilities.OPPFConfigXML.GetImageFormat();
				_log.Debug("ImageFormat read.");
				//Then create the filename from the information from the imager.
                string filename = OPPF.Utilities.FileUtils.GetFilename(imageInfo, _processingInfo, OPPF.Utilities.Imaging.ImageUtils.GetImageFormat(imageFormat));
				_log.Debug("Filename successfully read generated.");
				string directory = OPPF.Utilities.FileUtils.GetDirectory(imageInfo, _processingInfo, _robot);
				
				if (!Directory.Exists(directory)) 
				{
					Directory.CreateDirectory(directory);
				}

				//and then simply save the image!
                imageInfo.Image.Save(directory + filename, OPPF.Utilities.Imaging.ImageUtils.GetImageFormat(imageFormat));
				_log.Debug("Image successfully saved.");
			} 
			catch (Exception e) 
			{
				_log.Error("Error saving image from Formulatrix: " + imageInfo.ImageType + " for drop " + _processingInfo.DropNumber + " in plate " + _processingInfo.PlateID + ", imaging id " + _processingInfo.ImagingID, e);
				throw e;
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
			_imageInfo = null;
			_robot = null;
			_processingInfo = null;
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

	}
}
