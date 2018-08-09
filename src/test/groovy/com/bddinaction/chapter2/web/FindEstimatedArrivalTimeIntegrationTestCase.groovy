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
import spock.lang.Unroll

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 12:50 PM
 */
class FindEstimatedArrivalTimeIntegrationTestCase extends Specification {
    private final Logger logger = LoggerFactory.getLogger(FindEstimatedArrivalTimeIntegrationTestCase.class)
    String protocol
    String host
    String port

    def setup() {
        protocol = System.getProperty("protocol")
        host = System.getProperty("host")
        port = System.getProperty("port")
    }

    @Unroll("The estimated arrival time on the #line line departing #departure at #depatureTime destined for #destination")
    def "find out what time I will arrive at my destination"() {
        given: "the following resource uri"
            final String uri = "$protocol://$host:$port/itinerary/arrivaltime/line/$line/to/$destination/at/$depatureTime".replaceAll("\\s", "%20")

        when: "the resource uri is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(uri))

        then: "the response code should 200"
            response.getStatusLine().getStatusCode() == HttpStatus.SC_OK

        and: "the arrival time is $arrivalTime"
            def json = EntityUtils.toString(response.getEntity())
            logger.info("Response: {}", json)

        new JsonBuilder().build(json, LocalTime.class) == at(arrivalTime)

        where:
        departure     | destination | depatureTime | line          | arrivalTime
        'Midrand'     | 'Park'      | '8:05'       | 'North-South' | '8:26'
        'Rhodesfield' | 'Sandton'   | '8:10'       | 'East-West'   | '8:22'
        'Malboro'     | 'OR Tambo'  | '8:15'       | 'Airport'     | '8:28'
    }

    def at(String time) {
        def hour = Integer.valueOf(time.split(':')[0])
        def minute = Integer.valueOf(time.split(':')[1])
        new LocalTime(hour, minute)
    }
}
