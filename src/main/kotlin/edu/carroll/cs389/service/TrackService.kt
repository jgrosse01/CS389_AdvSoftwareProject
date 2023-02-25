package edu.carroll.cs389.service

import jakarta.servlet.http.HttpServletRequest

interface TrackService {
    fun trackClient(req: HttpServletRequest, url: String)
}