package edu.carroll.cs389.service

import jakarta.servlet.http.HttpServletRequest

class TrackServiceProxyLB : TrackService {
    override fun trackClient(req: HttpServletRequest, url: String){
        var remoteAddr = req.getHeader("X-FORWARDED-FOR")
        if (remoteAddr != null) {
            remoteAddr = req.remoteAddr
        }
    }

    private fun parseUserAgent(ua: String): List<String> {
        TODO("Not yet implemented")
    }
}