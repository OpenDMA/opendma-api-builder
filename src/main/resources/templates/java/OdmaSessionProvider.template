package org.opendma.api;

/**
 * A factory for sessions with the document management system this <code>OdmaSessionProvider</code>
 * object represents. An adapter that implements the <code>OdmaSessionProvider</code> interface will
 * typically be registered with a naming service based on JNDI.<br/>
 * An implementation of <code>OdmaSessionProvider</code> must include a public no-arg constructor.
 */
public interface OdmaSessionProvider {

    /**
     * Attempts to establish a session with the document management system this object represents.
     * 
     * @return a <code>{@link OdmaSession}</code> to the document management system
     */
    public OdmaSession getSession();

    /**
     * Attempts to establish a session with the document management system this object represents
     * as the account represented by the given username and password.
     * 
     * @param username
     *            the repository user on whose behalf the session is beeing established
     * @param password
     *            the user's password
     * 
     * @return a <code>{@link OdmaSession}</code> to the document management system
     */
    public OdmaSession getSessionForAccount(String username, String password);

    /**
     * Attempts to establish a session with the document management system this object represents
     * as the account represented by the given token.
     * 
     * @param tokenType
     *            the type of the token, e.g. <code>web:jwt</code>
     * @param token
     *            String representation of the token
     * 
     * @return a <code>{@link OdmaSession}</code> to the document management system
     */
    public OdmaSession getSessionWithToken(OdmaQName tokenType, String token);

}