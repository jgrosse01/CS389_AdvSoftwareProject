package edu.carroll.cs389

import edu.carroll.cs389.configuration.TrackerInterceptorProperties
import edu.carroll.cs389.service.tracker.TrackService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor which handles client tracking outside of HTML Controllers
 *
 * @param trackerService: Injected TrackService implementation which will be used to track client information upon
 *                        intercepting request
 * @see edu.carroll.cs389.service.tracker.TrackService
 *
 * @param trackerInterceptorProperties: Injected TrackerInterceptorProperties object which allows reference to custom
 *                                      application.properties configurations related to request tracking
 * @see edu.carroll.cs389.configuration.TrackerInterceptorProperties
 */
@Component
class TrackerInterceptor(
    @Qualifier("trackServiceRaw") private val trackerService: TrackService,
    private val trackerInterceptorProperties: TrackerInterceptorProperties
) : HandlerInterceptor {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackerInterceptor::class.java)
    }

    /**
     * Override of the preHandle function to handle client tracking
     *
     * Tracks client requests excluding those to the "/favicon.ico" subdomain and conditionally the "/error" subdomain
     * based on the properties of the values associated with the trackerInterceptorProperties object passed at object
     * construction.
     *
     * @return Always true to pass forward to the controller even if tracking failed.
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.debug("preHandle: PreHandle tracking initiated")
        // ignore favicon requests
        if (request.requestURI.toString() == "/favicon.ico") {
            log.debug("preHandle: Ignoring tracking request to favicon")
        }
        // ignore error requests based on configurations read and passed at runtime
        else if (request.requestURI.toString() == "/error" && this.trackerInterceptorProperties.ignoreErrorPageWhenTracking) {
            log.debug("preHandle: Ignoring tracking request to error page")
        }
        // log any other request
        else {
            log.debug("preHandle: Tracking request for ${request.requestURI} being processed")
            trackerService.trackClient(request)
        }
        log.debug("preHandle: PreHandle tracking concluded")
        return true
    }
}