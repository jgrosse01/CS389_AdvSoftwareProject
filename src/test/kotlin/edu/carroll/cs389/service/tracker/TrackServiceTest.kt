package edu.carroll.cs389.service.tracker

import org.springframework.test.util.AssertionErrors.assertFalse
import org.springframework.test.util.AssertionErrors.assertNotNull
import org.springframework.test.util.AssertionErrors.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest

@SpringBootTest
class TrackServiceTest(private val trackService: TrackService) {

    // Tests to handle expected input (Happy Path)
    @Test
    fun trackClientTestValidUserAgent() {
        val mhsr: MockHttpServletRequest = MockHttpServletRequest()
        mhsr.setParameter(
            "User-Agent", "Product/1.0 (OperatingSystem 2.0; Plat; bit; randomInfoBcWhyNot) Base/4.0 Browser/5.0"
        )
        mhsr.requestURI = "/"
        mhsr.remoteAddr = "192.168.1.1"

        assertTrue("trackClientTestValidUserAgent(): should succeed with valid user agent", trackService.trackClient(mhsr))
    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserVersion() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingOSVersion() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingOS() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowser() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersion() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersion() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingOSAndVersionAndBrowserVersion() {

    }

    @Test
    fun trackClientTestValidUserAgentMissingBrowserAndVersionAndOSVersion() {

    }

    // Tests to handle unexpected input (Crappy Path)

    @Test
    fun trackClientTestMissingUserAgent() {

    }

    @Test
    fun trackClientTestBrowserAndOSReversedUserAgent() {

    }

    // Tests to handle either missing or malicious input (Crazy Path)

    @Test
    fun trackClientTestNullUserAgent() {

    }

    @Test
    fun trackClientTestBlankServletRequest() {

    }

    @Test
    fun trackClientTestEmptyServletRequest() {

    }

    @Test
    fun trackClientTestSQLInjectionUserAgent() {

    }
}