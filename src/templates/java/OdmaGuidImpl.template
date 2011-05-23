package org.opendma;

import org.opendma.api.OdmaGuid;

public class OdmaGuidImpl implements OdmaGuid
{

    protected OdmaIdImpl repositoryId;
    
    protected OdmaIdImpl objectId;
    
    public OdmaGuidImpl(OdmaIdImpl idRepository, OdmaIdImpl idObject)
    {
        repositoryId = idRepository;
        objectId = idObject;
    }
    
    public boolean equals(Object obj)
    {
        return (obj instanceof OdmaGuidImpl) && ((OdmaGuidImpl)obj).repositoryId.equals(repositoryId) && ((OdmaGuidImpl)obj).objectId.equals(objectId);
    }

    public String toString()
    {
        return repositoryId.toString() + ":" + objectId.toString();
    }
    
}
