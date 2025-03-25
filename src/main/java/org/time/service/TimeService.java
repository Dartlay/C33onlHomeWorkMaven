package org.time.service;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class TimeService {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    public String getCurrentTime(String city) {
        ZoneId zoneId = getZoneIdForCity(city);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        return city + " time: " + zonedDateTime.format(formatter);
    }
    private ZoneId getZoneIdForCity(String city) {
        switch (city.toLowerCase()) {
            case "minsk":
                return ZoneId.of("Europe/Minsk");
            case "washington":
                return ZoneId.of("America/New_York");
            case "beijing":
                return ZoneId.of("Asia/Shanghai");
            default:
                return ZoneId.systemDefault();
        }
    }
}