package edu.carroll.cs389.jpa.model

import jakarta.persistence.*
import java.sql.Date
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name="TrackedUsers")
open class TrackedUser() {
    companion object {
        val serialVersionUID: Long = 1L
    }

    constructor(ipv4: String) : this() {
        this.ipv4 = ipv4
    }

    @Id
    @GeneratedValue
    private var id: Int? = null

    @Column(name = "ipv4_address", nullable = false)
    private lateinit var ipv4: String

    @Column(name = "operating_system")
    private lateinit var os: String

    @Column(name = "browser")
    private lateinit var browser: String

    @Column(name = "date_expires", nullable = false)
    private lateinit var dateExpires: Date

    @Column(name = "timestamp", nullable = false)
    private lateinit var timestamp: Timestamp

    @Column(name = "url_visited", nullable = false)
    private lateinit var pageVisited: String

    fun id(): Int? {return this.id}
    fun ipv4(): String {return this.ipv4}
    fun os(): String {return this.os}
    fun browser(): String {return this.browser}
    fun dateExpires(): Date {return this.dateExpires}
    fun timestamp(): Timestamp {return this.timestamp}
    fun pageVisited(): String {return this.pageVisited}


    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other::class != this::class) {
            return false
        }
        val trackedUser = other as TrackedUser
        return ipv4 == trackedUser.ipv4 &&
                timestamp == trackedUser.timestamp &&
                pageVisited == trackedUser.pageVisited
    }

    override fun hashCode(): Int {
        return Objects.hash(ipv4, timestamp, pageVisited)
    }

    override fun toString(): String {
        return "Client $ipv4 accessed page $pageVisited from $browser running on $os at $timestamp."
    }
}