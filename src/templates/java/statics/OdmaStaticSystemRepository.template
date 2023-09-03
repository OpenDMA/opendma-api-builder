package org.opendma.impl.core;

import java.util.Map;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;
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
 * Template implementation of the interface <code>{@link OdmaRepository}</code>.<p>
 * 
 * Full description follows.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public class OdmaStaticSystemRepository extends OdmaStaticSystemObject implements OdmaRepository
{

    protected OdmaStaticSystemRepository(Map<OdmaQName, OdmaProperty> externalProperties, String name, String displayName, OdmaId id, OdmaGuid guid, OdmaClass rootClass, Iterable<OdmaClass> rootAspects) throws OdmaInvalidDataTypeException, OdmaAccessDeniedException
    {
        properties = externalProperties;
        properties.put(OdmaCommonNames.PROPERTY_NAME,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_NAME,name,OdmaType.STRING,false,true));
        properties.put(OdmaCommonNames.PROPERTY_DISPLAYNAME,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_DISPLAYNAME,displayName,OdmaType.STRING,false,true));
        properties.put(OdmaCommonNames.PROPERTY_ROOTCLASS,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_ROOTCLASS,rootClass,OdmaType.REFERENCE,false,true));
        properties.put(OdmaCommonNames.PROPERTY_ROOTASPECTS,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_ROOTASPECTS,rootAspects,OdmaType.REFERENCE,true,true));
        properties.put(OdmaCommonNames.PROPERTY_ROOTFOLDER,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_ROOTFOLDER,null,OdmaType.REFERENCE,false,true));
        properties.put(OdmaCommonNames.PROPERTY_ID,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_ID,id,OdmaType.ID,false,true));
        properties.put(OdmaCommonNames.PROPERTY_GUID,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_GUID,guid,OdmaType.GUID,false,true));
        properties.put(OdmaCommonNames.PROPERTY_REPOSITORY,new OdmaPropertyImpl(OdmaCommonNames.PROPERTY_REPOSITORY,this,OdmaType.REFERENCE,false,true));
    }

    // =============================================================================================
    // Object specific property access
    // =============================================================================================

    // CHECKTEMPLATE: the following code has most likely been copied from a class template. Make sure to keep this code up to date!
    // The following template code is available as OdmaRepositoryTemplate

    /**
     * Returns the internal (technical) <i>name</i> of this <code>Repository</code>.<br>
     * 
     * <p>Property <b>Name</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the internal (technical) <i>name</i> of this <code>Repository</code>
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
     * Sets the internal (technical) <i>name</i> of this <code>Repository</code>.<br>
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
     * Returns the <i>display name</i> of this <code>Repository</code> to be displayed to end users.<br>
     * 
     * <p>Property <b>DisplayName</b> (opendma): <b>String</b><br>
     * [SingleValue] [Writable] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the <i>display name</i> of this <code>Repository</code> to be displayed to end users
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
     * Sets the <i>display name</i> of this <code>Repository</code> to be displayed to end users.<br>
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
     * Returns the <i>root</i> <code>Class</code> of the class hierarchy in this <code>Repository</code>.<br>
     * 
     * <p>Property <b>RootClass</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [SingleValue] [ReadOnly] [Required]<br>
     * Full description follows.</p>
     * 
     * @return the <i>root</i> <code>Class</code> of the class hierarchy in this <code>Repository</code>
     */
    public OdmaClass getRootClass()
    {
        try
        {
            return (OdmaClass)getProperty(OdmaCommonNames.PROPERTY_ROOTCLASS).getReference();
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
     * Returns the list of <code>Class</code>es that represent an <i>Aspect</i> and that do not inherit another aspect.<br>
     * 
     * <p>Property <b>RootAspects</b> (opendma): <b>Reference to Class (opendma)</b><br>
     * [MultiValue] [ReadOnly] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the list of <code>Class</code>es that represent an <i>Aspect</i> and that do not inherit another aspect
     */
    @SuppressWarnings("unchecked")
    public Iterable<OdmaClass> getRootAspects()
    {
        try
        {
            return (Iterable<OdmaClass>)getProperty(OdmaCommonNames.PROPERTY_ROOTASPECTS).getReferenceIterable();
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
     * Returns the <i>root</i> <code>Folder</code> of a dedicated folder tree in this <code>Repository</code> (if any).<br>
     * 
     * <p>Property <b>RootFolder</b> (opendma): <b>Reference to Folder (opendma)</b><br>
     * [SingleValue] [ReadOnly] [Nullable]<br>
     * Full description follows.</p>
     * 
     * @return the <i>root</i> <code>Folder</code> of a dedicated folder tree in this <code>Repository</code> (if any)
     */
    public OdmaFolder getRootFolder()
    {
        try
        {
            return (OdmaFolder)getProperty(OdmaCommonNames.PROPERTY_ROOTFOLDER).getReference();
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
