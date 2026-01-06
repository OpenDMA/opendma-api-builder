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

        private readonly OdmaId? _repositoryId;

        private readonly OdmaId? _objectId;

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class.
        /// </summary>
        /// <param name="repositoryId">The unique identifier of the repository that was not found.</param>
        public OdmaObjectNotFoundException(OdmaId repositoryId) 
            : base("Repository not found: `"+repositoryId+"`")
        {
            this._repositoryId = repositoryId;
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class.
        /// </summary>
        /// <param name="repositoryId">The unique identifier of the existing repository.</param>
        /// <param name="objectId">The unique identifier of the object that was not found.</param>
        public OdmaObjectNotFoundException(OdmaId repositoryId, OdmaId objectId)
            : base("Object `"+objectId+"` not found in repository `"+repositoryId+"`")
        {
            this._repositoryId = repositoryId;
            this._objectId = objectId;
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class with a specified error message.
        /// </summary>
        /// <param name="message">The message that describes the error.</param>
        /// <param name="repositoryId">The unique identifier of the repository.</param>
        /// <param name="objectId">The unique identifier of the object that was not found or null if the repository does not exist.</param>
        public OdmaObjectNotFoundException(string message, OdmaId repositoryId, OdmaId objectId) 
            : base(message)
        {
            this._repositoryId = repositoryId;
            this._objectId = objectId;
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="OdmaObjectNotFoundException"/> class with a specified error message and a reference to the inner exception that is the cause of this exception.
        /// </summary>
        /// <param name="message">The message that describes the error.</param>
        /// <param name="innerException">The exception that is the cause of the current exception.</param>
        /// <param name="repositoryId">The unique identifier of the repository.</param>
        /// <param name="objectId">The unique identifier of the object that was not found or null if the repository does not exist.</param>
        public OdmaObjectNotFoundException(string message, Exception innerException, OdmaId repositoryId, OdmaId objectId) 
            : base(message, innerException)
        {
            this._repositoryId = repositoryId;
            this._objectId = objectId;
        }

        /// <summary>
        /// Gets the unique identifier of the repository
        /// </summary>
        public OdmaId? RepositoryId => _repositoryId;

        /// <summary>
        /// Gets the unique identifier of the object that was not found or null if the repository has not been found
        /// </summary>
        public OdmaId? ObjectId => _objectId;

    }

}
