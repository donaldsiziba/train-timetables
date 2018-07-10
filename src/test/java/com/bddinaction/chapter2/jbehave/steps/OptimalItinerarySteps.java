package com.bddinaction.chapter2.jbehave.steps;

import com.bddinaction.chapter2.utilities.JsonBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import net.serenitybdd.rest.SerenityRest;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimalItinerarySteps {
    final Logger logger = LoggerFactory.getLogger(OptimalItinerarySteps.class);
    String protocol;
    String host;
    String port;

    public OptimalItinerarySteps() {
        protocol = System.getProperty("protocol");
        logger.info("Reading config property: protocol = {}", protocol);

        host = System.getProperty("host");
        logger.info("Reading config property: host = {}", host);

        port = System.getProperty("port");
        logger.info("Reading config property: port = {}", port);
    }

    @Given("$line line trains from $lineStart leave $departure for $destination at $departureTimes")
    public void givenArrivingTrains(String line, String lineStart, String departure, String destination, List<LocalTime> departureTimes) {
    }

    @When("I want to travel from $departure to $destination at $startTime")
    public void whenIWantToTravel(String departure, String destination, LocalTime startTime) throws IOException {
        SerenityRest.when()
                        .get(String.format("%s://%s:%s/train-timetables/itinerary/departuretimes/from/%s/to/%s/at/%s", protocol, host, port, departure, destination, startTime));
    }

    @Then("I should be told about the trains at: $expectedTrainTimes")
    public void shouldBeInformedAbout(List<LocalTime> expectedTrainTimes) throws IOException {
        assertThat(new JsonBuilder().<List<LocalTime>>build(SerenityRest.then().extract().response().asString(), new TypeReference<List<LocalTime>>() {})).isEqualTo(expectedTrainTimes);
    }
}

