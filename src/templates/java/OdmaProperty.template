package org.opendma.api;

import java.util.Date;

import org.opendma.api.collections.BlobList;
import org.opendma.api.collections.BooleanList;
import org.opendma.api.collections.DateTimeList;
import org.opendma.api.collections.DoubleList;
import org.opendma.api.collections.FloatList;
import org.opendma.api.collections.IntegerList;
import org.opendma.api.collections.LongList;
import org.opendma.api.collections.OdmaContentList;
import org.opendma.api.collections.OdmaObjectEnumeration;
import org.opendma.api.collections.ShortList;
import org.opendma.api.collections.StringList;
import org.opendma.exceptions.OdmaInvalidDataTypeException;

/**
 * A <i>Property</i> of an <code>OdmaObject</code> in the OpenDMA
 * architecture. It is always bound in name, data type and cardinality and can
 * not change these. If this property is not read only (i.e.
 * <code>{@link #isReadOnly()}</code> returns <code>false</code>), it can
 * change its value by calling the <code>{@link #setValue(Object)}</code>
 * method. Changes are only persisted in the repository after a call to the
 * save() mathod of the containing object.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public interface OdmaProperty
{

    /**
     * Returns the qualified name of this property.
     * 
     * @return the qualified name of this property.
     */
    public OdmaQName getName();

    /**
     * Returns the value of this property.<br>
     * The concrete <code>Object</code> returned by this method depends on the
     * data type of this property.
     * 
     * @return the value of this property.
     */
    public Object getValue();

    /**
     * Returns the numeric identifier of the data type of this property.<br>
     * You can find a list of all data types in the <code>OdmaTypes</code>
     * class.
     * 
     * @return the numeric identifier of the data type of this property.
     * 
     * @see org.opendma.OdmaTypes
     */
    public int getType();

    /**
     * Set the value of this property to the given new value. The
     * <code>Class</code> of the given <code>Object</code> has to match the
     * data type of this property.
     * 
     * @param newValue
     *            the new value to set this property to.
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if the Class of the given Object does not match
     *             the data type of this property
     */
    public void setValue(Object newValue) throws OdmaInvalidDataTypeException;

    /**
     * Returns true if and only if this property is a multio-value property.
     * 
     * @return true if and only if this property is a multio-value property.
     */
    public boolean isMultiValue();

    /**
     * Returns true if and only if this property has been changed and these
     * changes have not yet been saved.
     * 
     * @return true true if and only if this property has been changed and these
     *         changes have not yet been saved
     */
    public boolean isDirty();

    /**
     * Returns true if and only if this property must not be changed.
     * 
     * @return true if and only if this property must not be changed.
     */
    public boolean isReadOnly();

    /**
     * Returns the <code>String</code> value of this property if and only if
     * the data type of this property is a single valued <i>String</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>String</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>String</i>
     *             property
     */
    public String getString() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Integer</code> value of this property if and only if
     * the data type of this property is a single valued <i>Integer</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Integer</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued
     *             <i>Integer</i> property
     */
    public Integer getInteger() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Short</code> value of this property if and only if
     * the data type of this property is a single valued <i>Short</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Short</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>Short</i>
     *             property
     */
    public Short getShort() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Long</code> value of this property if and only if the
     * data type of this property is a single valued <i>Long</i>. Throws an
     * <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Long</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>Long</i>
     *             property
     */
    public Long getLong() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Float</code> value of this property if and only if
     * the data type of this property is a single valued <i>Float</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Float</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>Float</i>
     *             property
     */
    public Float getFloat() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Double</code> value of this property if and only if
     * the data type of this property is a single valued <i>Double</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Double</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>Double</i>
     *             property
     */
    public Double getDouble() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Boolean</code> value of this property if and only if
     * the data type of this property is a single valued <i>Boolean</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Boolean</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued
     *             <i>Boolean</i> property
     */
    public Boolean getBoolean() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>Date</code> value of this property if and only if the
     * data type of this property is a single valued <i>DateTime</i>. Throws an
     * <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>Date</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued
     *             <i>DateTime</i> property
     */
    public Date getDateTime() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>byte[]</code> value of this property if and only if
     * the data type of this property is a single valued <i>BLOB</i>. Throws an
     * <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>byte[]</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued <i>BLOB</i>
     *             property
     */
    public byte[] getBlob() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>OdmaObject</code> value of this property if and only
     * if the data type of this property is a single valued <i>Reference</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>OdmaObject</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued
     *             <i>Reference</i> property
     */
    public OdmaObject getObject() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>OdmaContent</code> value of this property if and only
     * if the data type of this property is a single valued <i>Content</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>OdmaContent</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a single valued
     *             <i>Content</i> property
     */
    public OdmaContent getContent() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>StringList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>String</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>StringList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>String</i>
     *             property
     */
    public StringList getStringList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>IntegerList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>Integer</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>IntegerList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Integer</i>
     *             property
     */
    public IntegerList getIntegerList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>ShortList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>Short</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>ShortList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Short</i>
     *             property
     */
    public ShortList getShortList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>LongList</code> value of this property if and only if
     * the data type of this property is a multi valued <i>Long</i>. Throws an
     * <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>LongList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Long</i>
     *             property
     */
    public LongList getLongList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>FloatList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>Float</i>. Throws
     * an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>FloatList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Float</i>
     *             property
     */
    public FloatList getFloatList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>DoubleList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>Double</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>DoubleList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Double</i>
     *             property
     */
    public DoubleList getDoubleList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>BooleanList</code> value of this property if and only
     * if the data type of this property is a multi valued <i>Boolean</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>BooleanList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Boolean</i>
     *             property
     */
    public BooleanList getBooleanList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>DateTimeList</code> value of this property if and
     * only if the data type of this property is a multi valued <i>DateTime</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>DateTimeList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued
     *             <i>DateTime</i> property
     */
    public DateTimeList getDateTimeList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>BlobList</code> value of this property if and only if
     * the data type of this property is a multi valued <i>BLOB</i>. Throws an
     * <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>BlobList</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>BLOB</i>
     *             property
     */
    public BlobList getBlobList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>OdmaObjectEnumeration</code> value of this property
     * if and only if the data type of this property is a multi valued
     * <i>Reference</i>. Throws an <code>OdmaInvalidDataTypeException</code>
     * otherwise,
     * 
     * @return the <code>OdmaObjectEnumeration</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued
     *             <i>Reference</i> property
     */
    public OdmaObjectEnumeration getObjectList() throws OdmaInvalidDataTypeException;

    /**
     * Returns the <code>OdmaContentList</code> value of this property if and
     * only if the data type of this property is a multi valued <i>Content</i>.
     * Throws an <code>OdmaInvalidDataTypeException</code> otherwise,
     * 
     * @return the <code>OdmaContent</code> value of this property
     * 
     * @throws OdmaInvalidDataTypeException
     *             if and only if this property is not a multi valued <i>Content</i>
     *             property
     */
    public OdmaContentList getContentList() throws OdmaInvalidDataTypeException;

}