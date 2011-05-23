package org.opendma;

import org.opendma.api.OdmaId;

public class OdmaIdImpl implements OdmaId
{

    protected String uuid;
    
    public OdmaIdImpl(String id)
    {
        uuid = id;
    }
    
    public boolean equals(Object obj)
    {
        return (obj instanceof OdmaIdImpl) && ((OdmaIdImpl)obj).uuid.equals(uuid);
    }

    public String toString()
    {
        return uuid;
    }

}
