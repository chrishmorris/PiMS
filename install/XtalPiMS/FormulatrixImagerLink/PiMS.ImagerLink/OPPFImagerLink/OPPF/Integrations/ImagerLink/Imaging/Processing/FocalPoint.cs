using System;

using Formulatrix.Integrations.ImagerLink.Imaging.Processing;

namespace OPPF.Integrations.ImagerLink.Imaging.Processing
{
	/// <summary>
    /// A Z and a relative focus value.
	/// </summary>
	public class FocalPoint : IFocalPoint
	{
		private int _focus;
		private double _z;

		public FocalPoint()
		{
			SetFocus(0);
			SetZ(0D);
		}

		#region IFocalPoint Members

        /// <summary>
        /// The relative computed focus at the Z height. Higher values are more in focus.
        /// </summary>
        int IFocalPoint.Focus
		{
			get
			{
				return _focus;
			}
		}

        /// <summary>
        /// The height above the trained Z=0 in micrometers where the focus was computed.
        /// </summary>
        double IFocalPoint.Z
		{
			get
			{
				return _z;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetFocus(int focus)
        {
            _focus = focus;
        }

        public void SetZ(double z)
        {
            _z = z;
        }

        #endregion

        public static string ToString(IFocalPoint focalPoint)
        {
            return "[Focus=" + focalPoint.Focus.ToString() + ";Z=" + focalPoint.Z.ToString() + "]";
        }
	}
}
