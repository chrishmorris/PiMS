using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using OPPF.Proxies2;

namespace OPPF.Utilities
{
    public class WSPlateFactory
    {
        private static WSPlate wsPlate2;

        public static WSPlate getWSPlate2()
        {
            if (null == wsPlate2)
            {
                ServicePointManager.Expect100Continue = false;
                wsPlate2 = new WSPlate();
                wsPlate2.Url = OPPFConfigXML.GetWsPlateEndpoint();
                wsPlate2.Credentials = new NetworkCredential(OPPFConfigXML.GetUsername(), OPPFConfigXML.GetPassword());
            }
            return wsPlate2;
        }
    }
}
