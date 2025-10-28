package org.opendma.exceptions;

import org.opendma.api.OdmaId;

/**
 * Thrown when an OpenDMA implementation is unable to locate the requested object.
 */
public class OdmaObjectNotFoundException extends OdmaException {

    private static final long serialVersionUID = 3730730647471169915L;

    private final OdmaId repositoryId;

    private final OdmaId objectId;

    /**
     * Constructs a new OdmaObjectNotFoundException indicating that a repository
     * cannot be found.
     *
     * @param repositoryId the unique identifier of the repository that was not found
     */
    public OdmaObjectNotFoundException(OdmaId repositoryId) {
        super("Repository not found: `"+repositoryId+"`");
        this.repositoryId = repositoryId;
        this.objectId = null;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException indicating that an object
     * cannot be found within a repository.
     *
     * @param repositoryId the unique identifier of the existing repository
     * @param objectId the unique identifier of the object that was not found
     */
    public OdmaObjectNotFoundException(OdmaId repositoryId, OdmaId objectId) {
        super("Object `"+objectId+"` not found in repository `"+repositoryId+"`");
        this.repositoryId = repositoryId;
        this.objectId = objectId;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified detail message and IDs.
     *
     * @param message the detail message.
     * @param repositoryId the unique identifier of the repository
     * @param objectId the unique identifier of the object that was not found or null if the repository does not exist
     */
    public OdmaObjectNotFoundException(String message, OdmaId repositoryId, OdmaId objectId) {
        super(message);
        this.repositoryId = repositoryId;
        this.objectId = objectId;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified detail message, cause and IDs.
     *
     * @param message the detail message.
     * @param cause the cause.
     * @param repositoryId the unique identifier of the repository
     * @param objectId the unique identifier of the object that was not found or null if the repository does not exist
     */
    public OdmaObjectNotFoundException(String message, Throwable cause, OdmaId repositoryId, OdmaId objectId) {
        super(message, cause);
        this.repositoryId = repositoryId;
        this.objectId = objectId;
    }

    /**
     * Constructs a new OdmaObjectNotFoundException with the specified cause and IDs.
     *
     * @param cause the cause.
     * @param repositoryId the unique identifier of the repository
     * @param objectId the unique identifier of the object that was not found or null if the repository does not exist
     */
    public OdmaObjectNotFoundException(Throwable cause, OdmaId repositoryId, OdmaId objectId) {
        super(cause);
        this.repositoryId = repositoryId;
        this.objectId = objectId;
    }

    /**
     * Returns the unique identifier of the repository
     *
     * @return the ID of the repository
     */
    public OdmaId getRepositoryId() {
        return repositoryId;
    }

    /**
     * Returns the unique identifier of the object that was not found or null if the repository has not been found
     *
     * @return the ID of the missing object.
     */
    public OdmaId getObjectId() {
        return objectId;
    }

}
