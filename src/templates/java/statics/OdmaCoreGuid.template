package org.opendma.impl.core;

import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;

public class OdmaCoreGuid implements OdmaGuid
{

    protected OdmaId repositoryId;
    
    protected OdmaCoreId objectId;
    
    public OdmaCoreGuid(OdmaId idRepository, OdmaCoreId idObject)
    {
        repositoryId = idRepository;
        objectId = idObject;
    }
    
    public boolean equals(Object obj)
    {
        return (obj instanceof OdmaCoreGuid) && ((OdmaCoreGuid)obj).repositoryId.equals(repositoryId) && ((OdmaCoreGuid)obj).objectId.equals(objectId);
    }

    public String toString()
    {
        return repositoryId.toString() + ":" + objectId.toString();
    }
    
}
