package edu.carroll.cs389.service.tracker

import com.blueconic.browscap.BrowsCapField
import com.blueconic.browscap.Capabilities
import com.blueconic.browscap.UserAgentParser
import com.blueconic.browscap.UserAgentService
import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Implementation of TrackService which takes raw (not passed through a load balancer or proxy)
 * Uses browscap library to process user-agent information from servlet requests
 * @see TrackService
 * @see com.blueconic.browscap
 *
 * @param trackerRepo: JPA repository that allows search by client ipv4 address
 * @see edu.carroll.cs389.jpa.repo.TrackerRepo
 */
@Service
class TrackServiceRaw(private val trackerRepo: TrackerRepo) : TrackService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackServiceRaw::class.java)
    }

    private val userAgentParser: UserAgentParser = UserAgentService().loadParser(
        listOf(
            BrowsCapField.BROWSER,
            BrowsCapField.BROWSER_TYPE,
            BrowsCapField.BROWSER_MAJOR_VERSION,
            BrowsCapField.PLATFORM,
            BrowsCapField.PLATFORM_VERSION
        )
    )

    /**
     * Function to parse user-agent from the passed in request and save information to the attached database in the
     * form of a TrackedUser
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     *
     * @param req: the HTTP Servlet Request which corresponds to a client attempting to access a webpage within the app
     */
    override fun trackClient(req: HttpServletRequest): Boolean {
        log.debug("trackClient: Attempting to track connecting client")
        val clientInfo: Capabilities = userAgentParser.parse(req.getHeader("User-Agent"))
        val ipv4: String = if (req.remoteAddr == "0:0:0:0:0:0:0:1") {
            "127.0.0.1"
        } else {
            req.remoteAddr
        }

        // null-check and format browser string (java library has potential to return null)
        var browser: String? = null
        // if we have a browser and corresponding version
        if (clientInfo.browser != null && (clientInfo.browserMajorVersion != null && clientInfo.browserMajorVersion != "Unknown")) {
            log.debug("trackClient: $ipv4 successfully acquired browser info")
            browser = "${clientInfo.browser} ${clientInfo.browserMajorVersion}"
        }
        // if we have a browser but not a corresponding version
        else if (clientInfo.browser != null && (clientInfo.browserMajorVersion == null || clientInfo.browserMajorVersion == "Unknown")) {
            log.warn("trackClient: $ipv4 client browser missing version tag")
            browser = clientInfo.browser
        } else {
            log.warn("trackClient: $ipv4 client has no browser info")
        }

        // null-check and format operating system string (java library has potential to return null)
        var os: String? = null
        // if we have a platform and corresponding version
        if (clientInfo.platform != null && (clientInfo.platformVersion != null && clientInfo.platformVersion != "Unknown")) {
            log.debug("trackClient: $ipv4 successfully acquired OS info")
            os = "${clientInfo.platform} ${clientInfo.platformVersion}"
        }
        // if we have a platform but not a corresponding version
        else if (clientInfo.platform != null && (clientInfo.platformVersion == null || clientInfo.platformVersion == "Unknown")) {
            log.warn("trackClient: $ipv4 client OS missing version tag")
            os = clientInfo.platform
        } else {
            log.warn("trackClient: $ipv4 client has no OS info")
        }

        // save trackedUser to DB with potentially null val for OS/Browser
        val user: TrackedUser = TrackedUser(req.remoteAddr, os, browser, req.requestURI.toString())
        log.debug("trackClient: Successfully created TrackedUser entity for client ${req.remoteAddr}")
        return try {
            trackerRepo.save(user)
            log.info("trackClient: Successfully tracked $ipv4")
            true
        } catch (e: Exception) {
            log.error("trackClient: Failed to save client info to database")
            false
        }
    }
}