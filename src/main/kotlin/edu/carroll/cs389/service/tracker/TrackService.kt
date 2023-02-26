package edu.carroll.cs389.service.tracker

import jakarta.servlet.http.HttpServletRequest

interface TrackService {
    fun trackClient(req: HttpServletRequest)
}