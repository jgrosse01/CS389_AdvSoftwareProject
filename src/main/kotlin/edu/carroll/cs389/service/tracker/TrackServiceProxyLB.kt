package edu.carroll.cs389.service.tracker

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

/**
 * NOT YET IMPLEMENTED
 * Intended purpose to handle requests which go through a load balancer or proxy such that information can still
 * be extracted (I understand that this can come in a slightly different format)
 */
@Service
class TrackServiceProxyLB : TrackService {
    /**
     * NOT YET IMPLEMENTED
     */
    override fun trackClient(
        ipv4: String?,
        browser: String?,
        browserMajorVersion: String?,
        platform: String?,
        platformVersion: String?,
        uri: String?
    ): Boolean {
        TODO("Not yet implemented")
    }
}