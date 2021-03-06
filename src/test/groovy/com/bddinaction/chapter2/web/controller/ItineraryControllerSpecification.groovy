package com.bddinaction.chapter2.web.controller

import com.bddinaction.chapter2.services.ItineraryService
import com.bddinaction.chapter2.services.TimetableService
import com.bddinaction.chapter2.web.ItineraryController
import org.joda.time.LocalTime
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

/**
 * Created by donald on 2015/04/17.
 */
class ItineraryControllerSpecification extends Specification {
    ItineraryController controller = new ItineraryController()
    ItineraryService itineraryService = Mock()
    TimetableService timetableService = Mock()
    MockMvc mockMvc

    def setup() {
        ReflectionTestUtils.setField(controller, "itineraryService", itineraryService)
        ReflectionTestUtils.setField(controller, "timetableService", timetableService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build()
    }

    def "request for departure times"() {
        given:
            LocalTime time = at(8, 0)
            itineraryService.findNextDepartures("Midrand", "Park", time) >> [at(8, 5), at(8, 15), at(8, 25), at(8, 35)]

        when: "the following url is invoked"
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/itinerary/departuretimes/from/Midrand/to/Park/at/8:00"))

        then: "the response code should indicate a success status"
            response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

        and:
            1 * itineraryService.findNextDepartures("Midrand", "Park", time)
    }

    def "request for arrival time"() {
        given:
            timetableService.getArrivalTime("North-South", "Park") >> at(8, 26)

        when: "the following url is invoked"
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/itinerary/arrivaltime/line/North-South/to/Park/at/8:05"))

        then: "the response code should indicate a success status"
            response.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

        and:
            1 * timetableService.getArrivalTime("North-South", "Park")
    }

    def LocalTime at(int hour, int minute) {
        new LocalTime(hour, minute)
    }
}