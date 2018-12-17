/*
 * CaptureProvider.cs - implementation of Formulatrix.Integrations.ImagerLink.Imaging.ICaptureProvider.
 * Copyright 2010 Jonathan Diprose <jon@strubi.ox.ac.uk>
 */

using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Imaging;
using log4net;

namespace OPPF.Integrations.ImagerLink.Imaging
{
	/// <summary>
	/// Returns information about imaging captures.
	/// </summary>
	/// <remarks>
	/// Khalid's comments:
	/// The ICaptureProvider must be implemented, but can be rendered relatively
	/// inert by  the methods returning null. For example, if you don't specify the
	/// list of drops to image by returning an ICapturePointList, RockImager will
	/// simply use the IPlateInfoProvider to lookup the plate definition and image
	/// all drops in the plate.
	/// </remarks>
	public class CaptureProvider : ICaptureProvider
	{

        /// <summary>
        /// The logger.
        /// </summary>
        private readonly ILog _log;

        /// <summary>
        /// The current set of capture profiles for imaging with selectable capture profile feature.
        /// </summary>
        //private System.Collections.Generic.List<IProperty> _captureProfiles;

        public CaptureProvider()
		{

            // Load configuration
            //OPPFConfigXML.Configure();

            // No selectable capture profiles
            // TODO: Implement
            //SetCaptureProfiles(null);

            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

            // Log the call to the constructor
            _log.Debug("Constructed a new " + this);

        }
	
		#region ICaptureProvider Members

		/// <summary>
		/// Gets an <c>ICapturePointList</c> to image for this plateID on the specified <c>imagingID</c>.
		/// </summary>
        /// <param name="robot"></param>
        /// <param name="plateID"></param>
        /// <param name="imagingID">The imaging ID started for this run.</param>
        /// <param name="includeOverview"></param>
        /// <returns>The list of <c>ICapturePoint</c>s, with an optional <c>ICaptureProfile</c> for this imaging.</returns>
        ICapturePointList ICaptureProvider.GetCapturePoints(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, string imagingID, bool includeOverview)
        {
            // TODO:  Add CaptureProvider.GetCapturePoints implementation
            _log.Debug("Called 4-arg ICaptureProvider.GetCapturePoints() - delegating to 5-arg version with userSelectionCaptureProfileID = 0");
            return ((ICaptureProvider) this).GetCapturePoints(robot, plateID, imagingID, includeOverview, 0);
        }

        /*
         * Added in Formulatrix.Integrations.ImagerLink.dll v2.0.0.73 (RockImager v2.0.3.18),
         * which is supposed to be more recent than v2.0.1.136 (RockImager v2.0.1.136).
         */
        /// <summary>
        /// Gets an <c>ICapturePointList</c> to image for this plateID on the specified <c>imagingID</c>.
        /// </summary>
        /// <param name="robot"></param>
        /// <param name="plateID"></param>
        /// <param name="imagingID">The imaging ID started for this run.</param>
        /// <param name="includeOverview"></param>
        /// <param name="userSelectionCaptureProfileID"></param>
        /// <returns></returns>
        /// <remarks>
        /// Added in Formulatrix.Integrations.ImagerLink.dll v2.0.0.73 (RockImager v2.0.3.18),
        /// which is supposed to be more recent than v2.0.1.136 (RockImager v2.0.1.136).
        /// </remarks>
        ICapturePointList ICaptureProvider.GetCapturePoints(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, string imagingID, bool includeOverview, int userSelectionCaptureProfileID)
        {
            // TODO:  Add CaptureProvider.GetCapturePoints implementation
            //return null;

            if (_log.IsInfoEnabled)
            {
                _log.Info("Called ICaptureProvider.GetCapturePoints(" + Robot.ToString(robot) + ", \"" + plateID + "\", \"" + imagingID + "\", " + includeOverview + ", " + userSelectionCaptureProfileID + ")");
            }

            CapturePointList capturePointList = null;

            // OPPF Vis
            if (1 == userSelectionCaptureProfileID)
            {
                _log.Info("Returning empty ICapturePointList");
                capturePointList = new CapturePointList();
            }

            // OPPF UV
            else if (2 == userSelectionCaptureProfileID)
            {
                _log.Info("Returning ICapturePointList with PlateCaptureProfile and CapturePoints set to an ICaptureProfile with one IProperty of \"LightPath\" = \"1\"");

                // Create the property array containing the UV LightPath property 
                IProperty[] properties = new OPPF.Integrations.ImagerLink.Property[1];
                properties[0] = new OPPF.Integrations.ImagerLink.Property("LightPath", "1");

                // Create a new CaptureProfile and apply the UV LightPath property
                CaptureProfile captureProfile = new CaptureProfile();
                captureProfile.SetProfileID("2");
                captureProfile.SetProperties(properties);

                // Create the array of capture profiles to be used for each drop - in this case only one
                CaptureProfile[] captureProfiles = new CaptureProfile[1];
                captureProfiles[0] = captureProfile;

                // Create a CapturePoint for each drop using RockImager's default drop position and the UV LightPath 
                // TODO: Get number of wells and drops from plate type
                int wells = 96;
                CapturePoint[] capturePoints = new CapturePoint[wells];
                for (int i = 0; i < wells; i++)
                {
                    // TODO: Loop over drops - currently assumes one drop per well
                    capturePoints[i] = new CapturePoint(i + 1, 1, captureProfiles, null, null, "1", RegionType.Drop);
                }

                // Create the CapturePointList and set the PlateCaptureProfile and CapturePoints
                // NB I don't think RockImager actually uses the PlateCaptureProfile
                capturePointList = new CapturePointList();
                capturePointList.SetPlateCaptureProfile(captureProfile);
                capturePointList.SetCapturePoints(capturePoints);
            }

            return capturePointList;
        }

