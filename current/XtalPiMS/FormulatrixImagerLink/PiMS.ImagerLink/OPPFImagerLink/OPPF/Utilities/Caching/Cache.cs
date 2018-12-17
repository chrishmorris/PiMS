using System.Threading;

namespace OPPF.Utilities.Caching
{
    /// <summary>
    /// This delegate will be used as a definition for the event
    /// to notify the caller that an item has expired.
    /// </summary>
    /// <param name="source"></param>
    /// <param name="e"></param>
    public delegate void CacheItemExpiredEventHandler(object source, CacheItemExpiredEventArgs e);

    /// <summary>
    /// This delegate will be used as a definition to get an 
    /// item if it does not exist in the cache.
    /// </summary>
    /// <param name="source"></param>
    /// <param name="e"></param>
    /// <returns></returns>
    public delegate object FetchItemEventHandler(object source, FetchItemEventArgs e);

    /// <summary>
    /// Cache class. All the members of this class is thread safe.
    /// </summary>
    public class Cache
    {

        /// <summary>
        /// Notifies the user that a cache item has expired.
        /// </summary>
        public event CacheItemExpiredEventHandler CacheItemExpired;

        /// <summary>
        /// Gets an item to cache if it doesn't exist in the cache.
        /// </summary>
        public event FetchItemEventHandler FetchItem;

        /// <summary>
        /// Holds the instance of the CacheBacker container.
        /// </summary>
        private CacheBacker _cacheBacker;

        /// <summary>
        /// The maximum size of the cache.
        /// </summary>
        private int _capacity;

        /// <summary>
        /// Number of cache hits since the last call to ResetStatistics
        /// </summary>
        private long _hits = 0;

        /// <summary>
        /// Number of cache misses since the last call to ResetStatistics
        /// </summary>
        private long _misses = 0;

        /// <summary>
        /// A ReaderWriterLock to synchronize access to the CacheBacker.
        /// </summary>
        private ReaderWriterLock _cacheBackerLock;

        /// <summary>
        /// Construct a new Cache with the specified initial and maximum capacities.
        /// </summary>
        /// <param name="initialSize"></param>
        /// <param name="capacity"></param>
        public Cache(int initialSize, int capacity) : base()
        {
            // Initial size cannot be smaller than 1.
            if (initialSize < 1)
            {
                initialSize = 1;
            }

            // Capacity cannot be smaller than 0.
            // If capacity is 0, there is no maximum size for
            // the cache and it will always grow.
            if (capacity < 0)
            {
                capacity = 0;
            }

            _cacheBacker = new CacheBacker(initialSize);
            _capacity = capacity;

            // Instantiate the lock.
            _cacheBackerLock = new ReaderWriterLock();
        }

        /// <summary>
        /// Allows the user to retrieve a cached item from the
        /// cache. If it doesn't exist in the cache, it is
        /// retrieved with the FetchItem event and entered into
        /// the cache.
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public object Fetch(string key, object param)
        {
            object tempItem;

            _cacheBackerLock.AcquireReaderLock(-1);
            try
            {
                // Get the item from the cache _cacheBacker.
                tempItem = _cacheBacker[key];
            }
            finally
            {
                _cacheBackerLock.ReleaseReaderLock();
            }

            if (tempItem != null)
            {
                // If the item exists, return it to the user.
                _hits++;
                return tempItem;
            }

            // Missed
            _misses++;

            // The item is not in the cache.
            // If no user has bound the FetchItem event,
            // then the correct item cannot be retrieved.
            // So null is simply returned.
            if (FetchItem == null)
            {
                return null;
            }

            // Fetch the correct item from the user by
            // raising the FetchItem event.
            tempItem = FetchItem(this, new FetchItemEventArgs(key, param));

            // Nulls are not inserted into the cache.
            if (tempItem == null)
            {
                return null;
            }

            // The fetched item is not null. Call the 
            // RemoveItems method to remove items that
            // are too old.
            RemoveItems();

            _cacheBackerLock.AcquireWriterLock(-1);
            try
            {
                // Add the new item to the cache.
                _cacheBacker.Add(key, ref tempItem);
            }
            finally
            {
                _cacheBackerLock.ReleaseWriterLock();
            }

            return tempItem;
        }

        /// <summary>
        /// Gets the size of the cache.
        /// </summary>
        public int Count
        {
            get
            {
                _cacheBackerLock.AcquireReaderLock(-1);
                try
                {
                    return _cacheBacker.Count;
                }
                finally
                {
                    _cacheBackerLock.ReleaseReaderLock();
                }
            }
        }

        /// <summary>
        /// Clears the entire content of the cache.
        /// </summary>
        public void Clear()
        {
            _cacheBackerLock.AcquireWriterLock(-1);
            try
            {
                _cacheBacker.Clear();
            }
            finally
            {
                _cacheBackerLock.ReleaseWriterLock();
            }
        }

        /// <summary>
        /// Removes oldest items until the number of items in the 
        /// cache is below capacity.
        /// </summary>
        protected void RemoveItems()
        {
            string tempKey;
            object tempItem;

            _cacheBackerLock.AcquireWriterLock(-1);

            try
            {
                if (_capacity == 0)
                {
                    return;
                }

                while (_capacity - 1 < _cacheBacker.Count)
                {
                    tempItem = _cacheBacker.Remove(out tempKey);
                    if (CacheItemExpired != null)
                    {
                        CacheItemExpired(this,new CacheItemExpiredEventArgs(tempKey, ref tempItem));
                    }
                }
            }
            finally
            {
                _cacheBackerLock.ReleaseWriterLock();
            }
        }

        /// <summary>
        /// Gets or sets the capacity of the cache.
        /// </summary>
        public int Capacity
        {
            get
            {
                return _capacity;
            }
            set
            {
                if (value < 0)
                {
                    _capacity = 0;
                }
                else
                {
                    _capacity = value;
                }
            }
        }

        /// <summary>
        /// Number of cache hits since the last reset
        /// </summary>
        public long Hits
        {
            get
            {
                return _hits;
            }
        }

        /// <summary>
        /// Number of cache misses since the last reset
        /// </summary>
        public long Misses
        {
            get
            {
                return _misses;
            }
        }

        /// <summary>
        /// Reset Hits and Misses to zero.
        /// </summary>
        public void ResetStatistics()
        {
            _hits = 0;
            _misses = 0;
        }

    }
}
