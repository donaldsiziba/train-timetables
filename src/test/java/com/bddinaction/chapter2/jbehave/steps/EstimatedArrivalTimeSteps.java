package com.bddinaction.chapter2.jbehave.steps;

import com.bddinaction.chapter2.utilities.JsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class EstimatedArrivalTimeSteps {
    final Logger logger = LoggerFactory.getLogger(EstimatedArrivalTimeSteps.class);
    String departure;
    String destination;
    String protocol;
    String host;
    String port;
    String uri;
    CloseableHttpResponse response;

    public EstimatedArrivalTimeSteps() {
        protocol = System.getProperty("protocol");
        logger.info("Reading config property: protocol = {}", protocol);

        host = System.getProperty("host");
        logger.info("Reading config property: host = {}", host);

        port = System.getProperty("port");
        logger.info("Reading config property: port = {}", port);
    }

    @Given("I want to go from <departure> to <destination>")
    public void givenIWantToGoFrom(String departure, String destination) {
        this.departure = departure;
        this.destination = destination;
    }

    @Given("the next train is scheduled to leave at <departure-time> on the <line> line")
    public void givenTheNextTrainIsScheduledToLeave(@Named("departure-time") LocalTime departureTime,
                                                    String line) {
        uri = String.format("%s://%s:%s/train-timetables/itinerary/arrivaltime/line/%s/to/%s/at/%s", protocol, host,
                port, line, destination, departureTime);
        logger.info("URI: {}", uri);
    }

    @When("I ask for my arrival time")
    public void whenIAskForMyArrivalTime() throws IOException {
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        response = client.execute(new HttpGet(uri.replaceAll("\\s", "%20")));
    }

    @Then("the estimated arrival time should be <arrival-time>")
    public void thenTheEstimatedArrivalTimeShouldBe(@Named("arrival-time") LocalTime expectedArrivalTime) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        logger.info("Response: {}", json);

        assertThat(new JsonBuilder().build(json, LocalTime.class))
                .isEqualTo(expectedArrivalTime);
    }
}

