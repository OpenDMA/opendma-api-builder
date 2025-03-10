package org.opendma.apibuilder;

/**
 * Representation of a <i>qualified name</i> consisting of a name <i>qualifier</i> and the <i>name</i> iteself.<br>
 * Using qualified names instead of simple Strings enables OpenDMA to mix different architectures with identical names
 * and distinguish them by their qualifier.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaApiBuilderQName
{

    /** the name <i>qualifier</i> of this qualified name */
    protected String qualifier;

    /** the <i>name</i> of this qualified name */
    protected String name;

    /**
     * Create a new <code>OdmaQName</code> with an empty name qualifier.
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
        this.qualifier = null;
        this.name = name;
    }

    /**
     * Create a new <code>OdmaQName</code> for a given <i>name qualifier</i> and <i>name</i>.
     * 
     * @param qualifier
     *            The name <i>qualifier</i> of this qualified name. Can be null. Must not be empty (if not null).
     * @param name
     *            The <i>name</i> of this qualified name. Must not be null. Must not be empty.
     * 
     * @throws IllegalArgumentException
     *             if the name is null or empty or if the qualifier is not null and empty
     */
    public OdmaApiBuilderQName(String qualifier, String name)
    {
        if (name == null)
        {
            throw new NullPointerException("The name of a OdmaApiBuilderQName may not be null.");
        }
        if ((qualifier != null) && (qualifier.length() == 0))
        {
            throw new IllegalArgumentException("The qualifier of a OdmaApiBuilderQName must have at least 1 character.");
        }
        if (name.length() == 0)
        {
            throw new IllegalArgumentException("The name of a OdmaApiBuilderQName must have at least 1 character.");
        }
        this.qualifier = qualifier;
        this.name = name;
    }

    /**
     * Returns the name <i>qualifier</i> of this qualified name.
     * 
     * @return the qualifier of this qualified name.
     */
    public String getQualifier()
    {
        return qualifier;
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
        result = prime * result + ((qualifier == null) ? 0 : qualifier.hashCode());
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
        if (qualifier == null)
        {
            if (other.qualifier != null)
                return false;
        }
        else if (!qualifier.equals(other.qualifier))
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
        return (qualifier == null ? "<null>" : qualifier) + ":" + name;
    }

}