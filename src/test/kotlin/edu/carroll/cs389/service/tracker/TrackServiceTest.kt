package edu.carroll.cs389.service.tracker

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
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

        assertTrue("trackClientTestValidUserAgent(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion,  requestURI))
    }

//    @Test
//    fun trackClientTestValidUserAgentMissingBrowserVersion() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingOSVersion() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingOS() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingBrowser() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingBrowserAndVersion() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingOSAndVersion() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion() {
//
//    }
//
//    @Test
//    fun trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion() {
//
//    }
//
//    // Tests to handle unexpected input (Crappy Path)
//
//    @Test
//    fun trackClientTestMissingUserAgent() {
//
//    }
//
//    @Test
//    fun trackClientTestBrowserAndOSReversedUserAgent() {
//
//    }
//
//    // Tests to handle either missing or malicious input (Crazy Path)
//
//    @Test
//    fun trackClientTestNullUserAgent() {
//
//    }
//
//    @Test
//    fun trackClientTestBlankServletRequest() {
//
//    }
//
//    @Test
//    fun trackClientTestEmptyServletRequest() {
//
//    }
//
//    @Test
//    fun trackClientTestSQLInjectionUserAgent() {
//
//    }
}