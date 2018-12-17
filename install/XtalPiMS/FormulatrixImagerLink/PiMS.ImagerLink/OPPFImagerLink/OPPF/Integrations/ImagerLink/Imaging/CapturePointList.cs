/*
 * CapturePointList.cs - implementation of Formulatrix.Integrations.ImagerLink.Imaging.ICapturePointList.
 * Copyright 2010 Jonathan Diprose <jon@strubi.ox.ac.uk>
 */

using Formulatrix.Integrations.ImagerLink.Imaging;
using log4net;

namespace OPPF.Integrations.ImagerLink.Imaging
{
	/// <summary>
	/// A list of <c>ICapturePoint</c>s, with an optional <c>ICaptureProfile</c> for this imaging.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>ICapturePointList</c>, with setters
	/// as well as the interface-defined getters.</remarks>
	public class CapturePointList : ICapturePointList
	{
        /// <summary>
        /// The logger.
        /// </summary>
        private readonly ILog _log;

        private ICaptureProfile _plateCaptureProfile;
		private ICapturePoint[] _capturePoints;
		private ICaptureProfile _dropLocationProfile;

		/// <summary>
		/// Zero-arg constructor
		/// </summary>
		/// <remarks>PlateCaptureProfile, CapturePoints and DropLocationProfile are
		/// all set to null.</remarks>
		public CapturePointList()
		{
			SetPlateCaptureProfile(null);
			SetCapturePoints(null);
			SetDropLocationProfile(null);

            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

            // Log the call to the constructor
            _log.Debug("Constructed a new " + this);

        }

		#region ICapturePointList Members

		/// <summary>
		/// The <c>ICaptureProfile</c> associated with the whole plate, otherwise <c>null</c>.
		/// </summary>
        ICaptureProfile ICapturePointList.PlateCaptureProfile
		{
			get
			{
                if (_log.IsDebugEnabled)
                {
                    if (null == _plateCaptureProfile)
                    {
                        _log.Debug("Called ICapturePointList.PlateCaptureProfile - returning null");
                    }
                    else
                    {
                        _log.Debug("Called ICapturePointList.PlateCaptureProfile - returning " + _plateCaptureProfile);
                    }
                }
				return _plateCaptureProfile;
			}
		}

		/// <summary>
		/// The <c>ICapturePoint</c> list for the plate.
		/// </summary>
        ICapturePoint[] ICapturePointList.CapturePoints
		{
			get
			{
                if (_log.IsDebugEnabled)
                {
                    if (null == _capturePoints)
                    {
                        _log.Debug("Called ICapturePointList.CapturePoints - returning null");
                    }
                    else
                    {
                        _log.Debug("Called ICapturePointList.CapturePoints - returning " + _capturePoints);
                    }
                }
                return _capturePoints;
			}
		}

		/// <summary>
		/// The <c>ICaptureProfile</c> associated with drop location, otherwise <c>null</c>.
		/// </summary>
        ICaptureProfile ICapturePointList.DropLocationProfile
		{
			get
			{
                if (_log.IsDebugEnabled)
                {
                    if (null == _dropLocationProfile)
                    {
                        _log.Debug("Called ICapturePointList.DropLocationProfile - returning null");
                    }
                    else
                    {
                        _log.Debug("Called ICapturePointList.DropLocationProfile - returning " + _dropLocationProfile);
                    }
                }
                return _dropLocationProfile;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetPlateCaptureProfile(ICaptureProfile plateCaptureProfile)
        {
            _plateCaptureProfile = plateCaptureProfile;
        }

        public void SetCapturePoints(ICapturePoint[] capturePoints)
        {
            _capturePoints = capturePoints;
        }

        public void SetDropLocationProfile(ICaptureProfile dropLocationProfile)
        {
            _dropLocationProfile = dropLocationProfile;
        }

        #endregion
    }
}
