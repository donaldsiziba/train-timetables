package com.bddinaction.chapter2.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.joda.time.LocalTime;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: donald
 * Date: 2014/06/29
 * Time: 11:03 AM
 */
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String uri = "http://localhost:10080/train-timetables/itinerary/arrivaltime/line/Airport/to/OR Tambo/at/08:10:00.000";
        final String encodedUri = URLEncoder.encode(uri, "UTF-8");
        System.out.println(encodedUri);
    }

    private static LocalTime at(int hour, int minute) {
        return new LocalTime(hour, minute);
    }
}
