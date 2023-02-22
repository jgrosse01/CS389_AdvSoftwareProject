package edu.carroll.cs389.service

import jakarta.servlet.http.HttpServletRequest

class TrackServiceProxyLB : TrackService {
    override fun getClientIP(req: HttpServletRequest): String {
        var remoteAddr = req.getHeader("X-FORWARDED-FOR")
        if (remoteAddr != null) {
            remoteAddr = req.remoteAddr
        }
        return remoteAddr
    }

    override fun getUserAgent(req: HttpServletRequest): String {
        TODO("Not yet implemented")
    }

    private fun parseUserAgent(ua: String): List<String> {
        TODO("Not yet implemented")
    }
}