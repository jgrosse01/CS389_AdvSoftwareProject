package edu.carroll.cs389.web.controller

import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

/**
 * Controller to house database query pages.
 * This website tracks information from connecting clients, and this controller will direct to pages that allow the
 * client connection info to be viewed in a table.html.
 *
 * @see edu.carroll.cs389.service.tracker.TrackService
 */
@Controller
class DbQueryController(private val trackerRepo: TrackerRepo) {
    companion object{
        val log: Logger = LoggerFactory.getLogger(DbQueryController::class.java)
    }

    /**
     * GetMapping function which will populate the on page table with the entire contents of the database.
     *
     * @return an HTML page for the ip_info domain with modelAttribute trackedUsers
     */
    @GetMapping("/ip_info")
    fun ipInfoGet(model: Model): String {
        log.info("ipInfoGet(): User has landed at the ip query page ('/ip_info')")
        model.addAttribute("trackedUsers", trackerRepo.findAll())
        return "ip_info"
    }

    /**
     * GetMapping function which will populate the on page table the search results from a post request.
     *
     * Currently unused as search form not yet implemented.
     *
     * @return an HTML page for the ip_info domain with modelAttribute trackedUsers
     */
    @PostMapping("/ip_info")
    fun ipInfoPost(model: Model, @ModelAttribute("clientIpv4Address") clientIpv4Address: String): String {
        log.info("ipInfoPost(): User has queried the database for ip information")
        model.addAttribute("trackedUsers", trackerRepo.findByClientIpv4Address(clientIpv4Address))
        return "ip_info"
    }
}