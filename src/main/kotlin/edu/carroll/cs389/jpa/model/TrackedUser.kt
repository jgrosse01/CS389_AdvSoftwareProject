package edu.carroll.cs389.jpa.model

import jakarta.persistence.*
import java.sql.Date
import java.sql.Timestamp

@Entity
@Table(name="TrackedUsers")
class TrackedUser() {
    companion object {
        val serialVersionUID: Long = 1L
        val EOL: String = System.lineSeparator()
        val TAB: String = "\t"
    }

    @Id
    @GeneratedValue
    private var id: Int? = null

    @Column(name = "ipv4", nullable = false)
    private lateinit var ipv4: String

    @Column(name = "OS")
    private lateinit var os: String

    @Column(name = "browser")
    private lateinit var browser: String

    @Column(name = "date_accessed", nullable = false)
    private lateinit var dateAccessed: Date

    @Column(name = "date_expires", nullable = false)
    private lateinit var dateExpires: Date

    @Column(name = "timestamp", nullable = false)
    private lateinit var timestamp: Timestamp

    @Column(name = "page_visited", nullable = false)
    private lateinit var pageVisited: String

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

    override fun toString(): String {
        return "Client $ipv4 accessed page $pageVisited from $browser running on $os at $timestamp."
    }
}