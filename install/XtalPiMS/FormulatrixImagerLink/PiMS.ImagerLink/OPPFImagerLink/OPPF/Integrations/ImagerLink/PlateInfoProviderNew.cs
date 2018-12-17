using System;

using OPPF.Proxies;
using OPPF.Utilities;
using Formulatrix.Integrations.ImagerLink;
using log4net;
using log4net.Config;

namespace OPPF.Integrations.ImagerLink
{
    /// <summary>
    /// Provides plate information.
    /// </summary>
    /// <remarks>Both RockImager and RockImagerProcessor instantiate and use
    /// IPlateInfoProvider. The function of the class is to provide information
    /// about a plate for the user.
    /// Khalid's comments:
    /// The methods of IPlateInfoProvider are required to display information to the
    /// user about the plate. You should provide as much detail of the plate as
    /// possible. If you don't have a field, let me know and we may be able to
    /// figure out a value you can substitute for the user instead: for example, our
    /// other customer simply returns the barcode for GetPlateID().
    /// </remarks>
    public class PlateInfoProviderNew : IPlateInfoProvider
    {

        /// <summary>
        /// 15 min cache lifetime 
        /// </summary>
        private static readonly long _cacheLifetime = 15 * TimeSpan.TicksPerMinute;

        /// <summary>
        /// Cached list of plate types
        /// </summary>
        private static IPlateType[] _plateTypes = null;

        /// <summary>
        /// Expiry date of the cache in ticks
        /// </summary>
        private static long _plateTypesCacheExpires = 0;

        /// <summary>
        /// Lock object for plate types cache
        /// </summary>
        private static System.Object _plateTypesLock = new System.Object();

        public static string getIDForName(string name)
        {
            if (null == _plateTypes)
            {
                // Populate it
                IPlateInfoProvider pipn = new PlateInfoProviderNew();
                pipn.GetPlateTypes(new Robot("x","x"));
            }

            for (int i = 0; i < _plateTypes.Length; i++)
            {
                if (_plateTypes[i].Name.Equals(name)) {
                    return _plateTypes[i].ID;
                }
            }
            throw new Exception("Can't map plate type \"" + name + "\" to id");
        }

        /// <summary>
        ///  Logger
        /// </summary>
        private readonly ILog _log;

        /// <summary>
        /// PlateInfo cache
        /// </summary>
        private readonly PlateInfoCacheNew _plateInfoCache;

        /// <summary>
        /// Zero-arg constructor
        /// </summary>
        public PlateInfoProviderNew()
        {

            // Load configuration
            OPPFConfigXML.Configure();

            // Get Logger
            _log = LogManager.GetLogger(this.GetType());

            // PlateInfoCache
            // TODO: Allow configuration of initialSize and Capacity
            _plateInfoCache = new PlateInfoCacheNew(1000, 1000);

            // Log the call to the constructor
            if (_log.IsDebugEnabled)
            {
                string msg = "Constructed a new " + this;
                _log.Debug(msg);
            }

        }

        #region IPlateInfoProvider Members

        /// <summary>
        /// Retrieves a plate id.
        /// </summary>
        /// <param name="robot">The robot to find the plate type for.</param>
        /// <param name="barcode">The <c>barcode</c> label of the plate.</param>
        /// <returns>The unique identifier describing the plate. Actually, the barcode is the unique identifier, so it just returns the barcode</returns>
        string IPlateInfoProvider.GetPlateID(IRobot robot, string barcode)
        {

            // OPPF PERFORMANCE BODGE - The barcode is the plateID
            return barcode;

        }

        /// <summary>
        /// Retrieves a plate description.
        /// </summary>
        /// <param name="robot">The robot to find the plate type for.</param>
        /// <param name="plateID">The <c>plateID</c> of the plate.</param>
        /// <returns>The <c>IPlateInfo</c> describing the plate.</returns>
        IPlateInfo IPlateInfoProvider.GetPlateInfo(IRobot robot, string plateID)
        {
            return _plateInfoCache.GetPlateInfo(robot, plateID);

            /*
             * Replaced by cache
             * 
            // Check arguments - do it up front to avoid possible inconsistencies later
            if (robot == null) throw new System.NullReferenceException("robot must not be null");
            if (plateID == null) throw new System.NullReferenceException("plateID must not be null");

            // Log the call
            if (_log.IsDebugEnabled)
            {
                string msg = "Called " + this + ".GetPlateInfo(robot=" + robot.ToString() + ", plateID=\"" + plateID + "\")";
                _log.Debug(msg);
            }

            // Special case for ReliabilityTestPlate
            if ("ReliabilityTestPlate".Equals(plateID))
            {
                OPPF.Integrations.ImagerLink.PlateInfo dummy = new OPPF.Integrations.ImagerLink.PlateInfo();
                dummy.DateDispensed = DateTime.Now;
                dummy.ExperimentName = "Dummy Expt Name";
                dummy.PlateNumber = 1;
                dummy.PlateTypeID = "1";
                dummy.ProjectName = "Dummy Project Name";
                dummy.UserEmail = "DummyEmailAddress";
                dummy.UserName = "Dummy UserName";

                return dummy;
            }

            // Declare the return variable
            OPPF.Integrations.ImagerLink.PlateInfo pi = null;

            try
            {
                // Create and populate the request object
                getPlateInfo request = new getPlateInfo();
                request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
                request.plateID = plateID;

                // Make the web service call
                WSPlate wsPlate = new WSPlate();
                getPlateInfoResponse response = wsPlate.getPlateInfo(request);

                // Get the webservice proxy PlateInfo
                OPPF.Proxies.PlateInfo ppi = response.getPlateInfoReturn;

                // Map it into an IPlateInfo
                pi = new OPPF.Integrations.ImagerLink.PlateInfo();
                pi.DateDispensed = ppi.dateDispensed;
                pi.ExperimentName = ppi.experimentName;
                pi.PlateNumber = ppi.plateNumber;
                pi.PlateTypeID = ppi.plateTypeID;
                pi.ProjectName = ppi.projectName;
                pi.UserEmail = ppi.userEmail;
                pi.UserName = ppi.userName;

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
             */
        }

