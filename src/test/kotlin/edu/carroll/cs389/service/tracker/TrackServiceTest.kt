package edu.carroll.cs389.service.tracker

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.util.AssertionErrors.assertTrue


@SpringBootTest
class TrackServiceTest(private val trackService: TrackService) {

    // Tests to handle expected input (Happy Path)
    @Test
    fun trackClientTestValidUserAgent() {
//        val mockRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
//
//        `when`(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
//        `when`(mockRequest.getParameter("requestURI")).thenReturn("/")
//        `when`(mockRequest.getParameter("requestAddr")).thenReturn("192.168.1.1")

        val mockRequest: MockHttpServletRequest = MockHttpServletRequest()
        mockRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
        mockRequest.requestURI = "/"
        mockRequest.remoteAddr = "192.168.1.1"

        assertTrue("trackClientTestValidUserAgent(): should succeed with valid user agent", trackService.trackClient(mockRequest))
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