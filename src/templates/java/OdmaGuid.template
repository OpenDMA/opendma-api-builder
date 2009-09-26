package org.opendma.api;

/**
 * Representation of the <i>global unique object identifier<i> that can be used
 * to globally identify an <code>OdmaObject</code>. A reference to the containing
 * <code>OdmaRepository</code> is included in this ID.<br>
 * To identify an object only inside an <code>OdmaRepository</code>, you can
 * use the <code>OdmaId</code>.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public interface OdmaGuid
{

    /**
     * Returns true if the given Object is a <code>OdmaGuid</code> or an
     * <code>OdmaGuid</code> identifying the same object.
     * 
     * @param obj
     *            The object to compare this Id to
     * 
     * @return true if and only if the given Object is a <code>OdmaId</code>
     *         or an <code>OdmaGuid</code> identifying the same object.
     */
    public boolean equals(Object obj);

    /**
     * Returns the <code>String</code> representation of this Identifier.
     * 
     * @return the <code>String</code> representation of this Identifier.
     */
    public String toString();

}