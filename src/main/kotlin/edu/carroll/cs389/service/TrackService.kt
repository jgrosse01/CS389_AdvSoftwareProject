package edu.carroll.cs389.service

import jakarta.servlet.http.HttpServletRequest

interface TrackService {
    fun getClientIP(req: HttpServletRequest): String

    fun getUserAgent(req: HttpServletRequest): String
}