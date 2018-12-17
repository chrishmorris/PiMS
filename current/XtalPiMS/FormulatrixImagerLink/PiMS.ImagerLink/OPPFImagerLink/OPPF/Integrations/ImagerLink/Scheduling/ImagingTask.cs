using System;

using Formulatrix.Integrations.ImagerLink.Scheduling;

namespace OPPF.Integrations.ImagerLink.Scheduling
{
	/// <summary>
	/// Specifies the date and time to image a plate, or provides
	/// information about a current/future imaging run.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>IImagingTask</c>, with setters
	/// as well as the interface-defined getters.</remarks>
	public class ImagingTask : IImagingTask
	{

		private Formulatrix.Integrations.ImagerLink.Scheduling.ImagingState _state;
		private DateTime _dateImaged;
		private int _priority;
		private DateTime _dateToImage;
		private bool _inQueue;

		public ImagingTask()
		{
			// Default state is NotCompleted
			SetState(ImagingState.NotCompleted);

			// Default inQueue is true, ie act on this ImagingTask
			SetInQueue(true);
		}

		#region IImagingTask Members

		/// <summary>
		/// Gets/sets the imaging state of <c>NotCompleted</c>, <c>Skipped</c>,
		/// <c>Imaging</c>, or <c>Completed</c>.
		/// </summary>
        Formulatrix.Integrations.ImagerLink.Scheduling.ImagingState IImagingTask.State
		{
			get
			{
				return _state;
			}
		}

		/// <summary>
		/// If the <c>State</c> is <c>Imaging</c> or <c>Completed</c>, the date the plate was imaged.
        /// Note that the DateTime is converted to universal time before storing but the value
        /// returned is in the local timezone.
		/// </summary>
        DateTime IImagingTask.DateImaged
		{
			get
			{
                // .NET < 2.0
                //return dateImaged;
                // .NET >= 2.0 Only!
                return _dateImaged.ToLocalTime();
			}
		}

		/// <summary>
		/// Priority of imaging. <see cref="ScheduleState"/>
		/// for the lowest and default priorities.
		/// </summary>
        /// <remarks>
        /// Hi Gael,
        /// Actually, the lower the priority number, the higher the priority of the
        /// plate (ie "1" gets imaged ahead of "5").
        /// 
        /// Basically, if there are multiple plates waiting in the queue (because they
        /// were all scheduled to be imaged at approximately the same time), the queue
        /// gets sorted by priority, then by schedule. For example:
        /// 
        /// Plate # : Imaging Time : Priority
        /// 1		12:05pm		5
        /// 2		12:06pm		5
        /// 3		12:07pm		5
        /// 
        /// 
        /// But, if the priority of plate #3 is changed:
        /// 
        /// Plate # : Imaging Time : Priority
        /// 3		12:07pm		1
        /// 1		12:05pm		5
        /// 2		12:06pm		5
        /// 
        /// 
        /// Hope that helps.
        /// 
        /// Ian, if you want to test this, you can manually edit the priority in the
        /// Imagign Tasks "Future Imaging Schedule" table. Just click in the priority
        /// cell and change the priority.
        /// 
        /// -Zak
        /// </remarks>
        int IImagingTask.Priority
		{
			get
			{
				return _priority;
			}
		}

		/// <summary>
		/// Date/Time imaging should commence.
        /// Note that the DateTime is converted to universal time before storing but the value
        /// returned is in the local timezone.
        /// </summary>
        DateTime IImagingTask.DateToImage
		{
			get
			{
                // .NET < 2.0
                //return dateToImage;
                // .NET >= 2.0 Only!
                return _dateToImage.ToLocalTime();
			}
		}

		/// <summary>
		/// This property should be true if this <c>ImagingTask</c> is to be acted
		/// on, or false if it should be ignored
		/// </summary>
        bool IImagingTask.InQueue
		{
			get
			{
				return _inQueue;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetState(ImagingState state)
        {
            _state = state;
        }

        public void SetDateImaged(DateTime dateImaged)
        {
            _dateImaged = dateImaged.ToUniversalTime();
        }

        public void SetPriority(int priority)
        {
            _priority = priority;
        }

        public void SetDateToImage(DateTime dateToImage)
        {
            _dateToImage = dateToImage.ToUniversalTime();
        }

        public void SetInQueue(bool inQueue)
        {
            _inQueue = inQueue;
        }

        #endregion

    }
}
