using System;
using System.Collections.Generic;
using System.Text;

namespace OpenDMA.Api
{

    /// <summary>
    /// A session is the context through which objects can be retrieved from a specific OpenDMA domain.
    /// It is typically established for a defined account with an instance of a document management system.
    /// </summary>
    public interface IOdmaSession
    {

        /// <summary>
        /// Returns a list of repository <see cref="OdmaId"/>s the account has access to.
        /// </summary>
        /// <returns>A list of repository <see cref="OdmaId"/>s the account has access to.</returns>
        IList<OdmaId> GetRepositoryIds();

        /// <summary>
        /// Returns the <see cref="IOdmaRepository"/> object for the given repository id.
        /// </summary>
        /// <param name="repositoryId">The id of the repository to return.</param>
        /// <returns>The <see cref="IOdmaRepository"/> object for the given repository id.</returns>
        /// <exception cref="OdmaObjectNotFoundException">If a repository with the given id does not exist or the current account has no access.</exception>
        IOdmaRepository GetRepository(OdmaId repositoryId);

        /// <summary>
        /// Returns the object of the given class identified by the given ID in the given repository.
        /// </summary>
        /// <param name="repositoryId">The id of the repository to retrieve the object from.</param>
        /// <param name="objectId">The id of the object to return.</param>
        /// <param name="propertyNames">Array of qualified property names to retrieve from the server or <c>null</c> to retrieve all.</param>
        /// <returns>The object of the given class identified by the given ID in the given repository.</returns>
        /// <exception cref="OdmaObjectNotFoundException">If no object with this ID exists or the account has no access.</exception>
        IOdmaObject GetObject(OdmaId repositoryId, OdmaId objectId, OdmaQName[] propertyNames);

        /// <summary>
        /// Performs a search operation against a repository and returns the result.
        /// </summary>
        /// <param name="repositoryId">The id of the repository to retrieve the object from.</param>
        /// <param name="queryLanguage">The language specifier in which the query is given.</param>
        /// <param name="query">Search specification in the given query language.</param>
        /// <returns>The search result of this operation.</returns>
        /// <exception cref="OdmaObjectNotFoundException">If the repository does not exist.</exception>
        /// <exception cref="OdmaQuerySyntaxException">If the query string is syntactically incorrect or the query language is not supported.</exception>
        IOdmaSearchResult Search(OdmaId repositoryId, string queryLanguage, string query);

        /// <summary>
        /// Returns a list of query languages that can be used to search the repository.
        /// </summary>
        /// <returns>A list of query languages that can be used to search the repository.</returns>
        IList<OdmaQName> GetSupportedQueryLanguages();

        /// <summary>
        /// Invalidate this session and release all associated resources.
        /// </summary>
        void Close();

    }

}