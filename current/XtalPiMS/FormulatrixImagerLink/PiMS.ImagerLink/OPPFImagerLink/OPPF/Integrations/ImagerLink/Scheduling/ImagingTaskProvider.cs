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
	public class ImagingTaskProvider : IImagingTaskProvider
	{
		#region Class Members

		/// <summary>
		/// The logger.
		/// </summary>
		private readonly ILog _log;

		/// <summary>
		/// The connection string for connecting to platedb.
		/// TODO - move to config file
		/// </summary>
		private static readonly string PLATEDB_CONNECTION_STRING = "driver={PostgreSQL Unicode};Server=localhost;Database=platedb;UID=PlateDBUser;PWD=PlateDBUser;";

		/// <summary>
		/// Minimum number of imaging tasks to prevent a call to the webservice
		/// version of GetImagingTasks.
		/// TODO - move to config file
		/// </summary>
		private static readonly int MIN_IMAGING_TASKS = 10;

		/// <summary>
		/// The connection to platedb.
		/// </summary>
		private OdbcConnection _platedbConnection;

		/// <summary>
		/// The query that gets the list of imaging tasks.
		/// </summary>
		private OdbcCommand _command;
		
		#endregion

		#region Constructors

		/// <summary>
		/// Zero-arg constructor. Initializes the logger and calls connectToDB().
		/// </summary>
		public ImagingTaskProvider()
		{

            // Load configuration
            OPPFConfigXML.Configure();

            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

			// Log the call to the constructor
			_log.Debug("Constructed a new " + this);

			// Connect to DB
			connectToDB();

		}

		#endregion

		#region Class Private Methods

		/// <summary>
		/// Connect to platedb and initialize the command that retrieves imaging tasks
		/// </summary>
		private void connectToDB()
		{
			// Get a new ODBC connection to platedb
			_platedbConnection = new OdbcConnection(PLATEDB_CONNECTION_STRING);

			try 
			{
				// Open the connection
				_platedbConnection.Open();

				// Create a Command object
				_command = _platedbConnection.CreateCommand();
				//command.CommandText = "select dttoimage, dtimaged, intpriority, intstate from tblschedules where strbarcode = ? and strinstrument = ?";
				_command.CommandText = "select dttoimage AT TIME ZONE 'UTC', dtimaged AT TIME ZONE 'UTC', intpriority, intstate from tblschedules where strbarcode = ? and strinstrument = ?";
				_command.Parameters.Add("@p1", OdbcType.VarChar);
				_command.Parameters.Add("@p2", OdbcType.VarChar);
			}
			catch (Exception e)
			{
				// Log it
				string msg = "connectToDB threw " + e.GetType() + ": " + e.Message;
				_log.Fatal(msg, e);

				// Really do want to rethrow - this is bad news
				throw e;
			}

		}

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

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".GetImagingTasks(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID +"\")";
				_log.Debug(msg);
			}

			// Declare the array that will be populated and returned - default to a zero-length array
			IImagingTask[] iImagingTasks = new IImagingTask[0];

			// Ensure connected to database - may throw an exception but we don't need/want to catch it
			if (System.Data.ConnectionState.Closed.Equals(_platedbConnection.State))
			{
				connectToDB();
			}

			// Try and read the imaging tasks from the db
			try
			{

				// TODO - unnecessary check? Can connectToDB fail without throwing an exception?
				if (System.Data.ConnectionState.Open.Equals(_platedbConnection.State))
				{
					// Update the parameters
					_command.Parameters["@p1"].Value = plateID;
					_command.Parameters["@p2"].Value = robot.Name;

					// Execute the select
					OdbcDataReader dataReader = _command.ExecuteReader();

					// Read the data reader's rows into the ProjectList
					if (dataReader.HasRows)
					{
						// Get an ArrayList to hold the tasks
						System.Collections.ArrayList tasks = new System.Collections.ArrayList();

						// Loop over all the returned rows
						while (dataReader.Read())
						{
							// Create and populate a new ImagingTask
							OPPF.Integrations.ImagerLink.Scheduling.ImagingTask task = new OPPF.Integrations.ImagerLink.Scheduling.ImagingTask();
                            // .NET < 2.0
                            //task.DateToImage = dataReader.GetDateTime(0);
							// .NET >= 2.0 Only!
                            task.SetDateToImage(DateTime.SpecifyKind(dataReader.GetDateTime(0), DateTimeKind.Utc));
							if (dataReader.IsDBNull(1))
							{
								task.SetDateImaged(DateTime.MinValue);
							}
							else
							{
                                // .NET < 2.0
								//task.DateImaged = dataReader.GetDateTime(1);
								// .NET >= 2.0 Only!
                                task.SetDateImaged(DateTime.SpecifyKind(dataReader.GetDateTime(1), DateTimeKind.Utc));
								//task.DateImaged = dataReader.GetDateTime(1).ToLocalTime();
							}
							task.SetPriority(dataReader.GetInt32(2));
							task.SetState((Formulatrix.Integrations.ImagerLink.Scheduling.ImagingState) dataReader.GetInt32(3));
							task.SetInQueue(true);

							// Store this task in the ArrayList
							tasks.Add(task);

							//_log.Debug("> Got task: DateToImage=" + task.DateToImage + ", Priority=" + task.Priority + ", State=" + task.State + ", InQueue= " + task.InQueue);
						}

						// Convert ArrayList to IImagingTask[]
						iImagingTasks = (IImagingTask[]) tasks.ToArray(typeof(IImagingTask));

					}

					// Close the dataReader
					dataReader.Close();

				}

			}
			catch (Exception e)
			{
				// Log it
				string msg = "Exception " + e.Message + " during direct db part of getImagingTasks() for plate " + plateID + " - will fail down to webservice call";
				_log.Error(msg, e);

				// Don't rethrow - fail down to webservice call
			}

			// If we got no or not enough tasks
			if ((null == iImagingTasks) || (iImagingTasks.GetLength(0) < MIN_IMAGING_TASKS))
			{

				// Hit the webservice, which may cause a schedule to be created
				iImagingTasks = GetImagingTasksFromWebService(robot, plateID);

			}

			// Set an appropriate value for InQueue
			setInQueue(iImagingTasks);
			
			// Ensure we never return null
			if (null == iImagingTasks)
			{
				// Fix it
				iImagingTasks = new IImagingTask[0];

				// Log it
				_log.Warn("Fixed null iImagingTasks at end of WSPlate.getImagingTasks() for plate " + plateID);
			}

			// Return the array of IImagingTasks
			return iImagingTasks;

		}

		/// <summary>
		/// Return <c>true</c> if this provider supplies <c>IImagingTask.Priority</c>.
		/// Otherwise the imager should manage individual task priority.
		/// </summary>
        bool IImagingTaskProvider.SupportsPriority(Formulatrix.Integrations.ImagerLink.IRobot robot)
		{

			// OPPF PERFORMANCE BODGE - Actually this method should always return true!
			return true;

			// Check arguments - do it up front to avoid possible inconsistencies later
			if (null == robot) throw new System.NullReferenceException("robot must not be null");

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".SupportsPriority(robot=" + RobotUtils.iRobotToString(robot) + ")";
				_log.Debug(msg);
			}

			// Set the request
			supportsPriority request = new supportsPriority();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);

			// Make the call
			WSPlate wsPlate = new WSPlate();
			supportsPriorityResponse response = null;
			try
			{
				response = wsPlate.supportsPriority(request);
			}
			catch (Exception e)
			{
				// Log it
				string msg = "WSPlate.supportsPriority threw " + e.GetType() + ": " + e.Message + " - ignoring";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Warn(msg, e);

				// Don't rethrow - just return false
				return false;
			}
			
			// Return the response's value
			//_log.Debug("supportsPriority returned " + response.supportsPriorityReturn);
			return response.supportsPriorityReturn;

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

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".UpdatedPriority(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID +"\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC, priority=" + priority + ")";
				_log.Debug(msg);
			}

			// Set the request
			updatedPriority request = new updatedPriority();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
			request.plateID = plateID;
            // .NET < 2.0
            //request.dateToImage = dateToImage;
            // .NET >= 2.0
            request.dateToImage = dateToImage.ToUniversalTime();
            request.priority = priority;

			// Make the call
			WSPlate wsPlate = new WSPlate();
			try 
			{
				wsPlate.updatedPriority(request);
			}
			catch (Exception e) 
			{
				// Log it
				string msg = "WSPlate.updatedPriority threw " + e.GetType() + ": " + e.Message + " - ignoring";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Warn(msg, e);

				// Don't rethrow - just return - don't really care if this didn't work
				return;
			}

			// Webservice always returns true if there is no exception
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

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".ImagingPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC, dateImaged=" + dateImaged.ToUniversalTime().ToString() + " UTC)";
				_log.Debug(msg);
			}

			// Set the request
			imagingPlate request = new imagingPlate();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
			request.plateID = plateID;
			request.scheduled = scheduled;
			request.dateToImage = dateToImage.ToUniversalTime();
			request.dateImaged = dateImaged.ToUniversalTime();

			// Make the call
			WSPlate wsPlate = new WSPlate();
			imagingPlateResponse response = null;
			try
			{
				response = wsPlate.imagingPlate(request);
			}
			catch (Exception e) 
			{
				string msg = "WSPlate.imagingPlate threw " + e.GetType() + ": " + e.Message + " - panic!";
                msg = msg + "\nin " + this + ".ImagingPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC, dateImaged=" + dateImaged.ToUniversalTime().ToString() + " UTC)";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Fatal(msg, e);

				// Panic
				throw e;
			}

			// If we got no response
			if (null == response)
			{
				string msg = "WSPlate.imagingPlate returned null - panic!";
                msg = msg + "\nin " + this + ".ImagingPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC, dateImaged=" + dateImaged.ToUniversalTime().ToString() + " UTC)";
                _log.Fatal(msg);

				// Panic
				throw new System.NullReferenceException(msg);
			}

			// If we got a null imagingID
			if (null == response.imagingPlateReturn)
			{
				string msg = "WSPlate.imagingPlate returned a null imagingID - panic!";
                msg = msg + "\nin " + this + ".ImagingPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC, dateImaged=" + dateImaged.ToUniversalTime().ToString() + " UTC)";
                _log.Fatal(msg);

				// Panic
				throw new System.NullReferenceException(msg);
			}

			// Return the imagingID
			return response.imagingPlateReturn;

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

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".ImagedPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID +"\", imagingID=\"" + imagingID + "\")";
				_log.Debug(msg);
			}

			// Set the request
			imagedPlate request = new imagedPlate();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
			request.plateID = plateID;
			request.imagingID = imagingID;

			// Make the call
			WSPlate wsPlate = new WSPlate();
			imagedPlateResponse response = null;
			try
			{
				response = wsPlate.imagedPlate(request);
			}
			catch (Exception e)
			{
				// Log it
				string msg = "WSPlate.imagedPlate threw " + e.GetType() + ": " + e.Message + " - panic!";
                msg = msg + "\nin " + this + ".ImagedPlate(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", imagingID=\"" + imagingID + "\")";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Fatal(msg, e);

				// Panic
				throw e;
			}
			
			// Webservice always returns true if it doesn't throw an exception
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

			// Log the call to the method
			if (_log.IsDebugEnabled) 
			{
				string msg = "Called " + this + ".SkippedImaging(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID +"\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC)";
				_log.Debug(msg);
			}

			// Set the request
			skippedImaging request = new skippedImaging();
			request.robot = OPPF.Utilities.RobotUtils.createProxy(robot);
			request.plateID = plateID;
			request.dateToImage = dateToImage.ToUniversalTime();

			// Make the call
			WSPlate wsPlate = new WSPlate();
			skippedImagingResponse response = null;
			try
			{
				response = wsPlate.skippedImaging(request);
			}
			catch (Exception e)
			{
				// Log it
				string msg = "WSPlate.skippedImaging threw " + e.GetType() + ": " + e.Message + " - ignoring";
                msg = msg + "\nin " + this + ".SkippedImaging(robot=" + RobotUtils.iRobotToString(robot) + ", plateID=\"" + plateID + "\", dateToImage=" + dateToImage.ToUniversalTime().ToString() + " UTC)";
                if (e is System.Web.Services.Protocols.SoapException)
                {
                    System.Web.Services.Protocols.SoapException ee = (System.Web.Services.Protocols.SoapException)e;
                    msg = msg + "\n\n" + ee.Detail.InnerXml;
                }
                _log.Warn(msg, e);

				// Don't rethrow - just return - don't really care if this didn't work
				return;
			}

			// Webservice always returns true if it doesn't throw an exception
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
