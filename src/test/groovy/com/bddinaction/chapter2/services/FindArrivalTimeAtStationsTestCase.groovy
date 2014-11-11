package com.bddinaction.chapter2.services

import com.bddinaction.chapter2.model.Line
import org.joda.time.LocalTime
import spock.lang.Specification

class FindArrivalTimeAtStationsTestCase extends Specification {

    def timetableService = new InMemoryTimetableService()

    def "find the correct arrival times between two stations"() {
        given: "the North-South line departing from Hatfield"
            def northSouthLine = Line.named("North-South").departingFrom("Hatfield")

        when: "we find the arrival times at Midrand"
            def arrivalTimes = timetableService.findArrivalTimes(northSouthLine, "Midrand")

        then: "we should get the following times"
            arrivalTimes == [at(7,55), at(8,05), at(8,15), at(8,25), at(8,35), at(8,45)]
    }

    def at(int hour, int minute) {
        new LocalTime(hour, minute)
    }
}

