package edu.carroll.cs389.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="application.tracker-interceptor")
class TrackerInterceptorProperties() {
    var ignoreErrorPageWhenTracking: Boolean = false
}