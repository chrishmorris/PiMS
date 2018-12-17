/*
 * CaptureProvider203.cs - implementation of Formulatrix.Integrations.ImagerLink.Imaging.ICaptureProvider
 * and Formulatrix.Integrations.ImagerLink.Imaging.ICaptureProfiles.
 * Copyright 2010 Jonathan Diprose <jon@strubi.ox.ac.uk>
 */

using System.Collections.Generic;
using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Imaging;
using log4net;

namespace OPPF.Integrations.ImagerLink.Imaging
{
	/// <summary>
	/// Returns information about imaging captures.
	/// </summary>
	/// <remarks>
    /// This class implements the ICaptureProfiles interface added in RockImager 2.0.3 and
    /// extends CaptureProvider for the ICaptureProvider implementation.
	/// </remarks>
    public class CaptureProvider203 : CaptureProvider, ICaptureProvider, ICaptureProfiles
	{

        /// <summary>
        /// The logger.
        /// </summary>
        private readonly ILog _log;

        /// <summary>
        /// The current set of capture profiles for imaging with selectable capture profile feature.
        /// </summary>
        private List<IProperty> _captureProfiles;

		public CaptureProvider203()
		{
            // Get Logger
            _log = LogManager.GetLogger(this.GetType());

            // Log the call to the constructor
            if (_log.IsDebugEnabled)
            {
                _log.Debug("Constructed a new " + this);
            }

            // No selectable capture profiles
            //SetCaptureProfiles(null);

            // Selectable capture profiles
            // TODO: Shift to DB
            List<IProperty> profiles = new List<IProperty>(2);
            profiles.Add(new OPPF.Integrations.ImagerLink.Property("OPPF Vis", "1"));
            profiles.Add(new OPPF.Integrations.ImagerLink.Property("OPPF UV", "2"));
            SetCaptureProfiles(profiles);
        }
	
        #region ICaptureProfiles Members

        /// <summary>
        /// Collect the current set of capture profiles for imaging with selectable capture profile feature.
        /// Return an empty list if you only need to do imaging with default capture profile.
        /// </summary>
        List<IProperty> ICaptureProfiles.CaptureProfiles
        {
            get
            {
                if (_log.IsDebugEnabled)
                {
                    _log.Debug("Called ICaptureProfiles.CaptureProfiles.get: returning " + _captureProfiles.Count + " capture profiles");
                }
                return _captureProfiles;
            }
        }

        #endregion

        #region Set methods for interface properties

        /// <summary>
        /// Set the current set of capture profiles for imaging with selectable capture profile feature.
        /// </summary>
        /// <param name="captureProfiles">The list of IProperty to set</param>
        public void SetCaptureProfiles(List<IProperty> captureProfiles)
        {
            if (null == captureProfiles)
            {
                _captureProfiles = new List<IProperty>(0);
            }
            else
            {
                _captureProfiles = captureProfiles;
            }
        }

        #endregion

    }
}
