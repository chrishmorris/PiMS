using System;

using Formulatrix.Integrations.ImagerLink;

namespace OPPF.Integrations.ImagerLink
{
	/// <summary>
	/// A collection of values describing a plate.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>IPlateInfo</c>, with setters
	/// as well as the interface-defined getters.</remarks>
	public class PlateInfo : IPlateInfo
	{

		private DateTime _dateDispensed;
		private string _userEmail;
		private string _plateTypeID;
		private int _plateNumber;
		private string _userName;
		private string _experimentName;
		private string _projectName;

		/// <summary>
		/// Zero-arg constructor, applying default attribute values.
		/// </summary>
		/// <remarks>Numbers are set to zero, strings to the empty string.</remarks>
		public PlateInfo() : this(new DateTime(), "", "", 0, "", "", "")
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
		public PlateInfo(DateTime dateDispensed, string userEmail, string plateTypeID, int plateNumber, string userName, string experimentName, string projectName)
		{
			SetDateDispensed(dateDispensed);
			SetUserEmail(userEmail);
			SetPlateTypeID(plateTypeID);
			SetPlateNumber(plateNumber);
			SetUserName(userName);
			SetExperimentName(experimentName);
			SetProjectName(projectName);
		}

		#region IPlateInfo Members

		/// <summary>
		/// Gets/sets the Date/Time the plate was dispensed.
        /// Note that the DateTime is converted to universal time before storing but the value
        /// returned is in the local timezone.
        /// </summary>
        DateTime IPlateInfo.DateDispensed
		{
			get
			{
                // .NET < 2.0
                //return dateDispensed;
                // .NET >= 2.0
                return _dateDispensed.ToLocalTime();
            }
		}

		/// <summary>
		/// Gets/sets the email of the user running the experiment.
		/// </summary>
        string IPlateInfo.UserEmail
		{
			get
			{
				return _userEmail;
			}
		}

		/// <summary>
		/// Gets/sets a unique ID describing the plate type.
		/// </summary>
        string IPlateInfo.PlateTypeID
		{
			get
			{
				return _plateTypeID;
			}
		}

		/// <summary>
		/// Gets/sets a unique ID for the plate.
		/// </summary>
        int IPlateInfo.PlateNumber
		{
			get
			{
				return _plateNumber;
			}
		}

		/// <summary>
		/// Gets/sets the name of the user running the experiment.
		/// </summary>
        string IPlateInfo.UserName
		{
			get
			{
				return _userName;
			}
		}

		/// <summary>
		/// Gets/sets the name of the experiment.
		/// </summary>
        string IPlateInfo.ExperimentName
		{
			get
			{
				return _experimentName;
			}
		}

		/// <summary>
		/// Gets/sets the name of the project.
		/// </summary>
        string IPlateInfo.ProjectName
		{
			get
			{
				return _projectName;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetDateDispensed(DateTime dateDispensed)
        {
            _dateDispensed = dateDispensed.ToUniversalTime();
        }

        public void SetUserEmail(string userEmail)
        {
            _userEmail = userEmail;
        }

        public void SetPlateTypeID(string plateTypeID)
        {
            _plateTypeID = plateTypeID;
        }

        public void SetPlateNumber(int plateNumber)
        {
            _plateNumber = plateNumber;
        }

        public void SetUserName(string userName)
        {
            _userName = userName;
        }

        public void SetExperimentName(string experimentName)
        {
            _experimentName = experimentName;
        }

        public void SetProjectName(string projectName)
        {
            _projectName = projectName;
        }

        #endregion

    }
}
