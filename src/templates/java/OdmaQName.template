package org.opendma.api;

/**
 * Represents a qualified name in OpenDMA, consisting of a qualifier and a name.
 * Both the qualifier and the name must not be null or empty, and they are immutable after creation.
 * This class is designed to be immutable and thread-safe.
 */
public final class OdmaQName {

    /** the qualifier part of the qualified name */
    private final String qualifier;

    /** the name part of the qualified name */
    private final String name;

    /**
     * Constructs an <code>OdmaQName</code> with the specified qualifier and name.
     *
     * @param qualifier the qualifier part of the qualified name, must not be null or empty
     * @param name      the name part of the qualified name, must not be null or empty
     * @throws IllegalArgumentException if either the qualifier or name is null or empty
     */
    public OdmaQName(String qualifier, String name) {
        if (qualifier == null || qualifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Qualifier must not be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        this.qualifier = qualifier;
        this.name = name;
    }

    /**
     * Returns the qualifier part of this OpenDMA qualified name.
     *
     * @return the qualifier as a String
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Returns the name part of this OpenDMA qualified name.
     *
     * @return the name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this OdmaQName is equal to another object.
     * Two <code>OdmaQName</code> objects are considered equal if their qualifier and name are both equal.
     *
     * @param obj the object to compare with
     * @return true if the given object is equal to this <code>OdmaQName</code>, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OdmaQName that = (OdmaQName) obj;
        return qualifier.equals(that.qualifier) && name.equals(that.name);
    }

    /**
     * Returns a hash code value for this <code>OdmaQName</code>.
     * The hash code is based on both the qualifier and the name.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    /**
     * Returns a string representation of this <code>OdmaQName</code>.
     *
     * @return the string representation of this <code>OdmaQName</code>
     */
    @Override
    public String toString() {
        return qualifier + ":" + name;
    }

}
