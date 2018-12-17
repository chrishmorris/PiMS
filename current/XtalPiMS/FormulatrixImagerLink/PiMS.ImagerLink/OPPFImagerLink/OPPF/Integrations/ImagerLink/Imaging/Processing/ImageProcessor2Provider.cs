using System;
using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Scheduling;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;
using OPPF.Integrations.ImagerLink.Scheduling;
using OPPF.Utilities;

namespace OPPF.Integrations.ImagerLink.Imaging.Processing
{
	/// <summary>
	/// ImageProcessorProvider provides the mechanism for RockImagerProcessor to
	/// obtain an ImageProcessor from our code.
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
	public class ImageProcessor2Provider : IImageProcessorProvider
	{
        private IPlateInfoProvider _pip;

		public ImageProcessor2Provider()
		{
            // Load configuration
            OPPFConfigXML.Configure();
            _pip = new PlateInfoProvider();
        }

        IImageProcessor IImageProcessorProvider.GetImageProcessor(IRobot robot, IProcessingInfo processingInfo)
		{
            IPlateInfo plateInfo = _pip.GetPlateInfo(robot, processingInfo.PlateID);
            IPlateType plateType = _pip.GetPlateType(robot, plateInfo.PlateTypeID);
			return new ImageProcessor2(robot, processingInfo, plateInfo, plateType);
		}

	}
}
