package com.bddinaction.chapter2.web.controller;

import com.bddinaction.chapter2.services.ItineraryService;
import com.bddinaction.chapter2.services.TimetableService;
import com.bddinaction.chapter2.web.ItineraryController;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 8:33 AM
 */
@Ignore
public class ItineraryControllerTestCase {
    MockMvc mockMvc;

    @InjectMocks
    ItineraryController controller;

    @Mock
    ItineraryService itineraryService;

    @Mock
    TimetableService timetableService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void departureTimes() throws Exception {
        LocalTime time = at(8, 0);
        when(itineraryService.findNextDepartures("Midrand", "Park", time)).thenReturn(new ArrayList<>(){{
            add(at(8, 5));
            add(at(8,15));
            add(at(8,25));
            add(at(8,35));
        }});

        mockMvc.perform(get("/itinerary/departuretimes/from/Midrand/to/Park/at/8:00"))
                .andExpect(status().is2xxSuccessful());

        verify(itineraryService).findNextDepartures("Midrand", "Park", time);
    }

    @Test
    public void arrivalTime() throws Exception {
        when(timetableService.getArrivalTime("North-South", "Park")).thenReturn(at(8, 26));

        mockMvc.perform(get("/itinerary/arrivaltime/line/North-South/to/Park/at/8:05"))
                    .andExpect(status().is2xxSuccessful());

        verify(timetableService).getArrivalTime("North-South", "Park");
    }

    private LocalTime at(int hour, int minute) {
        return new LocalTime(hour, minute);
    }
}
