package org.opendma.exceptions;

import org.opendma.OdmaTypes;

/**
 * Exception thrown whenever the data type accessed by the user does not match
 * the real data type in OpenDMA.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaInvalidDataTypeException extends OdmaException
{

    /** the serial version ID required for serialization */
    private static final long serialVersionUID = 5012000289312853013L;

    /** the data type expected by the user */
    private int expectedDataType;

    /** the multi-value / single-value property expected by the user */
    private boolean expectedMultivalue;

    /**
     * wheather this Exception carries information about the real data type that
     * is present in OpenDMA or not
     */
    private boolean hasFound;

    /** the real data type that is present in OpenDMA */
    private int foundDataType;

    /** the real multi-value / single-value property that is present in OpenDMA */
    private boolean foundMultivalue;

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     */
    public OdmaInvalidDataTypeException(int expectedDataType, boolean expectedMultivalue)
    {
        super(getDefaultMessage(expectedDataType, expectedMultivalue));
        this.hasFound = false;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param msg
     *            a message string for the user
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     */
    public OdmaInvalidDataTypeException(String msg, int expectedDataType, boolean expectedMultivalue)
    {
        super(msg);
        this.hasFound = false;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param msg
     *            a message string for the user
     * @param t
     *            the <code>Throwable</code> that caused this Exception
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     */
    public OdmaInvalidDataTypeException(String msg, Throwable t, int expectedDataType, boolean expectedMultivalue)
    {
        super(msg, t);
        this.hasFound = false;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param t
     *            the <code>Throwable</code> that caused this Exception
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     */
    public OdmaInvalidDataTypeException(Throwable t, int expectedDataType, boolean expectedMultivalue)
    {
        super(getDefaultMessage(expectedDataType, expectedMultivalue), t);
        this.hasFound = false;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @param foundDataType
     * @param foundMultivalue
     */
    public OdmaInvalidDataTypeException(int expectedDataType, boolean expectedMultivalue, int foundDataType, boolean foundMultivalue)
    {
        super(getDefaultMessage(expectedDataType, expectedMultivalue, foundDataType, foundMultivalue));
        this.hasFound = true;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
        this.foundDataType = foundDataType;
        this.foundMultivalue = foundMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param msg
     *            a message string for the user
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @param foundDataType
     * @param foundMultivalue
     */
    public OdmaInvalidDataTypeException(String msg, int expectedDataType, boolean expectedMultivalue, int foundDataType, boolean foundMultivalue)
    {
        super(msg);
        this.hasFound = true;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
        this.foundDataType = foundDataType;
        this.foundMultivalue = foundMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param msg
     *            a message string for the user
     * @param t
     *            the <code>Throwable</code> that caused this Exception
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @param foundDataType
     * @param foundMultivalue
     */
    public OdmaInvalidDataTypeException(String msg, Throwable t, int expectedDataType, boolean expectedMultivalue, int foundDataType, boolean foundMultivalue)
    {
        super(msg, t);
        this.hasFound = true;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
        this.foundDataType = foundDataType;
        this.foundMultivalue = foundMultivalue;
    }

    /**
     * Create a new <code>OdmaInvalidDataTypeException</code> with the given
     * parameters.
     * 
     * @param t
     *            the <code>Throwable</code> that caused this Exception
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @param foundDataType
     * @param foundMultivalue
     */
    public OdmaInvalidDataTypeException(Throwable t, int expectedDataType, boolean expectedMultivalue, int foundDataType, boolean foundMultivalue)
    {
        super(getDefaultMessage(expectedDataType, expectedMultivalue, foundDataType, foundMultivalue), t);
        this.hasFound = true;
        this.expectedDataType = expectedDataType;
        this.expectedMultivalue = expectedMultivalue;
        this.foundDataType = foundDataType;
        this.foundMultivalue = foundMultivalue;
    }

    /**
     * 
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @return x
     */
    private static String getDefaultMessage(int expectedDataType, boolean expectedMultivalue)
    {
        StringBuffer msgBuffer = new StringBuffer("Invalid data type. Expected ");
        if (expectedMultivalue)
        {
            msgBuffer.append("multi-valued ");
        }
        else
        {
            msgBuffer.append("single-valued ");
        }
        switch (expectedDataType)
        {
        case OdmaTypes.TYPE_STRING:
            msgBuffer.append("string");
            break;
        case OdmaTypes.TYPE_INTEGER:
            msgBuffer.append("integer");
            break;
        case OdmaTypes.TYPE_SHORT:
            msgBuffer.append("short");
            break;
        case OdmaTypes.TYPE_LONG:
            msgBuffer.append("long");
            break;
        case OdmaTypes.TYPE_FLOAT:
            msgBuffer.append("float");
            break;
        case OdmaTypes.TYPE_DOUBLE:
            msgBuffer.append("double");
            break;
        case OdmaTypes.TYPE_BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case OdmaTypes.TYPE_DATETIME:
            msgBuffer.append("datetime");
            break;
        case OdmaTypes.TYPE_BLOB:
            msgBuffer.append("blob");
            break;
        case OdmaTypes.TYPE_REFERENCE:
            msgBuffer.append("object");
            break;
        case OdmaTypes.TYPE_CONTENT:
            msgBuffer.append("content");
            break;
        case OdmaTypes.TYPE_ID:
            msgBuffer.append("id");
            break;
        case OdmaTypes.TYPE_GUID:
            msgBuffer.append("guid");
            break;
        default:
            msgBuffer.append("<unknown>");
            break;
        }
        return (msgBuffer.toString());
    }

    /**
     * 
     * @param expectedDataType
     *            the data type expected by the user
     * @param expectedMultivalue
     *            the multi-value / single-value property expected by the user
     * @param foundDataType
     * @param foundMultivalue
     * 
     * @return x
     */
    private static String getDefaultMessage(int expectedDataType, boolean expectedMultivalue, int foundDataType, boolean foundMultivalue)
    {
        StringBuffer msgBuffer = new StringBuffer("Invalid data type. Expected ");
        if (expectedMultivalue)
        {
            msgBuffer.append("multi-valued ");
        }
        else
        {
            msgBuffer.append("single-valued ");
        }
        switch (expectedDataType)
        {
        case OdmaTypes.TYPE_STRING:
            msgBuffer.append("string");
            break;
        case OdmaTypes.TYPE_INTEGER:
            msgBuffer.append("integer");
            break;
        case OdmaTypes.TYPE_SHORT:
            msgBuffer.append("short");
            break;
        case OdmaTypes.TYPE_LONG:
            msgBuffer.append("long");
            break;
        case OdmaTypes.TYPE_FLOAT:
            msgBuffer.append("float");
            break;
        case OdmaTypes.TYPE_DOUBLE:
            msgBuffer.append("double");
            break;
        case OdmaTypes.TYPE_BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case OdmaTypes.TYPE_DATETIME:
            msgBuffer.append("datetime");
            break;
        case OdmaTypes.TYPE_BLOB:
            msgBuffer.append("blob");
            break;
        case OdmaTypes.TYPE_REFERENCE:
            msgBuffer.append("object");
            break;
        case OdmaTypes.TYPE_CONTENT:
            msgBuffer.append("content");
            break;
        case OdmaTypes.TYPE_ID:
            msgBuffer.append("id");
            break;
        case OdmaTypes.TYPE_GUID:
            msgBuffer.append("guid");
            break;
        default:
            msgBuffer.append("<unknown>");
            break;
        }
        msgBuffer.append(" but found ");
        if (foundMultivalue)
        {
            msgBuffer.append("multi-valued ");
        }
        else
        {
            msgBuffer.append("single-valued ");
        }
        switch (foundDataType)
        {
        case OdmaTypes.TYPE_STRING:
            msgBuffer.append("string");
            break;
        case OdmaTypes.TYPE_INTEGER:
            msgBuffer.append("integer");
            break;
        case OdmaTypes.TYPE_SHORT:
            msgBuffer.append("short");
            break;
        case OdmaTypes.TYPE_LONG:
            msgBuffer.append("long");
            break;
        case OdmaTypes.TYPE_FLOAT:
            msgBuffer.append("float");
            break;
        case OdmaTypes.TYPE_DOUBLE:
            msgBuffer.append("double");
            break;
        case OdmaTypes.TYPE_BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case OdmaTypes.TYPE_DATETIME:
            msgBuffer.append("datetime");
            break;
        case OdmaTypes.TYPE_BLOB:
            msgBuffer.append("blob");
            break;
        case OdmaTypes.TYPE_REFERENCE:
            msgBuffer.append("object");
            break;
        case OdmaTypes.TYPE_CONTENT:
            msgBuffer.append("content");
            break;
        case OdmaTypes.TYPE_ID:
            msgBuffer.append("id");
            break;
        case OdmaTypes.TYPE_GUID:
            msgBuffer.append("guid");
            break;
        default:
            msgBuffer.append("<unknown>");
            break;
        }
        return (msgBuffer.toString());
    }

    /**
     * Returns the data type expected by the user.
     * 
     * @return the data type expected by the user
     */
    public int getExpectedDataType()
    {
        return (expectedDataType);
    }

    /**
     * Returns the multi-value / single-value property expected by the user.
     * 
     * @return the multi-value / single-value property expected by the user
     */
    public boolean isExpectedMultivalue()
    {
        return (expectedMultivalue);
    }

    /**
     * 
     * @return x
     */
    public boolean hasFoundDataType()
    {
        return (hasFound);
    }

    /**
     * 
     * @return x
     */
    public int getFoundDataType()
    {
        return (foundDataType);
    }

    /**
     * 
     * @return x
     */
    public boolean isFoundMultivalue()
    {
        return (foundMultivalue);
    }

}