using System;
using Formulatrix.Integrations.ImagerLink;
using log4net;
using log4net.Config;

namespace OPPF.Integrations.ImagerLink
{
	/// <summary>
	/// A unique identifier and display name for a robot.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>IRobot</c>, with setters
	/// as well as the interface-defined getters.
	/// IRobot.ID is max 20 characters
	/// IRobot.Name is max 100 characters
	/// </remarks>
	public class Robot : IRobot
    {

        #region Statics

        /// <summary>
        /// Return a stringified version of the specified IRobot
        /// </summary>
        /// <param name="robot">The IRobot to stringify</param>
        /// <returns>A stringified version of this Robot</returns>
        public static string ToString(IRobot robot)
        {
            return robot.GetType() + "[ID=\"" + robot.ID + "\", Name=\"" + robot.Name + "\"]";
        }

        #endregion

        /// <summary>
		///  Logger for this class
		/// </summary>
		private readonly ILog _log;

        /// <summary>
        /// The ID of the IRobot
        /// </summary>
		private string _iD;

        /// <summary>
        /// The Name of the IRobot
        /// </summary>
		private string _name;

        /// <summary>
		/// Zero-arg constructor, applying default attribute values.
		/// </summary>
		/// <remarks>Strings are set to the empty string.</remarks>
        public Robot() : this("", "")
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
        /// <param name="iD">The ID of the IRobot</param>
        /// <param name="name">The Name of the IRobot</param>
		public Robot(string iD, string name)
		{
            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

			SetID(iD);
			SetName(name);
		}

		/// <summary>
		/// Return a stringified version of this Robot
		/// </summary>
		/// <returns>A stringified version of this Robot</returns>
		public string ToString()
		{
            return Robot.ToString(this);
		}

		#region IRobot Members

        /// <summary>
        /// Log a verbose-level message.
        /// </summary>
        void IRobot.LogVerbose(string msg)
        {
            if (_log.IsDebugEnabled)
            {
                _log.Debug(msg);
            }
        }

        /// <summary>
        /// Log an info-level message.
        /// </summary>
        void IRobot.LogInfo(string msg)
        {
            if (_log.IsInfoEnabled)
            {
                _log.Info(msg);
            }
        }

        /// <summary>
        /// Log a warning-level message.
        /// </summary>
        void IRobot.LogWarning(string msg)
        {
            if (_log.IsWarnEnabled)
            {
                _log.Warn(msg);
            }
        }

        /// <summary>
        /// Log an info-level message.
        /// </summary>
        void IRobot.LogError(string msg)
        {
            if (_log.IsErrorEnabled)
            {
                _log.Error(msg);
            }
        }

        /// <summary>
        /// Log an exception.
        /// </summary>
        void IRobot.LogException(System.Exception e)
        {
            if (_log.IsErrorEnabled)
            {
                _log.Error(e);
            }
        }

        /// <summary>
		/// A unique ID for the <c>IRobot</c>.
		/// </summary>
        string IRobot.ID
		{
			get
			{
				return _iD;
			}
		}

		/// <summary>
		/// A display name of the <c>IRobot</c>.
		/// </summary>
        string IRobot.Name
		{
			get
			{
				return _name;
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

        #endregion

	}
}
