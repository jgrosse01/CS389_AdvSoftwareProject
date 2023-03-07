package edu.carroll.cs389.web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * HTML Controller to process non-specific requests (those which do not fall into a particular category within the site)
 */
@Controller
class IndexController() {
    companion object{
        val log: Logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    /**
     * Process HTTPServletRequests to teh "/" domain and return the corresponding HTML
     */
    @GetMapping("/")
    fun index(): String {
        log.info("index(): User landed at index page ('/')")
        return "index"
    }
}