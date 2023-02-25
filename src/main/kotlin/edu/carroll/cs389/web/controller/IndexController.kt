package edu.carroll.cs389.web.controller

import edu.carroll.cs389.service.TrackServiceRaw
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController(private val trackService: TrackServiceRaw) {
    companion object{
        val log: Logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    @GetMapping("/")
    fun index(req: HttpServletRequest): String {
        log.info("index(): User landed at index page ('/')")
        trackService.trackClient(req, "/")
        return "index"
    }
}