package edu.carroll.cs389.web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {
    companion object{
        val log: Logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    @GetMapping("/")
    fun index(): String {
        log.info("index(): User landed at index page ('/')")
        return "index"
    }
}