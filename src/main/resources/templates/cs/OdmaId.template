using System;
using System.Collections.Generic;
using System.Text;

namespace OpenDMA.Api
{

    /// <summary>
    /// Represents a unique identifier for an OdmaObject in an OdmaRepository.
    /// This class is designed to be immutable and thread-safe.
    /// </summary>
    /// <seealso cref="OdmaGuid" />
    public sealed class OdmaId
    {

        /// <summary>
        /// The unique identifier string.
        /// </summary>
        private readonly string _id;

        /// <summary>
        /// Constructs an OdmaId with the specified ID.
        /// </summary>
        /// <param name="id">The unique identifier string. Must not be null or empty.</param>
        /// <exception cref="ArgumentException">Thrown if the id is null or empty.</exception>
        public OdmaId(string id)
        {
            if (string.IsNullOrWhiteSpace(id))
            {
                throw new ArgumentException("ID must not be null or empty", nameof(id));
            }
            this._id = id;
        }

        /// <summary>
        /// Checks if this OdmaId is equal to another object.
        /// Two OdmaId objects are considered equal if their ID strings are equal.
        /// </summary>
        /// <param name="obj">The object to compare with.</param>
        /// <returns>true if the given object is equal to this OdmaId, false otherwise.</returns>
        public override bool Equals(object? obj)
        {
            if (ReferenceEquals(this, obj))
            {
                return true;
            }
            if (obj == null || obj.GetType() != GetType())
            {
                return false;
            }
            var other = (OdmaId)obj;
            return _id == other._id;
        }

        /// <summary>
        /// Returns a hash code value for this OdmaId.
        /// The hash code is based on the ID string.
        /// </summary>
        /// <returns>The hash code value.</returns>
        public override int GetHashCode()
        {
            return _id.GetHashCode();
        }

        /// <summary>
        /// Returns a string representation of this OdmaId.
        /// </summary>
        /// <returns>The ID string of this OdmaId.</returns>
        public override string ToString()
        {
            return _id;
        }

    }

}
