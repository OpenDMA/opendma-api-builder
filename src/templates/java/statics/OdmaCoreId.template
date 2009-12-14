package org.opendma.impl.core;

import org.opendma.api.OdmaId;

public class OdmaCoreId implements OdmaId
{

    protected String uuid;
    
    public OdmaCoreId(String id)
    {
        uuid = id;
    }
    
    public boolean equals(Object obj)
    {
        return (obj instanceof OdmaCoreId) && ((OdmaCoreId)obj).uuid.equals(uuid);
    }

    public String toString()
    {
        return uuid;
    }

}
