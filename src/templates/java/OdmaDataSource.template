package org.opendma;

/**
 * A factory for sessions with the physical OpenDMA data source that this <code>OdmaDataSource</code>
 * object represents. An adapter that implements the <code>OdmaDataSource</code> interface will typically
 * be registered with a naming service based on JNDI.<br/>
 * This interface has been designed with the JNDI <code>DataSource</code> interface in mind and
 * constitutes it's counterpart in the OpenDMA world.
 * 
 * @author Stefan Kopf, xaldon Technologies GmbH, the OpenDMA architecture board
 */
public interface OdmaDataSource
{

    /**
     * Attempts to establish a session with the ODMA data source this object represents.
     * 
     * @return a <code>{@link OdmaSession}</code> to the ODMA data source
     */
    public OdmaSession getSession();

    /**
     * Attempts to establish a session with the ODMA data source this object represents
     * with the given username and password.
     * 
     * @param username
     *            the repository user on whose behalf the session is beeing established
     * @param password
     *            the user's password
     * 
     * @return a <code>{@link OdmaSession}</code> to the ODMA data source
     */
    public OdmaSession getSession(String username, String password);

}