using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace OPPF.Utilities.Caching
{
    // Holds the FetchItem event arguments.

    public class FetchItemEventArgs
    {

        private string _key;
        private object _param;

        public FetchItemEventArgs(string key, object param)
        {
            _key = key;
            _param = param;
        }

        public string Key
        {
            get
            {
                return _key;
            }
        }

        public object Param
        {
            get
            {
                return _param;
            }
        }
    }
}
