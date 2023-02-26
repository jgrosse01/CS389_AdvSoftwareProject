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

@Service
class TrackServiceRaw(private val trackerRepo: TrackerRepo) : TrackService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackServiceRaw::class.java)
    }

    private val parser: UserAgentParser = UserAgentService().loadParser(
        listOf(
            BrowsCapField.BROWSER,
            BrowsCapField.BROWSER_TYPE,
            BrowsCapField.BROWSER_MAJOR_VERSION,
            BrowsCapField.PLATFORM,
            BrowsCapField.PLATFORM_VERSION
        )
    )

    override fun trackClient(req: HttpServletRequest) {
        log.debug("trackClient: Attempting to track connecting client")
        val clientInfo: Capabilities = parser.parse(req.getHeader("User-Agent"))
        val ipv4: String = if (req.remoteAddr == "0:0:0:0:0:0:0:1") {
            "127.0.0.1"
        } else {
            req.remoteAddr
        }

        // null-check and format browser string (java library has potential to return null)
        var browser: String? = null
        if (clientInfo.browser != null && clientInfo.browserMajorVersion != null) {
            log.debug("trackClient: $ipv4 successfully acquired browser info")
            browser = "${clientInfo.browser} ${clientInfo.browserMajorVersion}"
        } else if (clientInfo.browser != null && clientInfo.browserMajorVersion == null) {
            log.warn("trackClient: $ipv4 client browser missing version tag")
            browser = clientInfo.browser
        } else {
            log.warn("trackClient: $ipv4 client has no browser info")
        }

        var os: String? = null
        if (clientInfo.platform != null && clientInfo.platformVersion != null) {
            log.debug("trackClient: $ipv4 successfully acquired OS info")
            os = "${clientInfo.platform} ${clientInfo.platformVersion}"
        } else if (clientInfo.platform != null && clientInfo.platformVersion == null) {
            log.warn("trackClient: $ipv4 client OS missing version tag")
            os = clientInfo.platform
        } else {
            log.warn("trackClient: $ipv4 client has no OS info")
        }

        // save trackedUser to DB with potentially null val for OS/Browser
        val user: TrackedUser = TrackedUser(req.remoteAddr, os, browser, req.requestURI.toString())
        log.debug("trackClient: Successfully created TrackedUser entity for client ${req.remoteAddr}")
        try {
            trackerRepo.save(user)
            log.info("trackClient: Successfully tracked $ipv4")
        } catch (e: Exception) {
            log.error("trackClient: Failed to save client info to database")
        }
    }
}