using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace OPPF.Utilities.Caching
{
    // Holds the CacheItemExpired event arguments.
    public class CacheItemExpiredEventArgs
    {

        string key;
        object item;

        public CacheItemExpiredEventArgs(string p_key, ref object p_item)
        {
            key = p_key;
            item = p_item;
        }

        public object Item
        {
            get
            {
                return item;
            }
        }

        public string Key
        {
            get
            {
                return key;
            }
        }
    }
}
