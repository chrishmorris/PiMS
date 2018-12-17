using System;

namespace OPPF.Utilities
{
	/// <summary>
	/// Summary description for RobotUtils.
	/// </summary>
	public class RobotUtils
	{
		public RobotUtils()
		{
			//
			// TODO: Add constructor logic here
			//
		}

		/// <summary>
		/// Copy the passed IRobot into the webservice proxy equivalent
		/// </summary>
		/// <param name="robot"></param>
		/// <returns></returns>
		public static OPPF.Proxies.Robot createProxy(Formulatrix.Integrations.ImagerLink.IRobot robot)
		{
			// Copy the passed IRobot into the webservice equivalent
			// TODO - what happens with nulls?
			OPPF.Proxies.Robot wsRobot = new OPPF.Proxies.Robot();
			if (robot != null)
			{
				wsRobot.iD = robot.ID;
				wsRobot.name = robot.Name;
			}
			return wsRobot;
		}

        /// <summary>
		/// Return a stringified version of the specified IRobot
		/// </summary>
		/// <param name="robot">The IRobot to stringify</param>
		/// <returns>A stringified version of the specified IRobot</returns>
		public static string iRobotToString(Formulatrix.Integrations.ImagerLink.IRobot robot)
		{
			return robot.GetType() + "[ID=\"" + robot.ID + "\", Name=\"" + robot.Name + "\"]";
		}

	}
}
