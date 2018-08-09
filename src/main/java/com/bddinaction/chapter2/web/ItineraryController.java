package com.bddinaction.chapter2.web;

import com.bddinaction.chapter2.services.ItineraryService;
import com.bddinaction.chapter2.services.TimetableService;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 7:28 AM
 */
@RestController
@RequestMapping("/itinerary")
public class ItineraryController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private TimetableService timetableService;

    @GetMapping("/departuretimes/from/{departure}/to/{destination}/at/{time}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<LocalTime> departureTimes(@PathVariable String departure, @PathVariable String destination, @PathVariable String time) {
        return itineraryService.findNextDepartures(departure, destination, LocalTime.parse(time));
    }

    @GetMapping("/arrivaltime/line/{line}/to/{destination}/at/{time}")
    @ResponseStatus(HttpStatus.OK)
   public @ResponseBody LocalTime arrivalTime(@PathVariable String line, @PathVariable String destination,
                                @PathVariable String time) {
       return timetableService.getArrivalTime(line, destination);
   }
}