        /// <summary>
        /// Retrieve a plate type. Rewritten to use the cached list of plate types.
        /// </summary>
        /// <param name="robot">The robot to find the plate type for.</param>
        /// <param name="plateTypeID">The ID of the plate type.</param>
        /// <returns>The plate type with ID of plateTypeID, or null if not found.</returns>
        IPlateType IPlateInfoProvider.GetPlateType(IRobot robot, string plateTypeID)
        {

            // Check arguments - do it up front to avoid possible inconsistencies later
            if (robot == null) throw new System.NullReferenceException("robot must not be null");
            if (plateTypeID == null) throw new System.NullReferenceException("plateTypeID must not be null");

            // Log the call
            if (_log.IsDebugEnabled)
            {
                string msg = "Called " + this + ".GetPlateType(robot=" + robot.ToString() + ", plateTypeID=\"" + plateTypeID + "\")";
                _log.Debug(msg);
            }

            IPlateType[] plateTypes = ((IPlateInfoProvider)this).GetPlateTypes(robot);
            if (null != plateTypes)
            {
                for (int i = 0; i < plateTypes.Length; i++)
                {
                    if (plateTypeID.Equals(plateTypes[i].ID))
                    {
                        return plateTypes[i];
                    }
                }

                // Should not get here - log error
                string msg = "Failed to find PlateType with ID: " + plateTypeID + " for robot \"" + robot.Name + "\" from " + plateTypes.Length + " plateTypes - returning null";
                _log.Error(msg);

            }

            else
            {
                // Should not get here - log error
                string msg = "Failed to find PlateType with ID: " + plateTypeID + " for robot \"" + robot.Name + "\" - GetPlateTypes returned null - returning null";
                _log.Error(msg);
            }

            // No better option than to return null
            return null;

        }

        /// <summary>
        /// Retrieve all the plate types. The list of PlateTypes is cached for _cacheLifetime min.
        /// </summary>
        /// <param name="robot">The robot to find the plate types for.</param>
        /// <returns>An array of plate types, or null if there are none.</returns>
        IPlateType[] IPlateInfoProvider.GetPlateTypes(IRobot robot)
        {

            // Check arguments - do it up front to avoid possible inconsistencies later
            if (robot == null) throw new System.NullReferenceException("robot must not be null");

            // Log the call
            if (_log.IsDebugEnabled)
            {
                string msg = "Called " + this + ".GetPlateTypes(robot=" + robot.ToString() + ")";
                _log.Debug(msg);
            }

            // Return cached values if appropriate
            if ((_plateTypes != null) && (System.DateTime.Now.Ticks <= _plateTypesCacheExpires))
            {
                _log.Debug("GetPlateTypes() using cached response");
                return _plateTypes;
            }

            // TODO Use WS for platedb!
            // TODO Figure out how to use WS for pimsdb

            _log.Debug("GetPlateTypes() refreshing cache");

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
          10 | MRC                                  |         12 |       8 |               2 |                 2107 | 441313xxxxxx      |                      1
             */

            // Map into an array of IPlateType[]
            OPPF.Integrations.ImagerLink.PlateType[] iptArray = new OPPF.Integrations.ImagerLink.PlateType[10];
            int i = 0;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("1");
            iptArray[i].SetName("Greiner, central shelf only");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(1);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("2");
            iptArray[i].SetName("OPPF Full Greiner, all three shelves");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(3);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("3");
            iptArray[i].SetName("Fluidigm 1.96");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(1);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("4");
            iptArray[i].SetName("Fluidigm 4.96");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(4);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("5");
            iptArray[i].SetName("Fluidigm 8.96");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(8);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("6");
            iptArray[i].SetName("Fluidigm DC10 1.96");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(2);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("7");
            iptArray[i].SetName("Greiner DC");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(1);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("8");
            iptArray[i].SetName("STRUBI Capillary Plates");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(1);
            iptArray[i].SetNumRows(8);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("9");
            iptArray[i].SetName("Zurich Capillary Plates");
            iptArray[i].SetNumColumns(3);
            iptArray[i].SetNumDrops(19);
            iptArray[i].SetNumRows(16);
            i++;

            iptArray[i] = new OPPF.Integrations.ImagerLink.PlateType();
            iptArray[i].SetID("10");
            iptArray[i].SetName("MRC");
            iptArray[i].SetNumColumns(12);
            iptArray[i].SetNumDrops(2);
            iptArray[i].SetNumRows(8);
            i++;

            // Copy into the cache and update cache expiry
            _plateTypes = iptArray;
            _plateTypesCacheExpires = System.DateTime.Now.Ticks + _cacheLifetime;

            _log.Debug("GetPlateTypes() using fresh response");
            // Return the array of IPlateType[]
            return _plateTypes;

        }

        #endregion

    }

}
