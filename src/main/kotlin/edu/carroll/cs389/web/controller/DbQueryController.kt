package edu.carroll.cs389.web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/**
 * Controller to house database query pages.
 * This website tracks information from connecting clients, and this controller will direct to pages that allow the
 * client connection info to be viewed in a table.
 *
 * @see edu.carroll.cs389.service.tracker.TrackService
 */
@Controller
class DbQueryController {
    companion object{
        val log: Logger = LoggerFactory.getLogger(DbQueryController::class.java)
    }

    /**
     * Currently just returns a blank template. Will be adjusted later to reflect page content.
     */
    @GetMapping("/ip_info")
    fun ipInfoGet(): String {
        log.info("ipInfoGet(): User has landed at the ip query page ('/ip_info')")
        return "ip_info"
    }

    /**
     * Currently just returns a blank template. Will return a template with filled table later.
     */
    @PostMapping("/ip_info")
    fun ipInfoPost(): String {
        log.info("ipInfoPost(): User has queried the database for ip information")
        return "ip_info"
    }
}