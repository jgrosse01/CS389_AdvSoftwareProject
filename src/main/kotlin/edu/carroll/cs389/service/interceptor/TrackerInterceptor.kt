package edu.carroll.cs389.service.interceptor

import edu.carroll.cs389.configuration.TrackerInterceptorProperties
import edu.carroll.cs389.jpa.repo.TrackerRepo
import edu.carroll.cs389.service.tracker.TrackService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class TrackerInterceptor(private val trackerRepo: TrackerRepo, @Qualifier("trackServiceRaw") private val trackerService: TrackService, private val trackerInterceptorProperties: TrackerInterceptorProperties) : HandlerInterceptor {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackerInterceptor::class.java)
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.debug("preHandle: PreHandle tracking initiated")
        if (request.requestURI.toString() == "/favicon.ico") {
            log.debug("preHandle: Ignoring tracking request to favicon")
        } else if (request.requestURI.toString() == "/error" && this.trackerInterceptorProperties.ignoreErrorPageWhenTracking) {
            log.debug("preHandle: Ignoring tracking request to error page")
        } else {
            log.debug("preHandle: Tracking request for ${request.requestURI.toString()} being processed")
            trackerService.trackClient(request)
        }
        log.debug("preHandle: PreHandle tracking concluded")
        return true
    }
}