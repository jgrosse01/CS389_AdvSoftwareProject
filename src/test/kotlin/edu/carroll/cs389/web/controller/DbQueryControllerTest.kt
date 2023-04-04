package edu.carroll.cs389.web.controller

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@Disabled
@SpringBootTest(classes = [DbQueryController::class])
@AutoConfigureMockMvc
class DbQueryControllerTest {

    @Autowired
    private lateinit var dbMvc: MockMvc

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    @Test
    fun validGetMethodToDbQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidGetMethodToDbQueryTest(): should be 200 when valid URI",
            result.response.status == 200
        )
    }

    @Test
    fun invalidGetMethodToDbQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidGetMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validPostMethodToDbQueryTest() {
        val uri: String = "/ip_info"
        val searchTerm: String = "127.0.0.1"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.post(uri, searchTerm)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPostMethodToDbQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPostMethodToDbQueryTest() {
        val uri: String = "/this_uri_is_invalid"
        val searchTerm: String = "127.0.0.1"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.post(uri, searchTerm)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPostMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    @Test
    fun validPutMethodToDbQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.put(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPutMethodToDbQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPutMethodToDbQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.put(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPutMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validDeleteMethodToDbQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDbQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidDeleteMethodToDbQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

    @Test
    fun nullUriToDbQueryTest() {
        val uri: String = ""

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun sqlStatementAsParameterToDbQueryTest() {
        val uri: String = "/?';select * from tracked_users;'"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDbQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }
}