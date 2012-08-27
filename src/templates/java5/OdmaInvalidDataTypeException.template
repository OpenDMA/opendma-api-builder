package org.opendma.exceptions;

import org.opendma.api.OdmaType;

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
    private OdmaType expectedDataType;

    /** the multi-value / single-value property expected by the user */
    private boolean expectedMultivalue;

    /**
     * whether this Exception carries information about the real data type that
     * is present in OpenDMA or not
     */
    private boolean hasFound;

    /** the real data type that is present in OpenDMA */
    private OdmaType foundDataType;

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
    public OdmaInvalidDataTypeException(OdmaType expectedDataType, boolean expectedMultivalue)
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
    public OdmaInvalidDataTypeException(String msg, OdmaType expectedDataType, boolean expectedMultivalue)
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
    public OdmaInvalidDataTypeException(String msg, Throwable t, OdmaType expectedDataType, boolean expectedMultivalue)
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
    public OdmaInvalidDataTypeException(Throwable t, OdmaType expectedDataType, boolean expectedMultivalue)
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
    public OdmaInvalidDataTypeException(OdmaType expectedDataType, boolean expectedMultivalue, OdmaType foundDataType, boolean foundMultivalue)
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
    public OdmaInvalidDataTypeException(String msg, OdmaType expectedDataType, boolean expectedMultivalue, OdmaType foundDataType, boolean foundMultivalue)
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
    public OdmaInvalidDataTypeException(String msg, Throwable t, OdmaType expectedDataType, boolean expectedMultivalue, OdmaType foundDataType, boolean foundMultivalue)
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
    public OdmaInvalidDataTypeException(Throwable t, OdmaType expectedDataType, boolean expectedMultivalue, OdmaType foundDataType, boolean foundMultivalue)
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
    private static String getDefaultMessage(OdmaType expectedDataType, boolean expectedMultivalue)
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
        case STRING:
            msgBuffer.append("string");
            break;
        case INTEGER:
            msgBuffer.append("integer");
            break;
        case SHORT:
            msgBuffer.append("short");
            break;
        case LONG:
            msgBuffer.append("long");
            break;
        case FLOAT:
            msgBuffer.append("float");
            break;
        case DOUBLE:
            msgBuffer.append("double");
            break;
        case BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case DATETIME:
            msgBuffer.append("datetime");
            break;
        case BLOB:
            msgBuffer.append("blob");
            break;
        case REFERENCE:
            msgBuffer.append("object");
            break;
        case CONTENT:
            msgBuffer.append("content");
            break;
        case ID:
            msgBuffer.append("id");
            break;
        case GUID:
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
    private static String getDefaultMessage(OdmaType expectedDataType, boolean expectedMultivalue, OdmaType foundDataType, boolean foundMultivalue)
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
        case STRING:
            msgBuffer.append("string");
            break;
        case INTEGER:
            msgBuffer.append("integer");
            break;
        case SHORT:
            msgBuffer.append("short");
            break;
        case LONG:
            msgBuffer.append("long");
            break;
        case FLOAT:
            msgBuffer.append("float");
            break;
        case DOUBLE:
            msgBuffer.append("double");
            break;
        case BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case DATETIME:
            msgBuffer.append("datetime");
            break;
        case BLOB:
            msgBuffer.append("blob");
            break;
        case REFERENCE:
            msgBuffer.append("object");
            break;
        case CONTENT:
            msgBuffer.append("content");
            break;
        case ID:
            msgBuffer.append("id");
            break;
        case GUID:
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
        case STRING:
            msgBuffer.append("string");
            break;
        case INTEGER:
            msgBuffer.append("integer");
            break;
        case SHORT:
            msgBuffer.append("short");
            break;
        case LONG:
            msgBuffer.append("long");
            break;
        case FLOAT:
            msgBuffer.append("float");
            break;
        case DOUBLE:
            msgBuffer.append("double");
            break;
        case BOOLEAN:
            msgBuffer.append("boolean");
            break;
        case DATETIME:
            msgBuffer.append("datetime");
            break;
        case BLOB:
            msgBuffer.append("blob");
            break;
        case REFERENCE:
            msgBuffer.append("object");
            break;
        case CONTENT:
            msgBuffer.append("content");
            break;
        case ID:
            msgBuffer.append("id");
            break;
        case GUID:
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
    public OdmaType getExpectedDataType()
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
    public OdmaType getFoundDataType()
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