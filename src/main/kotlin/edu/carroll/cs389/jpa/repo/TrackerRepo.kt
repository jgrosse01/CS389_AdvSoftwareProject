package edu.carroll.cs389.jpa.repo

import edu.carroll.cs389.jpa.model.TrackedUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Extension of JpaRepository interface
 *
 * Allows for searching by fields of this application's database tables.
 */
interface TrackerRepo : JpaRepository<TrackedUser, Int> {
    /**
     * JPA Magic to define a function which will search the attached database by ipv4_address
     *
     * @param ipv4: The ipv4 address we would like to search the database for
     *
     *@return A list of all TrackedUser entities in the database which match the search condition
     */
    fun findByClientIpv4Address(ipv4: String): List<TrackedUser>
}