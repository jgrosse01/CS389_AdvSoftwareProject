package edu.carroll.cs389.service.tracker

import jakarta.servlet.http.HttpServletRequest

/**
 * Interface which defines the methods required by a TrackService
 * Indented purpose is to track client connection information when the client accesses any page within the webapp
 *
 * Currently, this class tracks connecting ipv4 address, browser & version, operating system & version, the page the request
 * was directed to, and the timestamp of the request
 */
interface TrackService {
    /**
     * Method which processes incoming requests and converts them into a storable entity in the form of a TrackedUser
     * Once a TrackedUser instance is created from the data in the passed servlet request, it is then saved to the
     * application attached database.
     *
     * @param req: the HTTP Servlet Request which corresponds to a client attempting to access a webpage within the app
     *
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     */
    fun trackClient(req: HttpServletRequest): Boolean
}