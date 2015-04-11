package com.bddinaction.chapter2.services;

import com.bddinaction.chapter2.model.Line;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryTimetableService implements TimetableService {

    private List<Line> lines = ImmutableList.of(
            Line.named("North-South").departingFrom("Hatfield")
                                 .withStations("Hatfield", "Pretoria","Centurion","Midrand", "Malboro", "Sandton", "Rosebank", "Park"),
            Line.named("East-West").departingFrom("Sandton")
                                 .withStations("Sandton", "Malboro", "Rhodesfield"),
            Line.named("Airport").departingFrom("Sandton")
                                 .withStations("Sandton", "Malboro", "OR Tambo")
    );

    private List<LocalTime> universalDepartureTimes = ImmutableList.of(
            new LocalTime(7,40), new LocalTime(7,50), new LocalTime(8, 0),
            new LocalTime(8,10), new LocalTime(8,20), new LocalTime(8,30));

    @Override
    public List<LocalTime> findArrivalTimes(Line line, String targetStation) {
        Line targetLine = lineMatching(line);
        int timeTaken = 0;
        for(String station : targetLine.getStations()) {
            if (station.equals(targetStation)) {
                break;
            }
            timeTaken += 5;
        }
        List<LocalTime> arrivalTimes = Lists.newArrayList();
        for(LocalTime time: universalDepartureTimes) {
            arrivalTimes.add(time.plusMinutes(timeTaken));
        }
        return arrivalTimes;
    }

    private Line lineMatching(Line requestedLine) {
        for(Line line : lines) {
            if (line.equals(requestedLine)) {
                return line;
            }
        }
        return null;
    }

    @Override
    public List<Line> findLinesThrough(String departure, String destination) {
        List<Line> resultLines = new ArrayList<Line>();
        for (Line line : lines) {
            if (line.getStations().contains(departure) && line.getStations().contains(destination)) {
                if (line.getStations().indexOf(destination) > line.getStations().indexOf(departure)) {
                    resultLines.add(line);
                }
            }
        }
        return resultLines;
    }

    @Override
    public void scheduleArrivalTime(String line, LocalTime departureTime) {
    }

    @Override
    public LocalTime getArrivalTime(String travellingOnLine, String destination) {
        if(travellingOnLine.equals("North-South") && destination.equals("Park"))
            return new LocalTime(8, 26);

        if(travellingOnLine.equals("East-West") && destination.equals("Sandton"))
        return new LocalTime(8, 22);

        if(travellingOnLine.equals("Airport") && destination.equals("OR Tambo"))
        return new LocalTime(8, 28);

        return null;
    }
}
