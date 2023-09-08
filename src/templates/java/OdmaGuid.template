package org.opendma.api;

/**
 * OdmaGuid represents a globally unique identifier for an <code>OdmaObject</code>.
 * It combines the <code>OdmaId</code> of the object with the <code>OdmaId</code> of the repository it is contained in.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class OdmaGuid {

    /** The <code>OdmaId</code> of the object. */
    private final OdmaId objectId;

    /** The <code>OdmaId</code> of the repository. */
    private final OdmaId repositoryId;

    /**
     * Constructs an OdmaGuid with the specified object ID and repository ID.
     *
     * @param objectId     The <code>OdmaId</code> of the object. Must not be null.
     * @param repositoryId The <code>OdmaId</code> of the repository. Must not be null.
     * @throws IllegalArgumentException if either objectId or repositoryId is null.
     */
    public OdmaGuid(OdmaId objectId, OdmaId repositoryId) {
        if (objectId == null) {
            throw new IllegalArgumentException("objectId must not be null");
        }
        if (repositoryId == null) {
            throw new IllegalArgumentException("repositoryId must not be null");
        }
        this.objectId = objectId;
        this.repositoryId = repositoryId;
    }

    /**
     * Returns the OdmaId of the object.
     *
     * @return The OdmaId of the object.
     */
    public OdmaId getObjectId() {
        return objectId;
    }

    /**
     * Returns the OdmaId of the repository.
     *
     * @return The OdmaId of the repository.
     */
    public OdmaId getRepositoryId() {
        return repositoryId;
    }

    /**
     * Compares this OdmaGuid to another object for equality.
     * Two OdmaGuid objects are considered equal if their objectId and repositoryId are equal.
     *
     * @param obj The object to compare with this OdmaGuid.
     * @return true if the given object is equal to this OdmaGuid, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OdmaGuid other = (OdmaGuid) obj;
        return objectId.equals(other.objectId) && repositoryId.equals(other.repositoryId);
    }

    /**
     * Returns a hash code value for this OdmaGuid.
     * The hash code is based on the objectId and repositoryId.
     *
     * @return The hash code value for this OdmaGuid.
     */
    @Override
    public int hashCode() {
        int result = objectId.hashCode();
        result = 31 * result + repositoryId.hashCode();
        return result;
    }

    /**
     * Returns a string representation of this OdmaGuid.
     * The format is "objectId:repositoryId".
     *
     * @return A string representation of this OdmaGuid.
     */
    @Override
    public String toString() {
        return objectId.toString() + ":" + repositoryId.toString();
    }

}
