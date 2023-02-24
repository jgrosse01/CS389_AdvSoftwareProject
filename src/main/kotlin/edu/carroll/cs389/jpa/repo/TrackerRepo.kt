package edu.carroll.cs389.jpa.repo

import edu.carroll.cs389.jpa.model.TrackedUser
import org.springframework.data.jpa.repository.JpaRepository

interface TrackerRepo : JpaRepository<TrackedUser, Int> {
    fun findByIpv4(ipv4: String): List<TrackedUser>
}