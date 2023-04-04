package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import edu.carroll.cs389.service.tracker.TrackService
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
class TrackServiceQueryDBTest {

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

        val dbUser = trackService.queryDatabase()[0]
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

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

}