package org.opendma.api;

/**
 * Representation of the <i>unique object identifier<i> that can be used to
 * identify an <ocde>OdmaObject</code> inside a <code>OdmaRepository</code>.<br>
 * To globally identify an object you have to use the <code>OdmaGuid</code>.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaId
{

    protected String uuid;
    
    /**
     * Create a new OdmaId from a String representation of an id.
     * 
     * @param id
     *            The string representation to create this OdmaId from
     */
    public OdmaId(String id)
    {
        uuid = id;
    }

    /**
     * Returns true if the given Object is a <code>OdmaId</code> or an
     * <code>OdmaGuid</code> identifying the same object.
     * 
     * @param obj
     *            The object to compare this Id to
     * 
     * @return true if and only if the given Object is a <code>OdmaId</code>
     *         or an <code>OdmaGuid</code> identifying the same object.
     */
    public boolean equals(Object obj)
    {
        return (obj instanceof OdmaId) && ((OdmaId)obj).uuid.equals(uuid);
    }

    /**
     * Returns the <code>String</code> representation of this Identifier.
     * 
     * @return the <code>String</code> representation of this Identifier.
     */
    public String toString()
    {
        return uuid;
    }

}