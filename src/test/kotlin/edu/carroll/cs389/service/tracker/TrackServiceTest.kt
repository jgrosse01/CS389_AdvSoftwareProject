package edu.carroll.cs389.service.tracker

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertFalse
import org.springframework.test.util.AssertionErrors.assertTrue


@SpringBootTest
class TrackServiceTest {

    @Qualifier("trackServiceRaw")
    @Autowired
    private lateinit var trackService: TrackService

    // Tests to handle expected input (Happy Path)
    @Test
    fun trackClientTestValidUserAgent() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    // test should not pass because there is no data cleaning/sanitization right now
    // consider using DTO as a midstep to condense passed info and clean data
    @Disabled
    @Test
    fun trackClientTestValidUserAgentGenericInfo() {
        val ipv4: String? = "0.0.0.0"
        val browser: String? = "I'm a Browser"
        val browserMajorVersion: String? = "What a Browser Version this is!"
        val platform: String? = "This isn't an operating system (:"
        val platformVersion: String? = "If it doesn't exist why does it need a version"
        val requestURI: String? = "/oooooh_a_request"

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = ""
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullBrowserVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = null
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }


    @Test
    fun trackClientTestValidUserAgentMissingOSVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = ""
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullOSVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = null
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowser() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = ""
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullBrowser() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = null
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOS() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = ""
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullOS() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = null
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = ""
        val browserMajorVersion: String? = ""
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = ""
        val platformVersion: String? = ""
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = ""
        val platform: String? = ""
        val platformVersion: String? = ""
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = ""
        val browserMajorVersion: String? = ""
        val platform: String? = "Ubuntu"
        val platformVersion: String? = ""
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingIPV4() {
        val ipv4: String? = ""
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullIPV4() {
        val ipv4: String? = null
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/"

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentMissingURI() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = ""

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    @Test
    fun trackClientTestValidUserAgentNullURI() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = null

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }


    // Tests to handle unexpected input (Crappy Path)

    @Test
    fun trackClientTestMissingUserAgent() {
        // simulate missing useragent by setting all the values parsed from the user agent to null
        val ipv4: String? = "192.168.1.1"
        val browser: String? = null
        val browserMajorVersion: String? = null
        val platform: String? = null
        val platformVersion: String? = null
        val requestURI: String? = "/"

        assertTrue(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    //
//    // Tests to handle either missing or malicious input (Crazy Path)
//
    @Test
    fun trackClientTestSQLInjectionUserAgent() {

    }

    @Disabled
    @Test
    fun trackClientTestInvalidURIForApp() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"
        val requestURI: String? = "/this_uri_does_not_exist"

        assertFalse(
            "trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )
    }

    // this test is expected to fail right now, I have no valid URI checking right now
    @Disabled
    @Test
    fun trackClientTestInvalidURIFormat() {
        val ipv4: String? = "192.168.1.1"
        val browser: String? = "Firefox"
        val browserMajorVersion: String? = "110"
        val platform: String? = "Ubuntu"
        val platformVersion: String? = "22.04 LTS"

        val invalidURIs: List<String> = listOf(
            "./this_could_be-a-File", "//uri_here", "subdomain1/subdomain2",
            "/\\/waow", "; delete from tracked_users where True;"
        )

        for (uri in invalidURIs) {
            val requestURI: String? = uri

            assertFalse(
                "trackClientTestValidUserAgent(): should succeed with valid user agent",
                trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
            )
        }
    }
}