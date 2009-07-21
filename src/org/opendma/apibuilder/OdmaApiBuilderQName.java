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
        return (qualifier);
    }

    /**
     * Returns the <i>name</i> of this qualified name.
     * 
     * @return the name of this qualified name.
     */
    public String getName()
    {
        return (name);
    }

    /**
     * Compares this <code>OdmaQName</code> to the given object and returns true if and only if the given object is
     * not null and both object are recognized as equal.<br>
     * Two <code>OdmaQName</code>s are equal if:<br>
     * (a) at least one qualifier is null and the names are equal, or<br>
     * (b) both qualifiers are equal and both names are equal.<br>
     * This implicates that a <code>OdmaQName</code> without a qualifier will be equal to any <code>OdmaQName</code>
     * with the same name.
     * 
     * @param qn
     *            the Object to compare this <code>OdmaQName</code> to
     * 
     * @return true if and only if this <code>OdmaQName</code> equals the given object
     */
    public boolean equals(Object qn)
    {
        return (qn != null) && (qn instanceof OdmaApiBuilderQName) && ((((qualifier == null) || (((OdmaApiBuilderQName) qn).qualifier == null)) && name.equals(((OdmaApiBuilderQName) qn).name)) || ((qualifier != null) && qualifier
                .equals(((OdmaApiBuilderQName) qn).qualifier) && name.equals(((OdmaApiBuilderQName) qn).name)));
    }

    /**
     * Compares this <code>OdmaQName</code> to the given object and returns true if and only if the given object is
     * not null and both object are recognized as equal, ignoring case considerations.<br>
     * Two <code>OdmaQName</code>s are equal if:<br>
     * (a) at least one qualifier is null and both names are equal (ignoring case considerations), or<br>
     * (b) both qualifiers are equal (ignoring case considerations) and both names are equal (ignoring case
     * considerations).<br>
     * This implicates that a <code>OdmaQName</code> without a qualifier will be equal to any <code>OdmaQName</code>
     * with the same name.
     * 
     * @param qn
     *            the Object to compare this <code>OdmaQName</code> to
     * 
     * @return true if and only if this <code>OdmaQName</code> equals the given object
     */
    public boolean equalsIgnoreCase(OdmaApiBuilderQName qn)
    {
        return (qn != null) && (qn instanceof OdmaApiBuilderQName) && ((((qualifier == null) || (((OdmaApiBuilderQName) qn).qualifier == null)) && this.name.equalsIgnoreCase(qn.name)) || ((qualifier != null) && this.qualifier
                .equalsIgnoreCase(qn.qualifier) && this.name.equalsIgnoreCase(qn.name)));
    }

    /**
     * Returns the hash code for this qualified name. The hash code for a <code>OdmaQName</code> object is computed as
     * the sum of the hash code of the name <i>qualifier</i> and the hash code of the <i>name</i>.
     * 
     * @return the hash code for this qualified name
     */
    public int hashCode()
    {
        return (((qualifier == null) ? 0 : qualifier.hashCode()) + name.hashCode());
    }

    /**
     * Retruns a string representation of this qualified name.
     * 
     * @return a string representation of this qualified name.
     */
    public String toString()
    {
        return "[" + (qualifier == null ? "<null>" : qualifier) + ", " + name + "]";
    }

}