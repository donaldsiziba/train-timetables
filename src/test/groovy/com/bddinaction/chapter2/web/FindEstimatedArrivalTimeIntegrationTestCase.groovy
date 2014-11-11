package com.bddinaction.chapter2.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
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
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK)

        and: "the arrival time is 8:26"
            final Gson gson =new GsonBuilder().setPrettyPrinting().create()

            final String jsonResponse = gson.toJson(EntityUtils.toString(response.getEntity()))
            logger.info("Json Response: {}", jsonResponse)

            final String strippedJsonResponse = jsonResponse.replace("\"", StringUtils.EMPTY)
            logger.info("Stripped Json Response: {}", strippedJsonResponse)

            LocalTime expectedTime = mapper().readValue(strippedJsonResponse, LocalTime.class)

            assertThat(expectedTime).isEqualTo(new LocalTime(8,26))
    }

    def mapper() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.registerModule(new JodaModule())
        mapper
    }
}
