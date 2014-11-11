package com.bddinaction.chapter2.web;

import com.bddinaction.chapter2.services.ItineraryService;
import com.bddinaction.chapter2.services.TimetableService;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 7:28 AM
 */
@Controller
@RequestMapping("/itinerary")
public class ItineraryController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private TimetableService timetableService;

    @RequestMapping(method = RequestMethod.GET, value = "/departuretimes/from/{departure}/to/{destination}/at/{time}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LocalTime> departureTimes(@PathVariable String departure, @PathVariable String destination,
                                      @PathVariable String time) {
        return itineraryService.findNextDepartures(departure, destination, LocalTime.parse(time));
    }

    @RequestMapping(method = RequestMethod.GET, value = "arrivaltime/line/{line}/to/{destination}/at/{time}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
   public LocalTime arrivalTime(@PathVariable String line, @PathVariable String destination,
                                @PathVariable String time) {
       return timetableService.getArrivalTime(line, destination);
   }
}
