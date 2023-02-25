package edu.carroll.cs389.service

import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TrackServiceRaw(private val trackerRepo: TrackerRepo) : TrackService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackServiceRaw::class.java)
    }
    override fun trackClient(req: HttpServletRequest, url: String){
        log.debug("trackClient: Attempting to track connecting client")
        //val header: List<String> = parseUserAgent(req.getHeader("User-Agent"))
       // val user = if (header.isEmpty()) {
            log.warn("trackClient: User tracked without browser or OS information")
            val user = TrackedUser(req.remoteAddr, url)
        //} else {
        //    log.debug("trackClient: Client successfully tracked.")
        //    TrackedUser(req.remoteAddr, header[0], header[1])
        //}
        try {
            trackerRepo.save(user)
        } catch(e: Exception) {
            log.error("trackClient: Failed to save client info to database")
        }
    }

    private fun parseUserAgent(ua: String): List<String> {
        // return OS, browser
        TODO("Not yet implemented")
    }
}