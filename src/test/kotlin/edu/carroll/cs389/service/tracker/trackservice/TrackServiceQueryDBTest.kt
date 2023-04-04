package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.jpa.repo.TrackerRepo
import edu.carroll.cs389.service.tracker.TrackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

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

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

}