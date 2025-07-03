package org.opendma.api;

/**
 * Represents a qualified name in OpenDMA, consisting of a namespace and a name.
 * Both the namespace and the name must not be null or empty, and they are immutable after creation.
 * This class is designed to be immutable and thread-safe.
 */
public final class OdmaQName {

    /** the namespace part of the qualified name */
    private final String namespace;

    /** the name part of the qualified name */
    private final String name;

    /**
     * Constructs an <code>OdmaQName</code> with the specified namespace and name.
     *
     * @param namespace the namespace part of the qualified name, must not be null or empty
     * @param name      the name part of the qualified name, must not be null or empty
     * @throws IllegalArgumentException if either the namespace or name is invalid
     */
    public OdmaQName(String namespace, String name) {
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace must not be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (name.contains(":")) {
            throw new IllegalArgumentException("Name must not contain colon ':' character");
        }
        if (namespace.startsWith(":") || namespace.endsWith(":") || namespace.contains("::")) {
            throw new IllegalArgumentException("Segments in Namespace must have at least 1 character");
        }
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Returns the namespace part of this OpenDMA qualified name.
     *
     * @return the namespace as a String
     */
    public String getNamespace() {
        return namespace;
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
     * Two <code>OdmaQName</code> objects are considered equal if their namespace and name are both equal.
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
        return namespace.equals(that.namespace) && name.equals(that.name);
    }

    /**
     * Returns a hash code value for this <code>OdmaQName</code>.
     * The hash code is based on both the namespace and the name.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        int result = namespace.hashCode();
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
        return namespace + ":" + name;
    }

    /**
     * Parses a string representation of a qualified name into an {@code OdmaQName}.
     *
     * @param qname the string to parse, in the format "namespace:name"
     * @return an {@code OdmaQName} object with parsed namespace and name
     * @throws IllegalArgumentException if the input does not contain a valid string representation of a qualified name
     */
    public static OdmaQName fromString(String qname) {
        if (qname == null) {
            throw new IllegalArgumentException("Qualified name must not be null");
        }

        int lastColonIndex = qname.lastIndexOf(':');
        if (lastColonIndex == -1) {
            throw new IllegalArgumentException("Qualified name must contain at least one colon");
        }

        String namespace = qname.substring(0, lastColonIndex);
        String name = qname.substring(lastColonIndex + 1);

        return new OdmaQName(namespace, name);
    }

}
