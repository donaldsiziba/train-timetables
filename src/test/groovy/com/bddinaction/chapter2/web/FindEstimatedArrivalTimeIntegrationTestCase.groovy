package com.bddinaction.chapter2.web

import org.apache.http.HttpStatus
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
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

    @Unroll("The estimated arrival time on the #line departing #departure at #depatureTime destined for #destination")
    def "find out what time I will arrive at my destination"() {
        given: "the following resource uri"
            final String uri = "$protocol://$host:$port/train-timetables/itinerary/arrivaltime/line/$line/to/$destination/at/$depatureTime".replaceAll("\\s", "%20")

        when: "the resource uri is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(uri))

        then: "the response code should 200"
            response.getStatusLine().getStatusCode() == HttpStatus.SC_OK

        and: "the arrival time is 8:26"
            def json = EntityUtils.toString(response.getEntity())
            logger.info("Response: {}", json)

            json == arrivalTime

        where:
        departure     | destination | depatureTime | line          | arrivalTime
        'Midrand'     | 'Park'      | '8:05'       | 'North-South' | '[ 8, 26, 0, 0 ]'
        'Rhodesfield' | 'Sandton'   | '8:10'       | 'East-West'   | '[ 8, 22, 0, 0 ]'
        'Malboro'     | 'OR Tambo'  | '8:15'       | 'Airport'     | '[ 8, 28, 0, 0 ]'
    }
}
