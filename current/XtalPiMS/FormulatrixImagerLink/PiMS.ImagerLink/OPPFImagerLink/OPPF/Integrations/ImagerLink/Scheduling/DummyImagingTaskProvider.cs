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
	/// Manages the scheduling for plates.
	/// </summary>
	/// <remarks>
	/// Khalids comments:
	/// The methods of IImagingTaskProvider are required to get schedule
	/// information. FYI, the ImagingID you create is returned later in the RIP. If
	/// your IImageProcessorProvider doesn't use ImagingID, we can work out
	/// something for this value as well.
	/// </remarks>
	public class DummyImagingTaskProvider : IImagingTaskProvider
	{
		#region Class Members

		/// <summary>
		/// The logger.
		/// </summary>
		private readonly ILog _log;

		/// <summary>
		/// Minimum number of imaging tasks to prevent a call to the webservice
		/// version of GetImagingTasks.
		/// TODO - move to config file
		/// </summary>
		private static readonly int MIN_IMAGING_TASKS = 10;

        private static Dictionary<string, ImagingTask> _tasks = new Dictionary<string, ImagingTask>();
        private static List<string> _scheduled = new List<string>();

		#endregion

        /// <summary>
        /// Generate an imagingID
        /// </summary>
        /// <param name="plateID"></param>
        /// <param name="dateToImage"></param>
        /// <returns></returns>
        private static String GetImagingID(string plateID, DateTime dateToImage)
        {
            return plateID + "-" + dateToImage.ToUniversalTime().ToString("yyyyMMdd-hhmmss");
        }

		#region Constructors

		/// <summary>
		/// Zero-arg constructor. Initializes the logger and calls connectToDB().
		/// </summary>
		public DummyImagingTaskProvider()
		{

            // Load configuration
            OPPFConfigXML.Configure();

            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

			// Log the call to the constructor
			_log.Debug("Constructed a new " + this);

		}

		#endregion

        #region Class Private Methods

        /// <summary>
        /// Determines whether the imaging state is unsuitable to be put
        /// in the queue. Only Completed and Skipped are unsuitable.
        /// </summary>
        /// <param name="state">The state to assess</param>
        /// <returns>true if unsuitable to be queued, otherwise false</returns>
        private bool isStateUnqueuable(ImagingState state)
        {
            if (ImagingState.Completed.Equals(state) || ImagingState.Skipped.Equals(state))
            {
                return true;
            }
            return false;
        }


		/// <summary>
		/// Search through the list of imaging tasks and set an appropriate value of
		/// InQueue. This implementation sets InQueue to false for all tasks with
        /// state unsuitable for queuing and all tasks before DateTime.Now except
		/// the one with the latest IImagingTask.DateToImage before DateTime.Now.
		/// </summary>
		/// <param name="tasks">The array of IImagingTasks to process. Assumes that the tasks
		/// are actually the fully editable OPPF implementation of IImagingTask</param>
		private void setInQueue(IImagingTask[] iImagingTasks)
		{
			// Do nothing if tasks is null
			if (null == iImagingTasks) return;

			// Find latest task before or at now
			DateTime now = DateTime.Now;
			IImagingTask latestBeforeNow = null;
			for (int i = 0; i < iImagingTasks.GetLength(0); i++)
            {
				if (1 > iImagingTasks[i].DateToImage.CompareTo(now))
				{
					if (null == latestBeforeNow) 
					{
						latestBeforeNow = iImagingTasks[i];
					}
					else if (1 == iImagingTasks[i].DateToImage.CompareTo(latestBeforeNow.DateToImage))
					{
						latestBeforeNow = iImagingTasks[i];
					}
				}
			}

			// Set inQueue for all unqueuable or before latestBeforeNow to false, otherwise true
			if (null != latestBeforeNow)
			{
				for (int i = 0; i < iImagingTasks.GetLength(0); i++) 
				{
					if (isStateUnqueuable(iImagingTasks[i].State) || (-1 == iImagingTasks[i].DateToImage.CompareTo(latestBeforeNow.DateToImage)))
					{
						((OPPF.Integrations.ImagerLink.Scheduling.ImagingTask) iImagingTasks[i]).SetInQueue(false);
					}
					else 
					{
						((OPPF.Integrations.ImagerLink.Scheduling.ImagingTask) iImagingTasks[i]).SetInQueue(true);
					}
				}
			}

		}

		/// <summary>
		/// Web service version of GetImagingTasks - useful because it will
		/// cause a full schedule to be written if one doesn't already
		/// exist.
		/// 
		/// Warning - this is a lot slower than going straight to platedb!
		/// </summary>
		/// <param name="robot">The robot</param>
		/// <param name="plateID">The barcode of the plate</param>
		/// <returns></returns>
		public IImagingTask[] GetImagingTasksFromWebService(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");

			// Log the call
			if (_log.IsInfoEnabled)
			{
				string msg = "Calling WSPlate.getImagingTasks() for plate " + plateID + ", robot " + RobotUtils.iRobotToString(robot);
				_log.Info(msg);
			}

			// Declare the array that will be populated and returned
			// - default to a zero-length array
			IImagingTask[] iImagingTasks = new IImagingTask[0];

			// Set the request
			getImagingTasks request = new getImagingTasks();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
			request.plateID = plateID;

			// Make the call
			getImagingTasksResponse response = null;
			try
			{
				WSPlate wsPlate = new WSPlate();
				response = wsPlate.getImagingTasks(request);
			}
			catch (Exception e)
			{
				// Log it
				string msg = "WSPlate.getImagingTasks threw " + e.GetType() + ": " + e.Message + " for plateid \"" + plateID + "\" in robot \"" + robot.Name + "\" - returning empty IImagingTask[]";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Error(msg, e);

				// Don't rethrow - just return empty array
				return iImagingTasks;
			}

			// If we got a response
			if (null != response)
			{

				// Get the array of ImagingTasks from the response
				OPPF.Proxies.ImagingTask[] wrapper = response.wrapper;

				// Convert to IImagingTasks
				iImagingTasks = new IImagingTask[wrapper.GetLength(0)];
				for (int i = 0; i < wrapper.GetLength(0); i++)
				{
					OPPF.Integrations.ImagerLink.Scheduling.ImagingTask task = new OPPF.Integrations.ImagerLink.Scheduling.ImagingTask();
					task.SetDateImaged(wrapper[i].dateImaged);
					task.SetDateToImage(wrapper[i].dateToImage);
					task.SetInQueue(wrapper[i].inQueue);
					task.SetPriority(wrapper[i].priority);
					task.SetState((Formulatrix.Integrations.ImagerLink.Scheduling.ImagingState) wrapper[i].state);

					iImagingTasks[i] = task;
				}

			}

			// Return the IImagingTask array
			return iImagingTasks;

		}

		#endregion

		#region IImagingTaskProvider Members

		/// <summary>
		/// Returns all scheduled <c>IImagingTask</c>s for the <c>plateID</c>.
		/// </summary>
        IImagingTask[] IImagingTaskProvider.GetImagingTasks(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");

            IImagingTask[] myTasks = null;

            lock (_scheduled)
            {
                if (! _scheduled.Contains(plateID))
                {
                    _scheduled.Add(plateID);

                    List<IImagingTask> myTaskList = new List<IImagingTask>();

                    DateTime now = DateTime.Now;
                    for (int i = 0; i < 25; i++)
                    {
                        DateTime dateToImage = now.AddHours(i);
                        string imagingID = DummyImagingTaskProvider.GetImagingID(plateID, dateToImage);
                        ImagingTask task = new ImagingTask();
                        task.SetDateToImage(dateToImage);
                        task.SetInQueue(true);
                        task.SetPriority(5);
                        task.SetState(ImagingState.NotCompleted);

                        myTaskList.Add(task);
                        try
                        {
                            _tasks.Add(imagingID, task);
                        }
                        catch (System.ArgumentException e)
                        {
                            // Swallow duplicate key errors
                            if (! e.Message.Contains("same key has already been added"))
                            {
                                throw e;
                            }
                        }

                    }

                    myTasks = myTaskList.ToArray();
                }
            }

            if (null == myTasks)
            {
                List<IImagingTask> myTaskList = new List<IImagingTask>();
                Dictionary<string, ImagingTask>.Enumerator en = _tasks.GetEnumerator();
                while (en.MoveNext())
                {
                    if (en.Current.Key.StartsWith(plateID))
                    {
                        myTaskList.Add(en.Current.Value);
                    }
                }
                myTasks = myTaskList.ToArray();
                setInQueue(myTasks);
            }

            return myTasks;
		}

		/// <summary>
		/// Return <c>true</c> if this provider supplies <c>IImagingTask.Priority</c>.
		/// Otherwise the imager should manage individual task priority.
		/// </summary>
        bool IImagingTaskProvider.SupportsPriority(Formulatrix.Integrations.ImagerLink.IRobot robot)
		{

			// OPPF PERFORMANCE BODGE - Actually this method should always return true!
			return true;

		}

		/// <summary>
		/// If this <c>IImagingTaskProvider</c> supports changing priority,
		/// changes the priority of the IImagingTask with this <c>dateToImage</c>.
		/// </summary>
        void IImagingTaskProvider.UpdatedPriority(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, DateTime dateToImage, int priority)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");

            string imagingID = DummyImagingTaskProvider.GetImagingID(plateID, dateToImage);

            ImagingTask task;
            bool ok = _tasks.TryGetValue(imagingID, out task);
            if (ok)
            {
                task.SetPriority(priority);
            }

            return;
		}

		/// <summary>
		/// Plate is starting to image. Returns an imaging identifier that will be
		/// embedded in the image names, and returned in ImagedPlate or null if
		/// the ImagingID is unavailable.
		/// </summary>
        string IImagingTaskProvider.ImagingPlate(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, bool scheduled, DateTime dateToImage, DateTime dateImaged)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");

            string imagingID = DummyImagingTaskProvider.GetImagingID(plateID, dateToImage);

            /*
             * Unnecessary code
             * 
            bool exists = true;
            if (!scheduled)
            {
                ImagingTask task = new ImagingTask();
                task.SetDateImaged(dateImaged);
                task.SetDateToImage(dateToImage);
                task.SetInQueue(true);
                task.SetPriority(5);
                task.SetState(ImagingState.Imaging);

                lock (_tasks)
                {
                    if (! _tasks.ContainsKey(imagingID))
                    {
                        exists = false;
                        _tasks.Add(imagingID, task);
                    }
                }
            }
             *
             * 
            if (scheduled || exists)
             */

            if (scheduled)
            {
                ImagingTask task;
                bool ok = _tasks.TryGetValue(imagingID, out task);
                if (ok)
                {
                    task.SetDateImaged(dateImaged);
                    task.SetState(ImagingState.Imaging);
                }
            }

            return DummyImagingTaskProvider.GetImagingID(plateID, dateToImage);
		}

		/// <summary>
		/// Marks a imaging as completed.
		/// </summary>
        void IImagingTaskProvider.ImagedPlate(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, string imagingID)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");
			if (null == imagingID) throw new System.NullReferenceException("imagingID must not be null");

            ImagingTask task;
            bool ok = _tasks.TryGetValue(imagingID, out task);
            if (ok)
            {
                task.SetInQueue(false);
                task.SetState(ImagingState.Completed);
            }

            return;
		}

		/// <summary>
		/// Marks an imaging as skipped.
		/// </summary>
        void IImagingTaskProvider.SkippedImaging(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, DateTime dateToImage)
		{

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");
			if (null == plateID) throw new System.NullReferenceException("plateID must not be null");

            string imagingID = DummyImagingTaskProvider.GetImagingID(plateID, dateToImage);

            ImagingTask task;
            bool ok = _tasks.TryGetValue(imagingID, out task);
            if (ok)
            {
                task.SetInQueue(false);
                task.SetState(ImagingState.Skipped);
            }

            return;

		}

        /// <summary>
        /// Retrieves a marker indicating when Rock Imager last checked for schedule updates.
        /// If the integration cannot detect which plates have changed since the last sync, 
        /// this method should return null.
        /// TODO Implement
        /// </summary>
        /// <returns>A schedule sync point, or null if the integration does not support sync points.</returns>
        IScheduleSyncPoint IImagingTaskProvider.GetScheduleSyncPoint()
        {
            return null;
        }

        /// <summary>
        /// Get all plate IDs whose schedule has been modified since the last time Rock Imager synced.  
        /// TODO Implement
        /// </summary>
        /// <param name="syncPoint">The last time the schedule was updated.  The integration should update the syncPoint during this call.</param>
        /// <returns>Plate IDs which have been modified since the last sync.</returns>
        string[] IImagingTaskProvider.GetPlateIDsWithScheduleUpdates(ref IScheduleSyncPoint syncPoint)
        {
            return new string[0];
        }

        #endregion

    }

}
