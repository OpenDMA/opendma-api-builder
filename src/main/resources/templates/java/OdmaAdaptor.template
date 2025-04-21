package org.opendma.api;

import java.util.Properties;

import org.opendma.exceptions.OdmaException;

public interface OdmaAdaptor
{

    public static final String PARAM_USERNAME = "username";

    public static final String PARAM_PASSWORD = "password";

    public static final String PARAM_TOKEN = "token";

    public static final String PARAM_ENDPOINT = "endpoint";

    /**
     * Attempts to establish a session with the document management system through this adaptor.
     * 
     * @param params
     *            set of adaptor specific parameters to be used for the new session
     * 
     * @return a <code>{@link OdmaSession}</code> to the document management system
     * 
     * @throws OdmaException if this adaptor cannot establish a session
     */
    public OdmaSession connect(Properties params) throws OdmaException;
    
    /**
     * Returns the identifier of this adaptor. Applications can discover this adaptor
     * on the classpath based on this identifier using the {@link OdmaAdaptorDiscovery}
     *
     * @return The identifier of this adaptor.
     */
    public String getSystemId();

}
