using System;
using System.Net;
using OPPF.Utilities;
using OPPF.Utilities.Caching;
using Formulatrix.Integrations.ImagerLink;
using log4net;
using OPPF.Proxies2;

namespace OPPF.Integrations.ImagerLink
{
    class PlateInfoCacheNew
    {
        private ILog _log;
        private Cache _plateInfoCache;


        public PlateInfoCacheNew(int initialSize, int capacity)
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
        /// Retrieves a plate description from the web service.
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
                string msg = "Called " + this + ".FetchPlateInfo(robot=" + robot.ToString() + ", plateID=\"" + plateID + "\")";
                _log.Debug(msg);
            }

            // Special case for ReliabilityTestPlate
            if ("ReliabilityTestPlate".Equals(plateID))
            {
                OPPF.Integrations.ImagerLink.PlateInfo dummy = new OPPF.Integrations.ImagerLink.PlateInfo();
                dummy.SetDateDispensed(DateTime.Now);
                dummy.SetExperimentName("Dummy Expt Name");
                dummy.SetPlateNumber(1);
                dummy.SetPlateTypeID("1");
                dummy.SetProjectName("Dummy Project Name");
                dummy.SetUserEmail("DummyEmailAddress");
                dummy.SetUserName("Dummy UserName");

                return dummy;
            }

            // Declare the return variable
            OPPF.Integrations.ImagerLink.PlateInfo pi = null;

            try
            {
                // Create and populate the request object
                getPlateInfo request = new getPlateInfo();
                request.robot = OPPF.Utilities.Robot2Utils.createProxy(robot);
                request.plateID = plateID;

                // Make the web service call
                WSPlate wsPlate = WSPlateFactory.getWSPlate2();

                _log.Info("Calling WSPlate.getPlateInfo()");
                getPlateInfoResponse response = wsPlate.getPlateInfo(request);

                // Get the webservice proxy PlateInfo
                OPPF.Proxies2.PlateInfo ppi = response.getPlateInfoReturn;

                // Map it into an IPlateInfo
                pi = new OPPF.Integrations.ImagerLink.PlateInfo();
                pi.SetDateDispensed(ppi.dateDispensed);
                pi.SetExperimentName(ppi.experimentName);
                pi.SetPlateNumber(ppi.plateNumber);
                // TODO Figure out how to get pims to tell us the right answer
                //pi.SetPlateTypeID(ppi.plateTypeID);
                pi.SetPlateTypeID(PlateInfoProviderNew.getIDForName(ppi.plateTypeID));
                pi.SetProjectName(ppi.projectName);
                pi.SetUserEmail(ppi.userEmail);
                pi.SetUserName(ppi.userName);

            }
            catch (Exception e)
            {
                string msg = "WSPlate.getPlateInfo threw " + e.GetType() + ":\n" + e.Message + "\nfor plate \"" + plateID + "\" in robot \"" + robot.Name + "\"\n - probably not in LIMS - not fatal.";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }

                // Log it
                _log.Error(msg, e);

                // Don't rethrow - return null - don't want to stop imaging
            }

            // Return the IPlateInfo
            return pi;

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
