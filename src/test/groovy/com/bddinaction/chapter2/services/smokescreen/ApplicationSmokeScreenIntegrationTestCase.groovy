package com.bddinaction.chapter2.services.smokescreen

import org.apache.http.HttpStatus
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Specification

/**
 * User: donald
 * Date: 2014/06/28
 * Time: 9:44 PM
 */
class ApplicationSmokeScreenIntegrationTestCase extends Specification {
    String protocol
    String host
    String port

    def setup() {
        protocol = System.getProperty("protocol")
        host = System.getProperty("host")
        port = System.getProperty("port")
    }
    def "smoke screen test"() {
        given: "the following home page url"
            final String url = "$protocol://$host:$port/train-timetables/index.jsp"

        when: "the home page url is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(url))

        then: "the response should be 200"
            response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
    }
}
