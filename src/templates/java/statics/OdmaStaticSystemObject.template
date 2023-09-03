package org.opendma.impl.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaType;
import org.opendma.exceptions.OdmaAccessDeniedException;
import org.opendma.exceptions.OdmaInvalidDataTypeException;
import org.opendma.exceptions.OdmaObjectNotFoundException;
import org.opendma.exceptions.OdmaRuntimeException;
import org.opendma.impl.OdmaPropertyImpl;

/**
 * Template implementation of the interface <code>{@link OdmaObject}</code>.<p>
 *
 * Full description follows.
 *
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaStaticSystemObject
{

    protected Map<OdmaQName, OdmaProperty> properties = new HashMap<OdmaQName, OdmaProperty>();

    protected OdmaStaticSystemObject()
    {
        // properties of opendma.org Object
        properties.put(OdmaCommonNames.PROPERTY_CLASS,null);
        properties.put(OdmaCommonNames.PROPERTY_ID,null);
        properties.put(OdmaCommonNames.PROPERTY_GUID,null);
        properties.put(OdmaCommonNames.PROPERTY_REPOSITORY,null);
    }

    protected void patchClass(OdmaClass newClass) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        properties.put(OdmaCommonNames.PROPERTY_CLASS,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_CLASS,newClass,OdmaType.REFERENCE,false,true));
    }

    protected void patchIds(OdmaId newId, OdmaGuid newGuid) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        properties.put(OdmaCommonNames.PROPERTY_ID,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_ID,newId,OdmaType.ID,false,true));
        properties.put(OdmaCommonNames.PROPERTY_GUID,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_GUID,newGuid,OdmaType.GUID,false,true));
    }

    protected void patchRepository(OdmaRepository newRepository) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        properties.put(OdmaCommonNames.PROPERTY_REPOSITORY,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_REPOSITORY,newRepository,OdmaType.REFERENCE,false,true));
    }

    // =============================================================================================
    // Generic property access
    // =============================================================================================

    /**
     * Returns an <code>{@link OdmaProperty}</code> for the property identified by the given qualified name. The named
     * property is automatically retrieved from the server if it is not yet in the local cache. So it is wise to call
     * the method <code>{@link #prepareProperties(OdmaQName[], boolean)}</code> at first if you are interested in
     * multiple properties to reduce the number of round trips to the server.
     *
     * @param propertyName
     *            the qualified name of the property to return
     *
     * @return an <code>{@link OdmaProperty}</code> for the property identified by the given qualified name.
     *
     * @throws OdmaObjectNotFoundException
     *             if and only if the given qualified name does not identify a property in the list of effective
     *             properties of the class of this object
     */
    public OdmaProperty getProperty(OdmaQName propertyName) throws OdmaObjectNotFoundException
    {
        OdmaProperty p = (OdmaProperty)properties.get(propertyName);
        if(p == null)
            throw new OdmaObjectNotFoundException(propertyName);
        return p;
    }

    /**
     * Checks if the named properties are already in the local cache and performs one single round trip to the server to
     * retrieve all properties that are not yet in the cache. If the flag <code>refresh</code> is set to
     * <code>true</code>, the proerties are always retrieved from the server. Such a refresh will reset unsaved
     * changes of properties to the current value.<br>
     * If a given qualified name does not identify a property, it is silently ignored.
     *
     * @param propertyNames
     *            array of qualified property names to retrieve from the server or <code>null</code> to retrieve all
     *
     * @param refresh
     *            flag to indicate if the properties should also be retrieved if they are already in the local cache.
     */
    public void prepareProperties(OdmaQName[] propertyNames, boolean refresh)
    {
        // do nothing here. We are static and all static properties are already in the Map
    }

    /**
     * Set the property identified by the given qualified name to the new value.<br>
     * This method is a shortcut for <code>getProperty(propertyName).setValue(newValue)</code> that can avoid the
     * retrieval of the property in the <code>getProperty(propertyName)</code> method if the property is not yet in
     * the local cache.
     *
     * @param propertyName
     *            the qualified name of the property to be changed
     * @param newValue
     *            the new value to set the named property to
     *
     * @throws OdmaObjectNotFoundException
     *             if and only if the given qualified name does not identify a property in the list of effective
     *             properties of the class of this object
     * @throws OdmaInvalidDataTypeException
     *             if and only if the Class of the given Object does not match the data type of the named property
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setProperty(OdmaQName propertyName, Object newValue) throws OdmaObjectNotFoundException, OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        // we are static. properties can not be changed.
        throw new OdmaAccessDeniedException();
    }

    /**
     * Returns true if there are some pending changes to properties that have not yet been persisted to the server.
     *
     * @return true if there are pending changes that have not yet been persisted
     */
    public boolean isDirty()
    {
        // we are static. properties can not be changed.
        return false;
    }

    /**
     * Persist the current pending changes to properties at the server.
     */
    public void save()
    {
        // we are not dirty, and will never be. Nothing to do here.
    }

    /**
     * Returns <code>true</code> if and only if the class of this object or one of its ancestors equals
     * the given name or the class of this object or one of its ancestors incorporates the aspect with
     * the given name.
     * 
     * @param classOrAspectName
     *             the qualified name of the class or aspect to test for
     * 
     * @return if the class of this object is or extends the given class or incorportes the given aspect
     */
    public boolean instanceOf(OdmaQName classOrAspectName)
    {
        OdmaClass test = getOdmaClass();
        while(test != null)
        {
            if(test.getQName().equals(classOrAspectName))
            {
                return true;
            }
            Iterable<OdmaClass> aspects = test.getAspects();
            if(aspects != null)
            {
                Iterator<OdmaClass> itAspects = aspects.iterator();
                while(itAspects.hasNext())
                {
                    OdmaClass declaredAspect = (OdmaClass)itAspects.next();
                    if(declaredAspect.getQName().equals(classOrAspectName))
                    {
                        return true;
                    }
                }
            }
            test = test.getParent();
        }
        return false;
    }

    // =============================================================================================
    // Object specific property access
    // =============================================================================================

    // CHECKTEMPLATE: the following code has most likely been copied from a class template. Make sure to keep this code up to date!
    // The following template code is available as OdmaObjectTemplate

    /**
     * Returns the OpenDMA <code>Class</code> describing this <code>Object</code>.<br>
     * 
     * <p>Property <b>Class</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the OpenDMA <code>Class</code> describing this <code>Object</code>
     */
    public OdmaClass getOdmaClass()
    {
        try
        {
            return (OdmaClass)getProperty(OdmaCommonNames.PROPERTY_CLASS).getReference();
        }
        catch(ClassCastException cce)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",cce);
        }
        catch(OdmaInvalidDataTypeException oidte)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",oidte);
        }
        catch(OdmaObjectNotFoundException oonfe)
        {
            throw new OdmaRuntimeException("Predefined system property missing",oonfe);
        }
    }

    /**
     * Returns the <i>unique object identifier</i> identifying this <code>Object</code> inside its <code>Repository</code>.<br>
     * 
     * <p>Property <b>Id</b> (opendma): <b>String</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the <i>unique object identifier</i> identifying this <code>Object</code> inside its <code>Repository</code>
     */
    public OdmaId getId()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_ID).getId();
        }
        catch(OdmaInvalidDataTypeException oidte)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",oidte);
        }
        catch(OdmaObjectNotFoundException oonfe)
        {
            throw new OdmaRuntimeException("Predefined system property missing",oonfe);
        }
    }

    /**
     * Returns the <i>global unique object identifier</i> globally identifying this <code>Object</code>.<br>
     * 
     * <p>Property <b>Guid</b> (opendma): <b>String</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the <i>global unique object identifier</i> globally identifying this <code>Object</code>
     */
    public OdmaGuid getGuid()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_GUID).getGuid();
        }
        catch(OdmaInvalidDataTypeException oidte)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",oidte);
        }
        catch(OdmaObjectNotFoundException oonfe)
        {
            throw new OdmaRuntimeException("Predefined system property missing",oonfe);
        }
    }

    /**
     * Returns the OpenDMA <code>Repository</code> where this <code>Object</code> resides.<br>
     * 
     * <p>Property <b>Repository</b> (opendma): <b>Reference to Repository (opendma)</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the OpenDMA <code>Repository</code> where this <code>Object</code> resides
     */
    public OdmaRepository getRepository()
    {
        try
        {
            return (OdmaRepository)getProperty(OdmaCommonNames.PROPERTY_REPOSITORY).getReference();
        }
        catch(ClassCastException cce)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",cce);
        }
        catch(OdmaInvalidDataTypeException oidte)
        {
            throw new OdmaRuntimeException("Invalid data type of system property",oidte);
        }
        catch(OdmaObjectNotFoundException oonfe)
        {
            throw new OdmaRuntimeException("Predefined system property missing",oonfe);
        }
    }

}