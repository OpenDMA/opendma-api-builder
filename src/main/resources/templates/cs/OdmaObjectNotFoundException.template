using System;
using System.Collections.Generic;
using System.Text;

namespace OpenDMA.Api
{

    /// <summary>
    /// Thrown when an OpenDMA implementation is unable to locate the requested object.
    /// </summary>
    public class OdmaObjectNotFoundException : OdmaException
    {

        private readonly OdmaGuid? _objectGuid;

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class.
        /// </summary>
        /// <param name="objectGuid">The unique identifier of the object that was not found.</param>
        public OdmaObjectNotFoundException(OdmaGuid objectGuid) 
            : base(objectGuid != null ? "Object not found: "+objectGuid.ToString() : "Object not found")
        {
            this._objectGuid = objectGuid;
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class with a specified error message.
        /// </summary>
        /// <param name="message">The message that describes the error.</param>
        /// <param name="objectGuid">The unique identifier of the object that was not found.</param>
        public OdmaObjectNotFoundException(string message, OdmaGuid objectGuid) 
            : base(message)
        {
            this._objectGuid = objectGuid;
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class with a specified error message and a reference to the inner exception that is the cause of this exception.
        /// </summary>
        /// <param name="message">The message that describes the error.</param>
        /// <param name="innerException">The exception that is the cause of the current exception.</param>
        /// <param name="objectGuid">The unique identifier of the object that was not found.</param>
        public OdmaObjectNotFoundException(string message, Exception innerException, OdmaGuid objectGuid) 
            : base(message, innerException)
        {
            this._objectGuid = objectGuid;
        }

        /// <summary>
        /// Gets the unique identifier of the object that was not found.
        /// </summary>
        public OdmaGuid? ObjectGuid => _objectGuid;

    }

}
