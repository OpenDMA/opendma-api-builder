package org.opendma.api;

/**
 * Represents a unique identifier for an <code>OdmaObject</code> in an
 * <code>OdmaRepository</code>.
 * <p>
 * This class is designed to be immutable and thread-safe.
 * 
 * @see OdmaGuid
 */
public final class OdmaId {

    /** The unique identifier string */
    private final String id;

    /**
     * Constructs an OdmaId with the specified ID.
     * 
     * @param id The unique identifier string. Must not be null or empty.
     * @throws IllegalArgumentException if the id is null or empty.
     */
    public OdmaId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID must not be null or empty");
        }
        this.id = id;
    }

    /**
     * Checks if this OdmaId is equal to another object.
     * Two OdmaId objects are considered equal if their ID strings are equal.
     *
     * @param obj The object to compare with.
     * @return true if the given object is equal to this OdmaId, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OdmaId other = (OdmaId) obj;
        return id.equals(other.id);
    }

    /**
     * Returns a hash code value for this OdmaId.
     * The hash code is based on the ID string.
     *
     * @return the hash code value.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns a string representation of this OdmaId.
     * 
     * @return the ID string of this OdmaId.
     */
    @Override
    public String toString() {
        return id;
    }

}
