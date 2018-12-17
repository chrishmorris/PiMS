using System;
using System.Data.Odbc;

using OPPF.Proxies;
using OPPF.Utilities;
using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Scheduling;
using log4net;
using log4net.Config;
using System.Collections.Generic;

namespace OPPF.Integrations.ImagerLink.Scheduling
{
	/// <summary>
	/// Manages the scheduling for plates for RockImager v2.0.4.
    /// TODO: Implement when appropriate - not working as claimed.
	/// </summary>
	/// <remarks>
    /// See Deni comments embedded in the IImageNowTask region.
	/// </remarks>
	public class ImagingTaskProvider204 : ImagingTaskProvider, IImageNowTask
	{

		#region Constructors

		/// <summary>
		/// Zero-arg constructor. Initializes the logger and calls connectToDB().
		/// </summary>
		public ImagingTaskProvider204()
		{
            // Intentionally empty - C# should inherit parent constructor automatically
            // and execute before any code here
		}

		#endregion

        #region IImageNowTask implementation

        /*
         * From email exchanges with Deni Setiawan <deni@formulatrix.com>:
         * 
Q. Is this a mechanism for making "image this plate now" functionality directly available from RockMaker rather than having to go to RockImager?
A. Yes, the purpose of IImageNowTask is to allow another application (i.e: RockMaker) to notify RockImager to image selected plate immediately.
         * 
Q. What's supposed to be in the IDictionary returned by the Get method? Is it an int-index list of plateIDs?
A. It contains collection of ID and barcode of selected Plate(s) that are sent to RockImager to be imaged.
ID is not plateID, it's the ID from a table that maintains the ImageNow notification records.
This ID's will be used to update the state of each ImageNow tasks that is executed by SetImageNowTasksHandled method
as soon as RI receives these ImageNow notification records.
         * 
Q. Is the string value in the IDictionary the barcode or the plateID?
A. Plate barcode.
         * 
Q. How often is the Get method called?
A. It is called whenever RI synchronize the imaging schedules.
The default is every 5 seconds, but it is configurable from RockImager.config file with key="ImageQueue.SynchronizeSchedulesMillis".
         * 
Q. What advantage does this have over creating new IImagingTasks and returning them to RockImager via IImageTaskProvider in the usual way?
A. The usual way requires RockImager Imaging state to be in Automatic imaging ("Imaging" - "Image Schedule Plates").
While ImageNow will be executed immediately regardless of RockImager imaging state.
         * 
         * On the basis of the last answer it is unlikely that the OPPF would want to provide a
         * functional implementation of this interface. If the imager is not in Automatic mode
         * its probably for maintenance or plate entry/removal. We would not want imaging to be
         * initiated other than from the console in these circumstances.
         */

        /// <summary>
        /// Returns all barcode of experiments marked with ImageNow command from RockMaker.
        /// Return an emtpy list if does not require RockMaker ImageNow functionality.
        /// </summary>
        /// <returns></returns>
        IDictionary<int, string> IImageNowTask.GetImageNowTasks()
        {
            return new System.Collections.Generic.Dictionary<int, string>(0);
        }

        /// <summary>
        /// Set flag on the list of taskID that has been added to ImagingTask schedule
        /// </summary>
        /// <param name="taskIDList"></param>
        void IImageNowTask.SetImageNowTasksHandled(List<int> taskIDList)
        {
            // Intentionally empty - see comments above
        }

        #endregion

    }

}
