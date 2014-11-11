package com.bddinaction.chapter2.services.smokescreen

import org.apache.http.HttpStatus
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Specification

import static org.fest.assertions.Assertions.assertThat

/**
 * User: donald
 * Date: 2014/06/28
 * Time: 9:44 PM
 */
class ApplicationSmokeScreenIntegrationTestCase extends Specification {
    def "smoke screen test"() {
        given: "the following home page url"
            final String url = "http://localhost:8082/train-timetables/index.jsp"

        when: "the home page url is invoked"
            final CloseableHttpClient client = HttpClientBuilder.create().build()
            final CloseableHttpResponse response = client.execute(new HttpGet(url))

        then: "the response should be 200"
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK)
    }
}
