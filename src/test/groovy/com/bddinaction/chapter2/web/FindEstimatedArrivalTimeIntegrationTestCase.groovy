package com.bddinaction.chapter2.web

import com.bddinaction.chapter2.utilities.JsonBuilder
import org.apache.http.HttpStatus
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.joda.time.LocalTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 12:50 PM
 */
class FindEstimatedArrivalTimeIntegrationTestCase extends Specification {
    private final Logger logger = LoggerFactory.getLogger(FindEstimatedArrivalTimeIntegrationTestCase.class)

    def "find out what time I will arrive at my destination"() {
        given: "the following resource uri"
            final String uri = "http://localhost:8082/train-timetables/itinerary/arrivaltime/line/North-South/to/Park/at/8:05"

        when: "the resource uri is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(uri))

        then: "the response code should 200"
            response.getStatusLine().getStatusCode() == HttpStatus.SC_OK

        and: "the arrival time is 8:26"
            def json = EntityUtils.toString(response.getEntity())
            logger.info("Response: {}", json)

            new JsonBuilder().build(json, LocalTime.class) == new LocalTime(8, 26)
    }
}
