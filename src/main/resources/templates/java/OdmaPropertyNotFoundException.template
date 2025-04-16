package org.opendma.exceptions;

import org.opendma.api.OdmaQName;

/**
 * Thrown when an OpenDMA implementation is unable to locate the requested property.
 */
public class OdmaPropertyNotFoundException extends OdmaException {

    private static final long serialVersionUID = 4599466174108144058L;

    private final OdmaQName propertyName;

    /**
     * Constructs a new OdmaPropertyNotFoundException with the specified property name.
     *
     * @param propertyName the qualified name of the property that was not found.
     */
    public OdmaPropertyNotFoundException(OdmaQName propertyName) {
        super(propertyName != null ? "Property not found: "+propertyName.toString() : "Property not found");
        this.propertyName = propertyName;
    }

    /**
     * Constructs a new OdmaPropertyNotFoundException with the specified detail message and property name.
     *
     * @param message the detail message.
     * @param propertyName the qualified name of the property that was not found.
     */
    public OdmaPropertyNotFoundException(String message, OdmaQName propertyName) {
        super(message);
        this.propertyName = propertyName;
    }

    /**
     * Constructs a new OdmaPropertyNotFoundException with the specified detail message, cause and property name.
     *
     * @param message the detail message.
     * @param cause the cause.
     * @param propertyName the qualified name of the property that was not found.
     */
    public OdmaPropertyNotFoundException(String message, Throwable cause, OdmaQName propertyName) {
        super(message, cause);
        this.propertyName = propertyName;
    }

    /**
     * Constructs a new OdmaPropertyNotFoundException with the specified cause and QName.
     *
     * @param cause the cause.
     * @param propertyName the qualified name of the property that was not found.
     */
    public OdmaPropertyNotFoundException(Throwable cause, OdmaQName propertyName) {
        super(cause);
        this.propertyName = propertyName;
    }

    /**
     * Returns the qualified name of the property that was not found.
     *
     * @return the qualified name of the missing property.
     */
    public OdmaQName getPropertyName() {
        return propertyName;
    }

}
