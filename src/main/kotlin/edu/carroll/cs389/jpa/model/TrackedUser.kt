package edu.carroll.cs389.jpa.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.Date
import java.util.Objects

@Entity
@Table(name="TrackedUsers")
open class TrackedUser() {
    companion object {
        private const val serialVersionUID: Long = 1L
        // this is 30 days in milliseconds
        private const val store_duration: Long = 2592000000
    }

    constructor(ipv4: String, url: String) : this() {
        this.ipv4 = ipv4
        this.pageVisited = url

        this.timestamp = Timestamp(Date().time)
    }

    constructor(ipv4: String, os: String, browser: String, url: String) : this() {
        this.ipv4 = ipv4
        this.os = os
        this.browser = browser
        this.pageVisited = url

        this.timestamp = Timestamp(Date().time)
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

    @Column(name = "timestamp", nullable = false)
    private lateinit var timestamp: Timestamp

    @Column(name = "url_visited", nullable = false)
    private lateinit var pageVisited: String

    fun id(): Int? {return this.id}
    fun ipv4(): String {return this.ipv4}
    fun os(): String {return this.os}
    fun browser(): String {return this.browser}
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