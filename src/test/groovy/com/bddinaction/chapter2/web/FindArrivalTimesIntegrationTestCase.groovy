package com.bddinaction.chapter2.web

import com.bddinaction.chapter2.utilities.JsonBuilder
import com.fasterxml.jackson.core.type.TypeReference
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

import static org.fest.assertions.Assertions.assertThat

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 12:24 PM
 */
class FindArrivalTimesIntegrationTestCase extends Specification {
    private final Logger logger = LoggerFactory.getLogger(FindArrivalTimesIntegrationTestCase.class)

    def "find out what time the next trains for my destination station leave"() {
        given: "the following resource uri"
            final String uri = "http://localhost:8082/train-timetables/itinerary/departuretimes/from/Midrand/to/Park/at/8:00"

        when: "the resource uri is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(uri))

        then: "the response code should 200"
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK)

        and: "the proposed times should be equal to the actual times"
            def actualTimes = new ArrayList<LocalTime>();
            actualTimes.add(at(8, 5));
            actualTimes.add(at(8,15));
            actualTimes.add(at(8,25));
            actualTimes.add(at(8,35));

            def json = EntityUtils.toString(response.getEntity())
            logger.info("Response: {}", json)

            new JsonBuilder().build(json, new TypeReference<List<LocalTime>>() {}) == actualTimes
    }

    def at(int hour, int minute) {
        new LocalTime(hour, minute)
    }
}