        /// <summary>
		/// Gets the capture profile for the plate under the microscope.
		/// </summary>
		/// <param name="plateID">The plateID to retrieve the capture profile for.</param>
		/// <returns>The default <c>ICaptureProfile</c> for this plateID, or <c>null</c> if the default imager configuration should be used.</returns>
        ICaptureProfile ICaptureProvider.GetDefaultCaptureProfile(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID)
		{
			// TODO:  Add CaptureProvider.GetDefaultCaptureProfile implementation
            if (_log.IsInfoEnabled)
            {
                _log.Info("Called ICaptureProvider.GetDefaultCaptureProfile(" + Robot.ToString(robot) + ", \"" + plateID + "\") - returning null");
            }
            return null;
		}

		/// <summary>
		/// Gets the first drop to move the plate to under the microscope.
		/// </summary>
		/// <param name="plateID">The plateID uniquely identifying the plate.</param>
		/// <param name="wellNumber">The first well number of the plate. The first well is <c>1</c>.</param>
		/// <param name="dropNumber">The first drop number in the first well. The first drop is <c>1</c>.</param>
		/// <param name="robot">The robot which the plate is under.</param>
        void ICaptureProvider.GetFirstDrop(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, ref int wellNumber, ref int dropNumber)
		{
			// TODO:  Add CaptureProvider.GetFirstDrop implementation
			wellNumber = 1;
			dropNumber = 1;
		}

        /*
         * Added in Formulatrix.Integrations.ImagerLink.dll v2.0.0.73 (RockImager v2.0.3.18),
         * which is supposed to be more recent than v2.0.1.136 (RockImager v2.0.1.136), and
         * subsequently removed to the ICaptureProfiles interface in v2.0.2.24 (RockImager
         * v2.0.3.20).
         * 
        /// <summary>
        /// Return the list of CaptureProfiles?
        /// </summary>
        /// <remarks>
        /// V2.0.3.20 docs suggest:
        /// Collect the current set of capture profiles for imaging with selectable capture profile feature.
        /// Return an empty list if you only need to do imaging with default capture profile.
        /// </remarks>
        System.Collections.Generic.List<IProperty> ICaptureProvider.CaptureProfiles
        {
            get
            {
                return _captureProfiles;
            }
        }
         * 
         */

        #endregion

        #region Set methods for interface properties

        /*
         * Added for the CaptureProfiles ICaptureProfile property that was subsequently relocated.
         * 
        /// <summary>
        /// Set the current set of capture profiles for imaging with selectable capture profile feature.
        /// </summary>
        /// <param name="captureProfiles"></param>
        public void SetCaptureProfiles(System.Collections.Generic.List<IProperty> captureProfiles)
        {
            if (null == captureProfiles)
            {
                _captureProfiles = new System.Collections.Generic.List<IProperty>(0);
            }
            else
            {
                _captureProfiles = captureProfiles;
            }
        }
        */

        #endregion

    }
}
