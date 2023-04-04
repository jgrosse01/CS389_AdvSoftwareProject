package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.service.tracker.TrackService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AssertionErrors.assertFalse
import org.springframework.test.util.AssertionErrors.assertTrue


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TrackServiceTrackClientTest {

    @Autowired
    private lateinit var trackService: TrackService

    /**********************************/
    /* General DB Persistence Testing */
    /**********************************/

    @Test
    fun testDbIsEmptyWithoutInsert() {
        assertTrue(
            "testDbIsEmptyWithoutInsert(): test db (newly instantiated and cleaned after every test" +
                    " should be empty without inserting data",
            trackService.queryDatabase().isEmpty()
        )
    }


    @Test
    fun testPersistenceOfTwoInserts() {
        val ipv41: String = "192.168.1.1"
        val browser1: String = "Firefox"
        val browserMajorVersion1: String = "110"
        val platform1: String = "Ubuntu"
        val platformVersion1: String = "22.04 LTS"
        val requestURI1: String = "/"

        val ipv42: String = "172.158.27.139"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "37"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        assertTrue(
            "testPersistenceOfTwoInserts(): should succeed with valid user agent",
            trackService.trackClient(ipv41, browser1, browserMajorVersion1, platform1, platformVersion1, requestURI1)
        )

        assertTrue(
            "testPersistenceOfTwoInserts(): should succeed with valid user agent",
            trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        )


        // assuming that queryDatabase works correctly (must assume something works)
        // we know the list is ordered by descending timestamp
        val results = trackService.queryDatabase()
        val user1 = results[1]
        val user2 = results[0]

        assertTrue(
            "testPersistenceOfTwoInserts(): should query information from second insert first due to timestamp",
            ipv41 == user1.clientIpv4Address() &&
                    "$browser1 $browserMajorVersion1" == user1.clientBrowserInfo() &&
                    "$platform1 $platformVersion1" == user1.clientOperatingSystem() &&
                    requestURI1 == user1.clientConnectionRequestedPage()
        )

        assertTrue(
            "testPersistenceOfTwoInserts(): should query information from first insert first due to timestamp",
            ipv42 == user2.clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == user2.clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == user2.clientOperatingSystem() &&
                    requestURI2 == user2.clientConnectionRequestedPage()
        )

    }

    @Test
    fun testVarietyOfData() {
        val ipv4s: List<String> = listOf<String>("18.354.563.541", "72.175.63.11", "174.283.69.420", "542.172.378.29")
        val browsers: List<String> = listOf<String>("Firefox", "Opera", "Brave", "Chrome")
        val browserMajorVersions: List<String> = listOf<String>("112", "7", "v1.49.132", "32")
        val platforms: List<String> = listOf<String>("Windows", "macOS", "Ubuntu", "Mint")
        val platformVersions: List<String> = listOf<String>("10 NT", "Catalina", "22.04 LTS", "Cinnamon 14")
        val requests: List<String> = listOf<String>("/", "/dummy_link", "/ip_info", "/bs-override.css")

        for (i in 0..3) {
            assertTrue(
                "testVarietyOfData(): entry $i should insert properly",
                trackService.trackClient(ipv4s[i], browsers[i], browserMajorVersions[i],
                    platforms[i], platformVersions[i], requests[i])
            )

            assertTrue(
                "testVarietyOfData(): Db should contain ${i+1} entries",
                trackService.queryDatabase().size == i+1
            )
        }

        val results = trackService.queryDatabase()

        for (i in 3 downTo 0) {
            val user = results[3-i]
            assertTrue(
                "testVarietyOfData(): entry $i should be queried properly",
                ipv4s[i] == user.clientIpv4Address() &&
                "${browsers[i]} ${browserMajorVersions[i]}" == user.clientBrowserInfo() &&
                "${platforms[i]} ${platformVersions[i]}" == user.clientOperatingSystem() &&
                requests[i] == user.clientConnectionRequestedPage()
            )
        }
    }

    /*****************************************************/
    /*****************************************************/
    /* THESE TESTS ALL PERTAIN TO THE trackClient METHOD */
    /*****************************************************/
    /*****************************************************/

    /***********************************************/
    /* Tests to handle expected input (Happy Path) */
    /***********************************************/

    @Test
    fun trackClientTestValidUserAgent() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgent(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgent(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
            "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
            "$platform $platformVersion" == user.clientOperatingSystem() &&
            requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = ""
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    browser == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOSVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = ""
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingOSVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingOSVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingOSVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    platform == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowser() {
        val ipv4: String = "192.168.1.1"
        val browser: String = ""
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowser(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowser(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowser(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "unknown" == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOS() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = ""
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingOS(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingOS(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingOS(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    "unknown" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = ""
        val browserMajorVersion: String = ""
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "unknown" == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = ""
        val platformVersion: String = ""
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    "unknown" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = ""
        val platform: String = ""
        val platformVersion: String = ""
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    browser == user.clientBrowserInfo() &&
                    "unknown" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = ""
        val browserMajorVersion: String = ""
        val platform: String = "Ubuntu"
        val platformVersion: String = ""
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "unknown" == user.clientBrowserInfo() &&
                    platform == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingIPV4() {
        val ipv4: String = ""
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertFalse(
            "trackClientTestValidUserAgentMissingIPV4(): should fail without IPv4",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingIPV4(): should have zero items in database after test",
            trackService.queryDatabase().isEmpty()
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullIPV4() {
        val ipv4: String? = null
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertFalse(
            "trackClientTestValidUserAgentNullIPV4(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgent()NullIPV4: should have zero items in database after test",
            trackService.queryDatabase().isEmpty()
        )
    }

    /**************************************************/
    /* Tests to handle unexpected input (Crappy Path) */
    /**************************************************/

    @Test
    fun trackClientTestValidUserAgentNullOS() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String? = null
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentNullOS(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentNullOS(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentNullOS(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    "unknown" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullOSVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String? = null
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentNullOSVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentNullOSVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentNullOSVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    platform == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullBrowser() {
        val ipv4: String = "192.168.1.1"
        val browser: String? = null
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentNullBrowser(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentNullBrowser(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentNullBrowser(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "unknown" == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullBrowserVersion() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String? = null
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestValidUserAgentNullBrowserVersion(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentNullBrowserVersion(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentNullBrowserVersion(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    browser == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }


    // test should not pass because there is no data cleaning/sanitization right now
    // consider using DTO as a midstep to condense passed info and clean data
    // disabled due to lack of sanitization currently
    @Disabled
    @Test
    fun trackClientTestValidUserAgentGenericInfo() {
        val ipv4: String = "0.0.0.0"
        val browser: String = "I'm a Browser"
        val browserMajorVersion: String = "What a Browser Version this is!"
        val platform: String = "This isn't an operating system (:"
        val platformVersion: String = "If it doesn't exist why does it need a version"
        val requestURI: String = "/oooooh_a_request"

        assertFalse(
            "trackClientTestValidUserAgentGenericInfo(): should fail without valid information",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentGenericInfo(): should have zero items in database after test",
            trackService.queryDatabase().isEmpty()
        )
    }

    /******************************************************************/
    /* Tests to handle either missing or malicious input (Crazy Path) */
    /******************************************************************/

    @Test
    fun trackClientTestMissingUserAgent() {
        // simulate missing useragent by setting all the values parsed from the user agent to null
        val ipv4: String = "192.168.1.1"
        val browser: String? = null
        val browserMajorVersion: String? = null
        val platform: String? = null
        val platformVersion: String? = null
        val requestURI: String = "/"

        assertTrue(
            "trackClientTestMissingUserAgent(): should succeed without valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestMissingUserAgent(): should have one item in database after test",
            trackService.queryDatabase().size == 1
        )

        val user = trackService.queryDatabase()[0]

        assertTrue(
            "trackClientTestValidUserAgentMissingUserAgent(): data entered should match on query",
            ipv4 == user.clientIpv4Address() &&
                    "unknown" == user.clientBrowserInfo() &&
                    "unknown" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingURI() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = ""

        assertFalse(
            "trackClientTestValidUserAgentMissingURI(): should fail without valid URI",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentMissingURI(): should have no items in database after test",
            trackService.queryDatabase().isEmpty()
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullURI() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String? = null

        assertFalse(
            "trackClientTestValidUserAgentNullURI(): should fail without valid URI",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "trackClientTestValidUserAgentNullURI(): should have no items in database after test",
            trackService.queryDatabase().isEmpty()
        )
    }

    // this test is expected to fail right now, I have no valid URI checking right now
    @Disabled
    @Test
    fun trackClientTestInvalidURIFormat() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"

        val invalidURIs: List<String> = listOf(
            "./this_could_be-a-File", "//uri_here", "subdomain1/subdomain2",
            "/\\/waow", "; delete from tracked_users where True;"
        )

        for (uri in invalidURIs) {
            val requestURI: String = uri

            assertFalse(
                "trackClientTestValidUserAgent(): should succeed with valid user agent",
                trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
            )
        }
    }

    @Disabled
    @Test
    fun trackClientTestInvalidURIForApp() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/this_uri_does_not_exist"

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Disabled
    @Test
    fun trackClientTestSQLInjectionUserAgent() {
        //NYI
    }

    /*******************************************************/
    /*******************************************************/
    /* THESE TESTS ALL PERTAIN TO THE queryDatabase METHOD */
    /*******************************************************/
    /*******************************************************/

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

}