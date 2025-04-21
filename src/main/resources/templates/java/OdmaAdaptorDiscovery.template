package org.opendma.api;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

public class OdmaAdaptorDiscovery
{
    
    /**
     * Returns the first OdmaDMA adaptor available on the classpath implementing the given
     * system identifier, <code>null</code> if no such adaptor exists.
     * 
     * @param systemId
     *            the system identifier to return an adaptor for
     *
     * @return OdmaDMA adaptor on the classpath implementing the given system identifier.
     */
    public OdmaAdaptor getAdaptor(String systemId) {
        for(OdmaAdaptor adaptor : getAllAdaptors()) {
            if(adaptor.getSystemId().equals(systemId)) {
                return adaptor;
            }
        }
        return null;
    }
    
    /**
     * Returns all OdmaDMA adaptors available on the classpath.
     *
     * @return All OdmaDMA adaptors available on the classpath.
     */
    public List<OdmaAdaptor> getAllAdaptors() {
        return AccessController.doPrivileged(new PrivilegedAction<List<OdmaAdaptor>>() {
            public List<OdmaAdaptor> run() {
                LinkedList<OdmaAdaptor> result = new LinkedList<OdmaAdaptor>();
                try{
                    for(OdmaAdaptor adaptor : ServiceLoader.load(OdmaAdaptor.class)) {
                        result.add(adaptor);
                    }
                } catch(Throwable t) {
                }
                return result;
            }
        });
    }

}
