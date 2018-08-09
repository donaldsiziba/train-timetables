package com.bddinaction.chapter2.jbehave.steps;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
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

    String protocol;
    String host;
    String port;

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
        Serenity.getCurrentSession().put("departure", departure);
        Serenity.getCurrentSession().put("destination", destination);
    }

    @Given("the next train is scheduled to leave at <departure-time> on the <line> line")
    public void givenTheNextTrainIsScheduledToLeave(@Named("departure-time") LocalTime departureTime, String line) {
        Serenity.getCurrentSession().put("uri", String.format("%s://%s:%s/itinerary/arrivaltime/line/%s/to/%s/at/%s", protocol, host, port, line, Serenity.getCurrentSession().get("destination").toString(), departureTime));
    }

    @When("I ask for my arrival time")
    public void whenIAskForMyArrivalTime() throws IOException {
        SerenityRest.when()
                        .get(Serenity.getCurrentSession().get("uri").toString());
    }

    @Then("the estimated arrival time should be <arrival-time>")
    public void thenTheEstimatedArrivalTimeShouldBe(@Named("arrival-time") LocalTime expectedArrivalTime) throws IOException {
        assertThat(SerenityRest.then()
                .extract().response().body().as(LocalTime.class)).
        isEqualTo(expectedArrivalTime);
    }
}

