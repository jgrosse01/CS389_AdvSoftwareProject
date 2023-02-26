package edu.carroll.cs389.service.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class TrackerInterceptorAppConf(private val trackerInterceptor: TrackerInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(trackerInterceptor)
    }
}