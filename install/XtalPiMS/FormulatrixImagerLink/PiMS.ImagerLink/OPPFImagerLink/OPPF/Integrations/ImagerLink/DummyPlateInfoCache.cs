using System;
using OPPF.Utilities.Caching;
using Formulatrix.Integrations.ImagerLink;
using log4net;
using OPPF.Proxies;
using System.Text.RegularExpressions;

namespace OPPF.Integrations.ImagerLink
{
    class DummyPlateInfoCache
    {
        private ILog _log;
        private Cache _plateInfoCache;


        public DummyPlateInfoCache(int initialSize, int capacity)
        {
            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

            _plateInfoCache = new Cache(initialSize, capacity);
            _plateInfoCache.FetchItem += new FetchItemEventHandler(FetchPlateInfo);

            // Log the call to the constructor
            if (_log.IsDebugEnabled)
            {
                string msg = "Constructed a new " + this;
                _log.Debug(msg);
            }

        }

        /// <summary>
        /// Event handler for _plateInfoCache.FetchItem. Delegates to FetchPlateInfo(IRobot, string).
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        /// <returns></returns>
        public object FetchPlateInfo(object sender, FetchItemEventArgs args)
        {
            return FetchPlateInfo((IRobot)args.Param, args.Key);
        }

        /// <summary>
        /// Retrieves a dummy plate description.
        /// </summary>
        /// <param name="robot">The robot to find the plate type for.</param>
        /// <param name="plateID">The <c>plateID</c> of the plate.</param>
        /// <returns>The <c>IPlateInfo</c> describing the plate.</returns>
        public IPlateInfo FetchPlateInfo(IRobot robot, string plateID)
        {

            // Check arguments - do it up front to avoid possible inconsistencies later
            if (robot == null) throw new System.NullReferenceException("robot must not be null");
            if (plateID == null) throw new System.NullReferenceException("plateID must not be null");

            // Log the call
            if (_log.IsDebugEnabled)
            {
                string msg = "Called " + this + ".DummyFetchPlateInfo(robot=" + robot.ToString() + ", plateID=\"" + plateID + "\")";
                _log.Debug(msg);
            }

            OPPF.Integrations.ImagerLink.PlateInfo dummy = new OPPF.Integrations.ImagerLink.PlateInfo();
            dummy.SetDateDispensed(DateTime.Now);
            dummy.SetExperimentName("Dummy Expt for " + plateID);

            Regex numberregex = new Regex("^\\d+$");
            if (numberregex.IsMatch(plateID))
            {
                int start = plateID.Length - 8;
                if (start < 0)
                {
                    start = 0;
                }
                dummy.SetPlateNumber(Convert.ToInt32(plateID.Substring(start)));
            }
            else
            {
                dummy.SetPlateNumber(1);
            }

            /*
 platetypeid |             strplatetype             | intcolumns | introws | intsubpositions | intsubpositionheight | strbarcodepattern | intdefaultscheduleplan
-------------+--------------------------------------+------------+---------+-----------------+----------------------+-------------------+------------------------
           1 | Greiner, central shelf only          |         12 |       8 |               1 |                 2107 | 4413xxxxxxxx      |                      1
           2 | OPPF Full Greiner, all three shelves |         12 |       8 |               3 |                 2107 | 3313xxxxxxxx      |                      1
           3 | Fluidigm 1.96                        |         12 |       8 |               1 |                  100 | 109xxxxxxx        |                      2
           4 | Fluidigm 4.96                        |         12 |       8 |               4 |                  100 | 110xxxxxxx        |                      2
           5 | Fluidigm 8.96                        |         12 |       8 |               8 |                  100 | 112xxxxxxx        |                      2
           6 | Fluidigm DC10 1.96                   |         12 |       8 |               1 |                  100 | 122xxxxxxx        |                      2
           7 | Greiner DC                           |         12 |       8 |               1 |                 2107 | 441310xxxxxx      |                      1
           8 | STRUBI Capillary Plates              |         12 |       8 |               1 |                  100 | 441311xxxxxx      |                      1
           9 | Zurich Capillary Plates              |          3 |      16 |              19 |                  100 | 441312xxxxxx      |                      2
             */
            if (plateID.StartsWith("109"))
            {
                dummy.SetPlateTypeID("3");
            }
            else if (plateID.StartsWith("110"))
            {
                dummy.SetPlateTypeID("4");
            }
            else if (plateID.StartsWith("112"))
            {
                dummy.SetPlateTypeID("5");
            }
            else if (plateID.StartsWith("122"))
            {
                dummy.SetPlateTypeID("6");
            }
            else if (plateID.StartsWith("441310"))
            {
                dummy.SetPlateTypeID("7");
            }
            else if (plateID.StartsWith("441311"))
            {
                dummy.SetPlateTypeID("8");
            }
            else if (plateID.StartsWith("441312"))
            {
                dummy.SetPlateTypeID("9");
            }
            else
            {
                dummy.SetPlateTypeID("1");
            }

            dummy.SetProjectName("Dummy Project for " + plateID);
            dummy.SetUserEmail("DummyEmailAddress");
            dummy.SetUserName("Dummy UserName");

            // Return the IPlateInfo
            return dummy;

        }

        /// <summary>
        /// Retrieves a plate description.
        /// </summary>
        /// <param name="robot">The robot to find the plate type for.</param>
        /// <param name="plateID">The <c>plateID</c> of the plate.</param>
        /// <returns>The <c>IPlateInfo</c> describing the plate.</returns>
        public IPlateInfo GetPlateInfo(IRobot robot, string plateID)
        {
            return (IPlateInfo)_plateInfoCache.Fetch(plateID, robot);
        }
    }
}
