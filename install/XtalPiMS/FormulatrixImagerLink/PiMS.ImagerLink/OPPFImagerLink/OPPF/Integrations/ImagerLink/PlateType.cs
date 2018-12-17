using System;

using Formulatrix.Integrations.ImagerLink;

namespace OPPF.Integrations.ImagerLink
{
	/// <summary>
	/// A collection of values describing a plate type.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>IPlateType</c>, with setters
	/// as well as the interface-defined getters.
	/// IPlateType.ID is max 20 characters
	/// IPlateType.Name is max 100 characters
	/// </remarks>
	public class PlateType : IPlateType
	{

		private int _numRows;
		private int _numDrops;
		private int _numColumns;
		private string _iD;
		private string _name;

        /// <summary>
        /// Convert WellNumber to standard row/col reference, eg 1 => A01.
        /// </summary>
        /// <param name="plateType">The plateType to use for the conversion</param>
        /// <param name="wellNumber">The wellNumber to convert</param>
        /// <returns>The string representation of wellNumber for the specified plateType</returns>
        /// <remarks>
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
        /// TODO: Handle larger plates
        /// </remarks>
        public static string WellNumberToString(IPlateType plateType, int wellNumber)
        {
            if (null == plateType)
            {
                throw new NullReferenceException("plateType must not be null");
            }

            if ((1 > wellNumber) || (wellNumber > (plateType.NumRows * plateType.NumColumns)))
            {
                throw new InvalidOperationException("Invalid wellNumber: " + wellNumber + " for " + plateType.NumColumns + " x " + plateType.NumRows + " plate");
            }

            if (26 < plateType.NumRows)
            {
                throw new NotImplementedException("Cannot handle plates with more the 26 rows");
            }

            int zeroBasedWellNumber = wellNumber - 1;

            // Zero-based row (ie 0 = A, 1 = B, etc.)
            int row = (int) (zeroBasedWellNumber / plateType.NumColumns);

            // Zero-based col
            int col = zeroBasedWellNumber % plateType.NumColumns;

            // Build string
            return String.Format("{0}{1:D2}", (char)(row + 65), col + 1); 
        }

		/// <summary>
		/// Zero-arg constructor, applying default attribute values.
		/// </summary>
		/// <remarks>Numbers are set to zero, strings to the empty string.</remarks>
		public PlateType() : this("", "", 0, 0, 0)
		{
            /*
             * Do nothing here!
             * C# docs imply code here will be executed after
             * the code in the Robot(string, string) constructor
             */
        }

		/// <summary>
        /// Full arg constructor, applying specified values.
        /// </summary>
		/// <param name="iD">The ID of the PlateType</param>
		/// <param name="name">The Name of the PlateType</param>
        /// <param name="numColumns">The NumColumns of the PlateType</param>
        /// <param name="numRows">The NumRows of the PlateType</param>
        /// <param name="numDrops">The NumDrops of the PlateType</param>
        public PlateType(string iD, string name, int numColumns, int numRows, int numDrops)
		{
            SetID(iD);
            SetName(name);
            SetNumColumns(numColumns);
            SetNumRows(numRows);
			SetNumDrops(numDrops);
		}

		#region IPlateType Members

        /// <summary>
        /// Gets/sets a value uniquely identifying this <c>IPlateType</c>.
        /// </summary>
        string IPlateType.ID
        {
            get
            {
                return _iD;
            }
        }

        /// <summary>
        /// Gets/sets a user displayable name for this <c>IPlateType</c>.
        /// </summary>
        string IPlateType.Name
        {
            get
            {
                return _name;
            }
        }

        /// <summary>
        /// Gets/sets the number of columns for this <c>IPlateType</c>.
        /// </summary>
        int IPlateType.NumColumns
        {
            get
            {
                return _numColumns;
            }
        }

        /// <summary>
		/// Gets/sets the number of rows for this <c>IPlateType</c>.
		/// </summary>
        int IPlateType.NumRows
		{
			get
			{
				return _numRows;
			}
		}

		/// <summary>
		/// Gets/sets the number of drops per well in this <c>IPlateType</c>.
		/// </summary>
        int IPlateType.NumDrops
		{
			get
			{
				return _numDrops;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetID(string iD)
        {
            _iD = iD;
        }

        public void SetName(string name)
        {
            _name = name;
        }

        public void SetNumColumns(int numColumns)
        {
            _numColumns = numColumns;
        }

        public void SetNumRows(int numRows)
        {
            _numRows = numRows;
        }

        public void SetNumDrops(int numDrops)
        {
            _numDrops = numDrops;
        }

        #endregion

    }
}
