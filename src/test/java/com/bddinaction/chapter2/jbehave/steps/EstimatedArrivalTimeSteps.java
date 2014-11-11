package com.bddinaction.chapter2.jbehave.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.*;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;


public class EstimatedArrivalTimeSteps {
    final Logger logger = LoggerFactory.getLogger(EstimatedArrivalTimeSteps.class);
    String departure;
    String destination;
    String port;
    String uri;
    CloseableHttpResponse response;

    public EstimatedArrivalTimeSteps() {
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
        uri = String.format("http://localhost:%s/train-timetables/itinerary/arrivaltime/line/%s/to/%s/at/%s", port,
                                line, destination, departureTime);
        logger.info("URI: {}", uri);
    }

    @When("I ask for my arrival time")
    public void whenIAskForMyArrivalTime() throws IOException {
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        response = client.execute(new HttpGet(uri.replaceAll("\\s", "%20")));
    }

    @Then("the estimated arrival time should be <arrival-time>")
    public void thenTheEstimatedArrivalTimeShouldBe(@Named("arrival-time") LocalTime expectedArrivalTime) throws IOException {
        final Gson gson =new GsonBuilder().setPrettyPrinting().create();

        final String jsonResponse = gson.toJson(EntityUtils.toString(response.getEntity()));
        logger.info("Json Response: {}", jsonResponse);

        final String strippedJsonResponse = jsonResponse.replace("\"", StringUtils.EMPTY);
        logger.info("Stripped Json Response: {}", strippedJsonResponse);

        LocalTime arrivalTime = mapper().readValue(strippedJsonResponse, LocalTime.class);

        assertThat(arrivalTime).isEqualTo(expectedArrivalTime);
    }

    private ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper;
    }
}

