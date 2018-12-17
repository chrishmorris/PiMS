using System;

namespace OPPF.Utilities
{
	/// <summary>
	/// Summary description for RobotUtils.
	/// </summary>
	public class Robot2Utils
	{
		public Robot2Utils()
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
		public static OPPF.Proxies2.Robot createProxy(Formulatrix.Integrations.ImagerLink.IRobot robot)
		{
			// Copy the passed IRobot into the webservice equivalent
			// TODO - what happens with nulls?
			OPPF.Proxies2.Robot wsRobot = new OPPF.Proxies2.Robot();
			if (null == robot)
			{
                throw new Exception("robot cannot be null");
			}
            if ((null == robot.Name) || ("".Equals(robot.Name)))
            {
                throw new Exception("robot.Name cannot be null or empty");
            }

            if ((null == robot.ID) || ("".Equals(robot.ID)))
            {
                wsRobot.iD = robot.Name;
            }
            else
            {
                wsRobot.iD = robot.ID;
            }
            wsRobot.name = robot.Name;
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
