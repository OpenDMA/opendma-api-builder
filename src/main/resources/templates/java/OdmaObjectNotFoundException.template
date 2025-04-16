package org.opendma.exceptions;

import org.opendma.api.OdmaGuid;

/**
 * Thrown when an OpenDMA implementation is unable to locate the requested object.
 */
public class OdmaObjectNotFoundException extends OdmaException {

    private static final long serialVersionUID = 3730730647471169915L;

    private final OdmaGuid objectGuid;

    /**
     * Constructs a new OdmaObjectNotFoundException with a provided GUID.
     *
     * @param objectGuid the unique identifier of the object that was not found.
     */
    public OdmaObjectNotFoundException(OdmaGuid objectGuid) {
        super(objectGuid != null ? "Object not found: "+objectGuid.toString() : "Object not found");
        this.objectGuid = objectGuid;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified detail message and GUID.
     *
     * @param message the detail message.
     * @param objectGuid the unique identifier of the object that was not found.
     */
    public OdmaObjectNotFoundException(String message, OdmaGuid objectGuid) {
        super(message);
        this.objectGuid = objectGuid;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified detail message, cause and GUID.
     *
     * @param message the detail message.
     * @param cause the cause.
     * @param objectGuid the unique identifier of the object that was not found.
     */
    public OdmaObjectNotFoundException(String message, Throwable cause, OdmaGuid objectGuid) {
        super(message, cause);
        this.objectGuid = objectGuid;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified cause and GUID.
     *
     * @param cause the cause.
     * @param guid the unique identifier of the object that was not found.
     */
    public OdmaObjectNotFoundException(Throwable cause, OdmaGuid objectGuid) {
        super(cause);
        this.objectGuid = objectGuid;
    }

    /**
     * Returns the unique identifier (GUID) of the object that was not found.
     *
     * @return the GUID of the missing object.
     */
    public OdmaGuid getObjectGuid() {
        return objectGuid;
    }

}
