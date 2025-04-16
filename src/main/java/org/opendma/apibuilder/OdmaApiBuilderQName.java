package org.opendma.apibuilder;

/**
 * Representation of a <i>qualified name</i> consisting of a <i>namespace</i> and the local <i>name</i>.<br>
 * Using qualified names instead of simple Strings enables OpenDMA to mix different architectures with identical names
 * and distinguish them by their namespace.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaApiBuilderQName
{

    /** the <i>namespace</i> of this qualified name */
    protected String namespace;

    /** the <i>name</i> of this qualified name */
    protected String name;

    /**
     * Create a new <code>OdmaQName</code> with an empty name namespace.
     * 
     * @param name
     *            The <i>name</i> of this qualified name. Must not be null. Must not be empty.
     * 
     * @throws NullPointerException
     *             if the name is null
     * @throws IllegalArgumentException
     *             if the name is empty
     */
    public OdmaApiBuilderQName(String name)
    {
        if (name == null)
        {
            throw new NullPointerException("The name of a OdmaApiBuilderQName must not be null.");
        }
        if (name.length() == 0)
        {
            throw new IllegalArgumentException("The name of a OdmaApiBuilderQName must have at least 1 character.");
        }
        this.namespace = null;
        this.name = name;
    }

    /**
     * Create a new <code>OdmaQName</code> for a given <i>namespace</i> and local <i>name</i>.
     * 
     * @param namespace
     *            The <i>namespace</i> of this qualified name. Can be null. Must not be empty (if not null).
     * @param name
     *            The <i>name</i> of this qualified name. Must not be null. Must not be empty.
     * 
     * @throws IllegalArgumentException
     *             if the name is null or empty or if the namespace is not null and empty
     */
    public OdmaApiBuilderQName(String namespace, String name)
    {
        if (name == null)
        {
            throw new NullPointerException("The name of a OdmaApiBuilderQName may not be null.");
        }
        if ((namespace != null) && (namespace.length() == 0))
        {
            throw new IllegalArgumentException("The namespace of a OdmaApiBuilderQName must have at least 1 character.");
        }
        if (name.length() == 0)
        {
            throw new IllegalArgumentException("The name of a OdmaApiBuilderQName must have at least 1 character.");
        }
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Returns the <i>namespace</i> of this qualified name.
     * 
     * @return the namespace of this qualified name.
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * Returns the <i>name</i> of this qualified name.
     * 
     * @return the name of this qualified name.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OdmaApiBuilderQName other = (OdmaApiBuilderQName) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (namespace == null)
        {
            if (other.namespace != null)
                return false;
        }
        else if (!namespace.equals(other.namespace))
            return false;
        return true;
    }

    /**
     * Retruns a string representation of this qualified name.
     * 
     * @return a string representation of this qualified name.
     */
    public String toString()
    {
        return (namespace == null ? "<null>" : namespace) + ":" + name;
    }

}