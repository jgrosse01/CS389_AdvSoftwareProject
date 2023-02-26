package edu.carroll.cs389.service.tracker

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class TrackServiceProxyLB : TrackService {
    override fun trackClient(req: HttpServletRequest){
        var remoteAddr = req.getHeader("X-FORWARDED-FOR")
        if (remoteAddr != null) {
            remoteAddr = req.remoteAddr
        }
        TODO("Not yet implemented")
    }
}