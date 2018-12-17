using System;
using System.Collections.Generic;
using System.Collections.Specialized;

namespace OPPF.Utilities.Caching
{
    class CacheBacker : NameObjectCollectionBase
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="initialCapacity"></param>
        public CacheBacker(int initialCapacity)
            : base(initialCapacity)
        {
        }

        /// <summary>
        /// Removes the oldest item from the cache.
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public object Remove(out string key)
        {
            object toReturn;

            if (this.Count < 1)
            {
                // Nothing to remove.
                key = null;
                return null;
            }

            // Get the oldest cache item.
            toReturn = this.BaseGet(0);

            //Get the oldest item key.
            key = this.BaseGetKey(0);

            // Remove the oldest item.
            this.BaseRemoveAt(0);

            return toReturn;
        }

        /// <summary>
        /// Indexer to get a cached item.
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public object this[string key]
        {
            get
            {
                return ResetItem(key);
            }
        }

        /// <summary>
        /// Add a cache item.
        /// </summary>
        /// <param name="key"></param>
        /// <param name="item"></param>
        public void Add(string key, ref object item)
        {
            // The cache item will automatically be
            // added to the end of the underlying ArrayList.
            this.BaseAdd(key, item);
        }

        /// <summary>
        /// Retrieves a cached item from the NameObjectCollectionBase
        /// and returns it. Also, the retrieved item is removed and
        /// then added again to ensure its age is reset.
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        object ResetItem(string key)
        {
            object tempItem;

            tempItem = this.BaseGet(key);

            // If the retrieved item is null,
            // it isn't reset.
            if (tempItem != null)
            {
                this.BaseRemove(key);
                this.BaseAdd(key, tempItem);
            }

            return tempItem;
        }

        /// <summary>
        /// Clears the entire contents of the cache.
        /// </summary>
        public void Clear()
        {
            this.BaseClear();
        }
    }
}
