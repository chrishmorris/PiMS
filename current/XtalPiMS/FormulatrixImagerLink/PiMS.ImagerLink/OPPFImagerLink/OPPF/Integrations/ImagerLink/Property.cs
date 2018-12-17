using System;

using Formulatrix.Integrations.ImagerLink;
using log4net;

namespace OPPF.Integrations.ImagerLink
{
	/// <summary>
	/// A simplistic container for properties.
	/// </summary>
	/// <remarks>This is a basic bean implementation of <c>IProperty</c>, with setters
	/// as well as the interface-defined getters.</remarks>
	public class Property : IProperty
    {
        #region Statics

        public static string ToString(IProperty property)
        {
            return "[" + property.Name + "=" + property.Value + "]";
        }

        #endregion

        /// <summary>
        /// The logger.
        /// </summary>
        private readonly ILog _log;

        /// <summary>
        /// The Name of the IProperty
        /// </summary>
		private string _name;

        /// <summary>
        /// The Value of the IProperty
        /// </summary>
        private string _value;

        /// <summary>
		/// Zero-arg constructor, applying default attribute values.
		/// </summary>
		/// <remarks>Strings are set to the empty string.</remarks>
		public Property() : this("", "")
		{
            /*
             * Do nothing here!
             * C# docs imply code here will be executed after
             * the code in the Robot(string, string) constructor
             */
        }

		/// <summary>
		/// Full arg constructor, applying specified values.
		/// </summary>
        /// <param name="name">The Name of the IProperty</param>
        /// <param name="value">The Value of the IProperty</param>
		public Property(string name, string value)
		{
			SetName(name);
			SetValue(value);

            // Get Logger.
            _log = LogManager.GetLogger(this.GetType());

            // Log the call to the constructor
            _log.Debug("Constructed a new " + this);

        }

		#region IProperty Members

        /// <summary>
        /// Gets the property name.
        /// </summary>
        string IProperty.Name
        {
            get
            {
                if (_log.IsDebugEnabled)
                {
                    if (null == _name)
                    {
                        _log.Debug("Called IProperty.Name - returning null");
                    }
                    else
                    {
                        _log.Debug("Called IProperty.Name - returning \"" + _name + "\"");
                    }
                }
                return _name;
            }
        }

        /// <summary>
		/// Gets the property value.
		/// </summary>
        string IProperty.Value
		{
			get
			{
                if (_log.IsDebugEnabled)
                {
                    if (null == _name)
                    {
                        _log.Debug("Called IProperty.Value - returning null");
                    }
                    else
                    {
                        _log.Debug("Called IProperty.Value - returning \"" + _value + "\"");
                    }
                }
                return _value;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetName(string name)
        {
            _name = name;
        }

        public void SetValue(string value)
        {
            _value = value;
        }

        #endregion

        public string ToString()
        {
            return Property.ToString(this);
        }
	}
}
