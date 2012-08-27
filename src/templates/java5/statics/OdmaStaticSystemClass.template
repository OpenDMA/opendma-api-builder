package org.opendma.impl.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaType;
import org.opendma.exceptions.OdmaAccessDeniedException;
import org.opendma.exceptions.OdmaInvalidDataTypeException;
import org.opendma.exceptions.OdmaObjectNotFoundException;
import org.opendma.exceptions.OdmaRuntimeException;
import org.opendma.impl.OdmaPropertyImpl;

/**
 * Template implementation of the interface <code>{@link OdmaClass}</code>.<p>
 *
 * The <i>Class</i> specific version of the <code>{@link OdmaObject}</code> interface that offers short cuts to all defined OpenDMA properties.
 *
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public abstract class OdmaStaticSystemClass extends OdmaStaticSystemObject implements OdmaClass
{

    protected OdmaStaticSystemClass(OdmaStaticSystemClass parent, Iterable<OdmaClass> subClasses) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        properties.put(OdmaCommonNames.PROPERTY_SUBCLASSES,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_SUBCLASSES,subClasses,OdmaType.REFERENCE,true,true));
    }

    protected void buildProperties() throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        ArrayList<OdmaPropertyInfo> props = new ArrayList<OdmaPropertyInfo>();
        if(getParent() != null)
        {
            Iterable<OdmaPropertyInfo> parentProps = getParent().getProperties();
            Iterator<OdmaPropertyInfo> itParentProps = parentProps.iterator();
            while(itParentProps.hasNext())
            {
                props.add(itParentProps.next());
            }
        }
        Iterator<OdmaPropertyInfo> itDeclaredProperties = getDeclaredProperties().iterator();
        while(itDeclaredProperties.hasNext())
        {
            props.add(itDeclaredProperties.next());
        }
        properties.put(OdmaCommonNames.PROPERTY_PROPERTIES,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_PROPERTIES,props,OdmaType.REFERENCE,true,true));
    }

    // =============================================================================================
    // Object specific property access
    // =============================================================================================

    // CHECKTEMPLATE: the following code has most likely been copied from a class template. Make sure to keep this code up to date!
    // The following template code is available as OdmaClassTemplate

    /**
     * Returns the internal (technical) <i>name</i> of this <code>Class</code>.<br>
     * 
     * <p>Property <b>Name</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the internal (technical) <i>name</i> of this <code>Class</code>
     */
    public String getName()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_NAME).getString();
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
     * Sets the internal (technical) <i>name</i> of this <code>Class</code>.<br>
     * 
     * <p>Property <b>Name</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setName(String value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_NAME).setValue(value);
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
     * Returns the name <i>qualifier</i> of this <code>Class</code>.<br>
     * 
     * <p>Property <b>NameQualifier</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the name <i>qualifier</i> of this <code>Class</code>
     */
    public String getNameQualifier()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_NAMEQUALIFIER).getString();
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
     * Sets the name <i>qualifier</i> of this <code>Class</code>.<br>
     * 
     * <p>Property <b>NameQualifier</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setNameQualifier(String value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_NAMEQUALIFIER).setValue(value);
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
     * Returns the <i>display name</i> of this <code>Class</code> to be displayed to end users.<br>
     * 
     * <p>Property <b>DisplayName</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the <i>display name</i> of this <code>Class</code> to be displayed to end users
     */
    public String getDisplayName()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_DISPLAYNAME).getString();
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
     * Sets the <i>display name</i> of this <code>Class</code> to be displayed to end users.<br>
     * 
     * <p>Property <b>DisplayName</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setDisplayName(String value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_DISPLAYNAME).setValue(value);
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
     * Returns the <i>parent</i> <code>Class</code> that is extended by this <code>Class</code>.<br>
     * 
     * <p>Property <b>Parent</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [SingleValue] [ReadOnly] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the <i>parent</i> <code>Class</code> that is extended by this <code>Class</code>
     */
    public OdmaClass getParent()
    {
        try
        {
            return (OdmaClass)getProperty(OdmaCommonNames.PROPERTY_PARENT).getReference();
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
     * Returns the list of <i>aspects</i> that are implemented by this <code>Class</code>.<br>
     * 
     * <p>Property <b>Aspects</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [MultiValue] [Writable] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the list of <i>aspects</i> that are implemented by this <code>Class</code>
     */
    @SuppressWarnings("unchecked")
    public Iterable<OdmaClass> getAspects()
    {
        try
        {
            return (Iterable<OdmaClass>)getProperty(OdmaCommonNames.PROPERTY_ASPECTS).getReferenceIterable();
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
     * Returns the list of <i>properties</i> that are desclared by this <code>Class</code> (does not contain inherited properties)..<br>
     * 
     * <p>Property <b>DeclaredProperties</b> (opendma): <b>Reference to PropertyInfo (opendma)</b><br>
     * [MultiValue] [Writable] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the list of <i>properties</i> that are desclared by this <code>Class</code> (does not contain inherited properties).
     */
    @SuppressWarnings("unchecked")
    public Iterable<OdmaPropertyInfo> getDeclaredProperties()
    {
        try
        {
            return (Iterable<OdmaPropertyInfo>)getProperty(OdmaCommonNames.PROPERTY_DECLAREDPROPERTIES).getReferenceIterable();
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
     * Returns the list of <i>properties</i> that are effective for objects of this <code>Class</code>. Contains inherited and declared properties..<br>
     * 
     * <p>Property <b>Properties</b> (opendma): <b>Reference to PropertyInfo (opendma)</b><br>
     * [MultiValue] [ReadOnly] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the list of <i>properties</i> that are effective for objects of this <code>Class</code>. Contains inherited and declared properties.
     */
    @SuppressWarnings("unchecked")
    public Iterable<OdmaPropertyInfo> getProperties()
    {
        try
        {
            return (Iterable<OdmaPropertyInfo>)getProperty(OdmaCommonNames.PROPERTY_PROPERTIES).getReferenceIterable();
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
     * Returns whether this <code>Class</code> describes an Aspect or a valid class object.<br>
     * 
     * <p>Property <b>Aspect</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether this <code>Class</code> describes an Aspect or a valid class object
     */
    public Boolean getAspect()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_ASPECT).getBoolean();
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
     * Returns whether <code>Object</code>s of this <code>Class</code> can be created or not.<br>
     * 
     * <p>Property <b>Instantiable</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether <code>Object</code>s of this <code>Class</code> can be created or not
     */
    public Boolean getInstantiable()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_INSTANTIABLE).getBoolean();
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
     * Sets whether <code>Object</code>s of this <code>Class</code> can be created or not.<br>
     * 
     * <p>Property <b>Instantiable</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setInstantiable(Boolean value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_INSTANTIABLE).setValue(value);
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
     * Returns whether this <code>Class</code> should be displayed to end users or not.<br>
     * 
     * <p>Property <b>Hidden</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether this <code>Class</code> should be displayed to end users or not
     */
    public Boolean getHidden()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_HIDDEN).getBoolean();
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
     * Sets whether this <code>Class</code> should be displayed to end users or not.<br>
     * 
     * <p>Property <b>Hidden</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setHidden(Boolean value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_HIDDEN).setValue(value);
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
     * Returns whether this <code>Class</code> is defined by the system (true) or by users (false).<br>
     * 
     * <p>Property <b>System</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether this <code>Class</code> is defined by the system (true) or by users (false)
     */
    public Boolean getSystem()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_SYSTEM).getBoolean();
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
     * Sets whether this <code>Class</code> is defined by the system (true) or by users (false).<br>
     * 
     * <p>Property <b>System</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @throws OdmaAccessDeniedException
     *             if this property can not be set by the current user
     */
    public void setSystem(Boolean value) throws OdmaAccessDeniedException
    {
        try
        {
            getProperty(OdmaCommonNames.PROPERTY_SYSTEM).setValue(value);
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
     * Returns whether objects of this class can be retrieved from a session by their id or not.<br>
     * 
     * <p>Property <b>Retrievable</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether objects of this class can be retrieved from a session by their id or not
     */
    public Boolean getRetrievable()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_RETRIEVABLE).getBoolean();
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
     * Returns whether objects of this class can be found by a search query or not.<br>
     * 
     * <p>Property <b>Searchable</b> (opendma): <b>Boolean</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return whether objects of this class can be found by a search query or not
     */
    public Boolean getSearchable()
    {
        try
        {
            return getProperty(OdmaCommonNames.PROPERTY_SEARCHABLE).getBoolean();
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
     * Returns the list of <code>Class</code>es that extend this class (i.e. that contain a reference to this <code>Class</code> in their <i>parent</i> property).<br>
     * 
     * <p>Property <b>SubClasses</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [MultiValue] [ReadOnly] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the list of <code>Class</code>es that extend this class (i.e. that contain a reference to this <code>Class</code> in their <i>parent</i> property)
     */
    @SuppressWarnings("unchecked")
    public Iterable<OdmaClass> getSubClasses()
    {
        try
        {
            return (Iterable<OdmaClass>)getProperty(OdmaCommonNames.PROPERTY_SUBCLASSES).getReferenceIterable();
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
     * the qualified name of this <code>Class</code><br>
     * <p>Full description follows.</p>
     * 
     * @return the qualified name of this <code>Class</code>
     */
    public OdmaQName getQName()
    {
        return new OdmaQName(getNameQualifier(),getName());
    }

}