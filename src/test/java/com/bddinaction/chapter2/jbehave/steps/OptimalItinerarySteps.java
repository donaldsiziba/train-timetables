package com.bddinaction.chapter2.jbehave.steps;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class OptimalItinerarySteps {
    final Logger logger = LoggerFactory.getLogger(OptimalItinerarySteps.class);
    String port;
    String uri;
    CloseableHttpResponse response;

    public OptimalItinerarySteps() {
        port = System.getProperty("port");
        logger.info("Reading config property: port = {}", port);
    }

    @Given("$line line trains from $lineStart leave $departure for $destination at $departureTimes")
    public void givenArrivingTrains(String line,
                                    String lineStart,
                                    String departure,
                                    String destination,
                                    List<LocalTime> departureTimes) {
    }

    @When("I want to travel from $departure to $destination at $startTime")
    public void whenIWantToTravel(String departure, String destination, LocalTime startTime) throws IOException {
        uri = String.format("http://localhost:%s/train-timetables/itinerary/departuretimes/from/%s/to/%s/at/%s", port,
                departure, destination, startTime);

        logger.info("URI: {}", uri);

        final CloseableHttpClient client = HttpClientBuilder.create().build();
        response = client.execute(new HttpGet(uri));
    }

    @Then("I should be told about the trains at: $expectedTrainTimes")
    public void shouldBeInformedAbout(List<LocalTime> expectedTrainTimes) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final String jsonResponse = gson.toJson(EntityUtils.toString(response.getEntity()));
        logger.info("Json Response: {}", jsonResponse);

        final String strippedJsonResponse = jsonResponse.replace("\"", StringUtils.EMPTY);
        logger.info("Stripped Json Response: {}", strippedJsonResponse);

        List<LocalTime> proposedTrainTimes = mapper().readValue(strippedJsonResponse, new TypeReference<List<LocalTime>>() {
        });
        assertThat(proposedTrainTimes).isEqualTo(expectedTrainTimes);
    }

    private ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper;
    }
}

