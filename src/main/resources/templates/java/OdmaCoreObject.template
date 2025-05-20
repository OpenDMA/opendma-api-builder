package org.opendma.api;

import java.util.Iterator;

import org.opendma.exceptions.OdmaAccessDeniedException;
import org.opendma.exceptions.OdmaInvalidDataTypeException;
import org.opendma.exceptions.OdmaPropertyNotFoundException;

/**
 * Generic capabilities of an object in OpenDMA. Do not use directly. Instead, use <code>{@link OdmaObject}</code>.
 */
public interface OdmaCoreObject {

    /**
     * Returns an <code>{@link OdmaProperty}</code> for the property identified by the given qualified name.
     * The named property is automatically retrieved from the server if it is not yet in the local cache.
     * To optimize performance, consider calling <code>{@link #prepareProperties(OdmaQName[], boolean)}</code>
     * first when accessing multiple properties.
     * 
     * @param propertyName
     *            the qualified name of the property to return
     * 
     * @return an <code>{@link OdmaProperty}</code> for the property identified by the given qualified name.
     * 
     * @throws OdmaPropertyNotFoundException
     *             Thrown if the given qualified name does not identify a property in the effective properties of the class of this object.
     */
    OdmaProperty getProperty(OdmaQName propertyName) throws OdmaPropertyNotFoundException;

    /**
     * Checks if the specified properties are already in the local cache and retrieves them from the server if not.
     * If <code>refresh</code> is set to <code>true</code>, all specified properties are always retrieved from the server.
     * Such a refresh will reset unsaved changes of properties to the latest state on the server.
     * If a given qualified name does not identify a property, it is silently ignored.
     * 
     * @param propertyNames
     *            An array of qualified property names to retrieve or <code>null</code> to retrieve all properties.
     * 
     * @param refresh
     *            Indicates whether properties should be refreshed even if they are in the local cache.
     */
    void prepareProperties(OdmaQName[] propertyNames, boolean refresh);

    /**
     * Sets the specified property to a new value.<br>
     * This is a shortcut for <code>getProperty(propertyName).setValue(newValue)</code>. It avoids the retrieval of the property
     * in the <code>getProperty(propertyName)</code> method if the property is not yet in the local cache.
     * 
     * @param propertyName
     *            The qualified name of the property to be changed
     * @param newValue
     *            The new value for the property
     * 
     * @throws OdmaPropertyNotFoundException
     *             Thrown if the given qualified name does not identify a property in the effective properties of this object's class.
     * @throws OdmaInvalidDataTypeException
     *             Thrown if the type of <code>newValue</code> does not match the property's data type.
     * @throws OdmaAccessDeniedException
     *             Thrown if the current user does not have permission to modify the property.
     */
    void setProperty(OdmaQName propertyName, Object newValue) throws OdmaPropertyNotFoundException, OdmaInvalidDataTypeException, OdmaAccessDeniedException;

    /**
     * Checks if there are pending changes to properties that have not been persisted to the server.
     * 
     * @return <code>true<code> if there are pending changes that have not yet been persisted, <code>false<code> otherwise
     */
    boolean isDirty();

    /**
     * Persists the current pending changes to properties at the server.
     */
    void save();

    /**
     * Determines whether this object's class or one of its ancestors matches or incorporates the specified class or aspect.
     * 
     * @param classOrAspectName
     *             The qualified name of the class or aspect to test.
     * 
     * @return <code>true</code> if the class matches or incorporates the specified aspect, <code>false</code> otherwise.
     */
    boolean instanceOf(OdmaQName classOrAspectName);
    
    // ----- EXPERIMENTAL: performance optimization framework

    /**
     * Provides a list of properties that are immediately available at low cost, e.g. without a request to a back-end system.
     * This helps to optimize the communication across media boundaries like a network communication. Can return <code>null</code>
     * if this object does not want to or simply cannot help to optimize performance. In case there are no immediately
     * available properties and this object wants to force only rudimentary stub objects across media boundaries, it returns
     * an empty list. If name, type, cardinality and readability of a property is known immediately but not the value, it
     * is recommended to return an unresolved OdmaProperty.
     * 
     * This method is part of the experimental performance optimization framework.
     * 
     * @return Iterator over immediately available OdmaProperty objects, <code>null</code> to indicate the performance
     *         optimization is not supported.
     */
    Iterator<OdmaProperty> availableProperties();
    
    /**
     * Indicates if the list of properties returned by <code>availableProperties()</code> is complete, i.e. if it contains
     * all the properties defined for objects of this class.
     * 
     * This method is part of the experimental performance optimization framework.
     * 
     * @return <code>true</code> if the list of properties returned by <code>availableProperties()</code> is complete, <code>false</code> if unknown or incomplete.
     */
    boolean availablePropertiesComplete();
    
    /**
     * Indicates if it is recommended to embed this entire object within the referencing object in communications across
     * media boundaries like a network communication.
     * 
     * This method is part of the experimental performance optimization framework.
     * 
     * @return <code>true</code> if the list of properties returned by <code>availableProperties()</code> is complete, <code>false</code> if unknown or incomplete.
     */
    boolean isEmbeddingRecommended();

}
