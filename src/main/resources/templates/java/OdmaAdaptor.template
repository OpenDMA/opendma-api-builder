package org.opendma.api;

import java.util.List;
import java.util.Properties;

import org.opendma.exceptions.OdmaException;

/**
 * An OpenDMA Adaptor allows client applications to establish sessions with
 * an ECM or DM system.
 */
public interface OdmaAdaptor
{

    /** connection parameter for the username */
    public static final String PARAM_USERNAME = "username";

    /** connection parameter for the password */
    public static final String PARAM_PASSWORD = "password";

    /** connection parameter for the token type */
    public static final String PARAM_TOKEN_TYPE = "tokenType";

    /** connection parameter for the token */
    public static final String PARAM_TOKEN = "token";

    /** connection parameter for the endpoint */
    public static final String PARAM_ENDPOINT = "endpoint";

    /**  qualified name of the JWT token type */
    public static final OdmaQName TOKEN_TYPE_JWT = new OdmaQName("web", "jwt");

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
    
    /**
     * Returns a list of token types that can be used for authentication.
     * 
     * @return a list of token types that can be used for authentication
     */
    List<OdmaQName> getSupportedTokenTypes();

}
