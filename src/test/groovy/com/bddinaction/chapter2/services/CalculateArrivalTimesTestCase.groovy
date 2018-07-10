package com.bddinaction.chapter2.services

import com.bddinaction.chapter2.model.Line
import org.joda.time.LocalTime
import spock.lang.Specification

class CalculateArrivalTimesTestCase extends Specification {

    TimetableService timetableService = Mock(TimetableService)
    ItineraryService itineraryService = new ItineraryService()

    def "calculate the correct arrival time"() {
        given: """North-South line trains from Hatfield leave Midrand for Park at
                  7:55, 8:05, 8:15, 8:25, 8:35, 8:45, 8:55, 9:05"""
            itineraryService.setTimetableService(timetableService)
            def northSouthLineFromHatfield = Line.named("North-South").departingFrom("Hatfield")
            timetableService.findLinesThrough("Midrand","Park") >> [northSouthLineFromHatfield]
            timetableService.findArrivalTimes(northSouthLineFromHatfield, "Midrand") >> [at(7,55), at(8,05), at(8,15), at(8,25), at(8,35), at(8,45), at(8,55), at(9,05)]

         when: "I want to travel from Midrand to Park at 8:00"
            def proposedTrainTimes = itineraryService.findNextDepartures("Midrand", "Park", at(8,00))

        then: "I should be told about the trains at: 8:05, 8:15, 8:25, 8:35"
            proposedTrainTimes == [at(8,05), at(8,15), at(8,25), at(8,35)]
    }

    def at(int hour, int minute) {
        new LocalTime(hour, minute)
    }
}