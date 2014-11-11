package com.bddinaction.chapter2.services

import com.bddinaction.chapter2.model.Line
import spock.lang.Specification
import spock.lang.Unroll

class FindLinesThroughStationsTestCase extends Specification {

    @Unroll("The line between #departure and #destination should leave #lineName from #lineDeparture")
    def "find the correct lines between two stations"() {
        given:
            InMemoryTimetableService timetableService = new InMemoryTimetableService()
        when:
            List<Line> lines = timetableService.findLinesThrough(departure, destination)
        then:
            Line expectedLine = Line.named(lineName).departingFrom(lineDeparture)
            lines == [expectedLine]
        where:
            departure     | destination    | lineName      | lineDeparture
            "Midrand"     | "Park"         | "North-South" | "Hatfield"
            "Sandton"     | "Rhodesfield"  | "East-West"   | "Sandton"
            "Malboro"     | "OR Tambo"     | "Aiport"      | "Sandton"
    }
}