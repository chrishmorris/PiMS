/*
 * CaptureProfile.cs - implementation of Formulatrix.Integrations.ImagerLink.Imaging.ICaptureProfile.
 * Copyright 2010 Jonathan Diprose <jon@strubi.ox.ac.uk>
 */

using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Imaging;

namespace OPPF.Integrations.ImagerLink.Imaging
{
	/// <summary>
	/// Specifies a unique id and a set of properties for imaging.
	/// </summary>
	/// <remarks>
    /// This is a basic bean implementation of <c>ICaptureProfile</c>, with setters
	/// as well as the interface-defined getters.
    /// 
    /// To use Ultraviolet imaging, the Properties list in the ICaptureProfile for the plate must return
    /// an IProperty with the Name 'LightPath' and the Value set to the integer '1'. Visible light is the
    /// default (0) and may be omitted.
    /// 
    /// The other IProperties that can be used in UV capture profiles are:
    /// 
    /// GainUV
    /// Double indicating the gain to be applied to the camera
    /// 
    /// GammaUV
    /// Double indicating the gamma setting to be applied to the image
    /// 
    /// ImagingBinningUV
    /// Integer indicating the binning to be used (1 = normal, 2 = 2x2 binning, 4 = 4x4 binning)
    /// 
    /// FixedExposureUV
    /// Double indicating the exposure time for the camera, in milliseconds.
    /// 
    /// As always, if these properties are not set then the imager defaults set in RockImager will be used.
    /// </remarks>
	public class CaptureProfile : ICaptureProfile
	{
		private string _profileID;
		private IProperty[] _properties;

		public CaptureProfile()
		{
			SetProfileID("");
			SetProperties(null);
		}

		#region ICaptureProfile Members

		/// <summary>
		/// The unique ID identifying the capture profile.
		/// </summary>
        string ICaptureProfile.ProfileID
		{
			get
			{
				return _profileID;
			}
		}

		/// <summary>
		/// The current set of properties for imaging.
		/// </summary>
        IProperty[] ICaptureProfile.Properties
		{
			get
			{
				return _properties;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetProfileID(string profileID)
        {
            _profileID = profileID;
        }

        public void SetProperties(IProperty[] properties)
        {
            _properties = properties;
        }

        #endregion

    }
}
