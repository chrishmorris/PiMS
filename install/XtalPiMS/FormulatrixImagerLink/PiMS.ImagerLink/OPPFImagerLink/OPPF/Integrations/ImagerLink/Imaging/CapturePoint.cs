/*
 * CapturePoint.cs - implementation of Formulatrix.Integrations.ImagerLink.Imaging.ICapturePoint.
 * Copyright 2010 Jonathan Diprose <jon@strubi.ox.ac.uk>
 */

using Formulatrix.Integrations.ImagerLink.Imaging;

namespace OPPF.Integrations.ImagerLink.Imaging
{
	/// <summary>
	/// Specifies a location and a set of images to capture.
	/// </summary>
	/// <remarks>
	/// This is a basic bean implementation of <c>ICapturePoint</c>, with setters
	/// as well as the interface-defined getters.
	/// <c>ICapturePoint</c> locations are relative to the top-left plate corner by <c>WellNumber</c> 1.
	/// 
	/// <c>WellNumber</c> definition:
	/// <code>
	///  |-X-----------------
	///  Y 0,0
	///  |       1  2  3  4
	///  |     /------------
	///  |    /             |
	///  |  A |  1  2  3  4 |
	///  |    |             |
	///  |  B |  5  6  7  8 |
	///
	/// 1, 2, 3, 4 are columns. A, B, C are rows.
	///
	/// WellNumber = starts at one and proceeds along rows and columns
	/// X = distance along the columns
	/// Y = distance along the rows
	/// Z = distance down into the plate
	/// Width = size in the X direction
	/// Height = size in the Y direction
	/// </code>
	/// 
	/// All X,Y locations are relative to center of the parent region.
	/// Z is relative to the trained starting point for the drop.
	/// 
	/// In the case where there is no parent region are relative
	/// to the trained center point on the imager.
	/// 
	/// All units are in micrometers.
	/// </remarks>
	public class CapturePoint : ICapturePoint
	{

        private ICaptureProfile[] _captureProfiles;
        private int _dropNumber;
        private Formulatrix.Integrations.ImagerLink.Imaging.ImageRect _location;
        private ICapturePoint _parent;
        private string _regionID;
        private Formulatrix.Integrations.ImagerLink.Imaging.RegionType _regionType;
        private int _wellNumber;


        /// <summary>
        /// Zero-arg constructor
        /// </summary>
        /// <remarks>Center is set to the default Point, IsAbsolute to false, RegionType
        /// to RegionType.Overview, RegionID to the empty string, CaptureProfiles to null,
        /// Size to the default Size, DropNumber to zero, WellNumber to Zero and Parent
        /// to null.</remarks>
        public CapturePoint()
        {
            SetCaptureProfiles(null);
            SetDropNumber(0);
            SetLocation(null);
            SetParent(null);
            SetRegionID("");
            SetRegionType(RegionType.Overview);
            SetWellNumber(0);
        }

        public CapturePoint(int wellNumber, int dropNumber, ICaptureProfile[] captureProfiles, ImageRect location, ICapturePoint parent, string regionID, RegionType regionType)
        {
            SetWellNumber(wellNumber);
            SetDropNumber(dropNumber);
            SetCaptureProfiles(captureProfiles);
            SetLocation(location);
            SetParent(parent);
            SetRegionID(regionID);
            SetRegionType(regionType);
        }

        #region ICapturePoint Members

        /// <summary>
        /// The list of <c>ICaptureProfiles</c> to image for the <c>IRegion</c>,
        /// otherwise <c>null</c> if the default capture profile should be used.
        /// If the list is zero length than no images are taken.
        /// </summary>
        ICaptureProfile[] ICapturePoint.CaptureProfiles
        {
            get
            {
                return _captureProfiles;
            }
        }

        /// <summary>
        /// Gets/sets the number of the drop within the well to which this <c>IRegion</c> is assigned.
        /// </summary>
        int ICapturePoint.DropNumber
        {
            get
            {
                return _dropNumber;
            }
        }

        /// <summary>
        /// Gets the center ImageRect specifying the center point and surrounding size of this ICapturePoint.
        /// </summary>
        /// <remarks>
        /// The Location X,Y is relative to the ICapturePoint.Parent's ICapturePoint, or if the parent is
        /// null to the trained drop location.
        /// 
        /// For RegionType.Overview the Location is ignored and the image should be captured at the trained
        /// location.
        /// 
        /// For RegionType.Drop the Location is required if the plate type is configured for automatic drop
        /// location, and that automatic drop location should not be run for this ICapturePoint.
        /// 
        /// For RegionType.RegionOfInterest the Location is required, or the region will not be captured.
        /// </remarks>
        ImageRect ICapturePoint.Location
        {
            get
            {
                return _location;
            }
        }

        /// <summary>
        /// The parent <c>ICapturePoint</c> to which this point is relative to, otherwise <c>null</c>.
        /// </summary>
        ICapturePoint ICapturePoint.Parent
        {
            get
            {
                return _parent;
            }
        }

        /// <summary>
        /// Gets/sets the unique identifier for this <c>IRegion</c>.
        /// </summary>
        string ICapturePoint.RegionID
        {
            get
            {
                return _regionID;
            }
        }

        /// <summary>
		/// Gets/sets the type of this <c>ICapturePoint</c>.
		/// </summary>
        RegionType ICapturePoint.RegionType
		{
			get
			{
				return _regionType;
			}
		}

		/// <summary>
		/// Gets/sets the number of the well to which this <c>IRegion</c> is assigned.
		/// </summary>
        int ICapturePoint.WellNumber
		{
			get
			{
				return _wellNumber;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetCaptureProfiles(ICaptureProfile[] captureProfiles)
        {
            _captureProfiles = captureProfiles;
        }

        public void SetDropNumber(int dropNumber)
        {
            _dropNumber = dropNumber;
        }

        public void SetLocation(ImageRect location)
        {
            _location = location;
        }

        public void SetParent(ICapturePoint parent)
        {
            _parent = parent;
        }

        public void SetRegionID(string regionID)
        {
            _regionID = regionID;
        }

        public void SetRegionType(RegionType regionType)
        {
            _regionType = regionType;
        }

        public void SetWellNumber(int wellNumber)
        {
            _wellNumber = wellNumber;
        }

        #endregion

    }
}
