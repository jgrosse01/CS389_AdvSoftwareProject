package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import edu.carroll.cs389.service.tracker.TrackService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AssertionErrors.assertTrue
import java.lang.reflect.Field
import java.sql.Timestamp
import java.util.Date

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TrackServiceQueryTest {

    @Autowired
    private lateinit var trackService: TrackService

    // trackerRepo needed to directly save to database in order to ensure
    // timestamp equality for TrackedUser comparison
    @Autowired
    private lateinit var trackerRepo: TrackerRepo


    /*******************************************************/
    /*******************************************************/
    /* THESE TESTS ALL PERTAIN TO THE queryDatabase METHOD */
    /*******************************************************/
    /*******************************************************/

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    @Test
    fun validTrackedUserTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)

        val dbUser = trackService.query()[0]
        assertTrue(
            "validTrackedUserTest(): Tracked user should be equal",
            dbUser == user
        )

        assertTrue(
            "validTrackedUserTest(): Tracked user should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv4 == dbUser.clientIpv4Address() &&
            "$browser $browserMajorVersion" == dbUser.clientBrowserInfo() &&
            "$platform $platformVersion" == dbUser.clientOperatingSystem() &&
            requestURI == dbUser.clientConnectionRequestedPage() &&
            timestamp == dbUser.clientConnectionAttemptTimestamp()
        )
    }

    @Test
    fun validTwoTrackedUserTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)

        val dbUser2 = trackService.query()[0]
        val dbUser = trackService.query()[1]
        assertTrue(
            "validTwoTrackedUserTest(): Tracked user should be equal",
            dbUser == user
        )

        assertTrue(
            "validTwoTrackedUserTest(): Tracked user2 should be equal",
            dbUser2 == user2
        )

        assertTrue(
            "validTwoTrackedUserTest(): Tracked user should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv4 == dbUser.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == dbUser.clientBrowserInfo() &&
                    "$platform $platformVersion" == dbUser.clientOperatingSystem() &&
                    requestURI == dbUser.clientConnectionRequestedPage() &&
                    timestamp == dbUser.clientConnectionAttemptTimestamp()
        )

        assertTrue(
            "validTwoTrackedUserTest(): Tracked user2 should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv42 == dbUser2.clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == dbUser2.clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == dbUser2.clientOperatingSystem() &&
                    requestURI2 == dbUser2.clientConnectionRequestedPage() &&
                    timestamp2 == dbUser2.clientConnectionAttemptTimestamp()
        )
    }

    @Test
    fun insertThreeAndCheckOrderByTimestampTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        val timestamp3: Timestamp = Timestamp(Date().time + 300000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        val user3: TrackedUser = TrackedUser(ipv43, "$platform3 $platformVersion3", "$browser3 $browserMajorVersion3", requestURI3)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)
        privateField.set(user3, timestamp3)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)
        trackerRepo.save(user3)

        val results = trackService.query()

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should contain 3 entries",
            results.size == 3
        )

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should be in order starting with greatest " +
                    "timestamp and ending with least",
            results[0].clientConnectionAttemptTimestamp() > results[1].clientConnectionAttemptTimestamp() &&
            results[1].clientConnectionAttemptTimestamp() > results[2].clientConnectionAttemptTimestamp()
        )
    }

    @Test
    fun insertThreeWithTwoSameTimestampAndCheckOrderByTimestampTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = timestamp

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        val timestamp3: Timestamp = Timestamp(Date().time + 300000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        val user3: TrackedUser = TrackedUser(ipv43, "$platform3 $platformVersion3", "$browser3 $browserMajorVersion3", requestURI3)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)
        privateField.set(user3, timestamp3)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)
        trackerRepo.save(user3)

        val results = trackService.query()

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should contain 3 entries",
            results.size == 3
        )

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should be in order starting with greatest " +
                    "timestamp and ending with least",
            results[0].clientConnectionAttemptTimestamp() > results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() >= results[2].clientConnectionAttemptTimestamp()
        )
    }

    @Test
    fun insertThreeAndSearchTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        val timestamp3: Timestamp = Timestamp(Date().time + 300000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        val user3: TrackedUser = TrackedUser(ipv43, "$platform3 $platformVersion3", "$browser3 $browserMajorVersion3", requestURI3)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)
        privateField.set(user3, timestamp3)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)
        trackerRepo.save(user3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeAndSearchTest(): Based on search term, should only have one entry that is returned",
            results.size == 1
        )

        assertTrue(
            "insertThreeAndSearchTest(): Based on search term, returned user should equal user 1 (user)",
            results[0] == user
        )
    }

    @Test
    fun insertThreeWithTwoSameIPv4AndSearchTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "192.168.1.1"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        val timestamp3: Timestamp = Timestamp(Date().time + 300000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        val user3: TrackedUser = TrackedUser(ipv43, "$platform3 $platformVersion3", "$browser3 $browserMajorVersion3", requestURI3)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)
        privateField.set(user3, timestamp3)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)
        trackerRepo.save(user3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeWithTwoSameAndSearchTest(): Should have query size of 2 because 2 have same IP",
            results.size == 2
        )

        assertTrue(
            "insertThreeWithTwoSameAndSearchTest(): Should have query 0 equal user 2 and query 1 equal user 1",
            results[0] == user2 && results[1] == user
        )
    }

    @Test
    fun insertThreeOfSameThenSearchAndCheckOrderByTimestampTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val ipv42: String = "192.168.1.1"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val ipv43: String = "192.168.1.1"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        val timestamp3: Timestamp = Timestamp(Date().time + 300000000)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        val user3: TrackedUser = TrackedUser(ipv43, "$platform3 $platformVersion3", "$browser3 $browserMajorVersion3", requestURI3)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)
        privateField.set(user2, timestamp2)
        privateField.set(user3, timestamp3)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)
        trackerRepo.save(user2)
        trackerRepo.save(user3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeOfSameThenSearchAndCheckOrderByTimestampTest(): Should contain 3 entries",
            results.size == 3
        )

        assertTrue(
            "insertThreeOfSameThenSearchAndCheckOrderByTimestampTest(): Should be in order starting with greatest " +
                    "timestamp and ending with least",
            results[0].clientConnectionAttemptTimestamp() > results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() > results[2].clientConnectionAttemptTimestamp()
        )
    }

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    @Test
    fun queryWithNoEntriesTest() {
        val results = trackService.query()

        assertTrue(
            "queryWithNoEntriesTest(): Should have length 0 in results",
            results.isEmpty()
        )
    }

    @Test
    fun searchWithNoEntriesTest() {
        val results = trackService.query("186.128.74.129")

        assertTrue(
            "searchWithNoEntriesTest(): Should have length 0 in results",
            results.isEmpty()
        )
    }

    @Test
    fun searchForEmptyStringTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)

        var results = trackService.query("")

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (1)",
            results.size == 1
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (1)",
            results[0] == user
        )

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        // setting field to known timestamp so we can compare
        privateField.set(user2, timestamp2)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user2)

        results = trackService.query("")

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (2)",
            results.size == 2
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (2)",
            results[0] == user2 &&
            results[1] == user
        )
    }

    @Test
    fun searchForEntryThatDoesNotExistTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)

        var results = trackService.query("162.187.27.147")

        assertTrue(
            "searchForEntryThatDoesNotExistTest(): when IP does not exist in Db, there should be no result",
            results.isEmpty()
        )
    }


    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

    @Test
    fun searchForNullTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)

        var results = trackService.query(null)

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (1)",
            results.size == 1
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (1)",
            results[0] == user
        )

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"
        val timestamp2: Timestamp = Timestamp(Date().time + 3000000)

        val user2: TrackedUser = TrackedUser(ipv42, "$platform2 $platformVersion2", "$browser2 $browserMajorVersion2", requestURI2)
        // setting field to known timestamp so we can compare
        privateField.set(user2, timestamp2)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user2)

        results = trackService.query(null)

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (2)",
            results.size == 2
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (2)",
            results[0] == user2 &&
                    results[1] == user
        )
    }

    // test method disabled as there is currently no sanitization in place. this is a task for the later life
    // of the project (pre-deployment but outside the scope of this semester project)
    @Disabled
    @Test
    fun searchForSQLCodeTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"
        val timestamp: Timestamp = Timestamp(Date().time)

        val user: TrackedUser = TrackedUser(ipv4, "$platform $platformVersion", "$browser $browserMajorVersion", requestURI)
        // setting private field accessible to ensure that equality can be checked
        val privateField: Field = user.javaClass.getDeclaredField("clientConnectionAttemptTimestamp")
        privateField.isAccessible = true
        // setting field to known timestamp so we can compare
        privateField.set(user, timestamp)

        // insert data into database directly (allows for direct specification of TrackedUser object
        trackerRepo.save(user)

        var results = trackService.query()

        assertTrue(
            "searchForSQLCodeTest(): Should have one datapoint that matches user",
            results.size == 1 &&
            results[0] == user
        )

        results = trackService.query("; DELETE FROM tracked_users WHERE *;")

        assertTrue(
            "searchForSQLCodeTest(): should still keep data after searching for a delete statement",
            results.size == 1 &&
            results[0] == user
        )
    }

}