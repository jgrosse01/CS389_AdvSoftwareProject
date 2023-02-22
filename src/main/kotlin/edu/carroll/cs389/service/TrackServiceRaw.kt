package edu.carroll.cs389.service

import jakarta.servlet.http.HttpServletRequest

class TrackServiceRaw : TrackService {
    override fun getClientIP(req: HttpServletRequest): String {
        return req.remoteAddr
    }

    override fun getUserAgent(req: HttpServletRequest): String {
        TODO("Not yet implemented")
    }

    private fun parseUserAgent(ua: String): List<String> {
        TODO("Not yet implemented")
    }
}