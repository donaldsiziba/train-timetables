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

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 12:24 PM
 */
class FindArrivalTimesIntegrationTestCase extends Specification {
    private final Logger logger = LoggerFactory.getLogger(FindArrivalTimesIntegrationTestCase.class)
    String protocol
    String host
    String port

    def setup() {
        protocol = System.getProperty("protocol")
        host = System.getProperty("host")
        port = System.getProperty("port")
    }

    def "find out what time the next trains for my destination station leave"() {
        given: "the following resource uri"
            final String uri = "$protocol://$host:$port/train-timetables/itinerary/departuretimes/from/Midrand/to/Park/at/8:00"

        when: "the resource uri is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(uri))

        then: "the response code should 200"
            response.getStatusLine().getStatusCode() == HttpStatus.SC_OK

        and: "the proposed times should be equal to the actual times"

            String json = EntityUtils.toString(response.getEntity())
            logger.info("Response: {}", json)

            new JsonBuilder().build(json, new TypeReference<List<LocalTime>>() {}) == [at(8, 5), at(8,15), at(8,25), at(8,35)]
    }

    def at(int hour, int minute) {
        new LocalTime(hour, minute)
    }
}
