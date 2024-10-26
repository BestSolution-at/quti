package at.bestsolution.quti;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.client.jdkhttp.JDKQutiClient;

public class Main {
    public static void main(String[] args) {
        var client = JDKQutiClient.create(URI.create("http://localhost:8080"));
        var calendarService = client.service(CalendarService.class);
        var eventService = client.service(EventService.class);
        
        var calendarBuilder = client.builder(CalendarNewDTO.Builder.class);
        var calendarNewDto = calendarBuilder
            .name("Sample Calendar")
            .build();
        var calendarId = calendarService.create(calendarNewDto);
        System.err.println("Created calendar '%s'".formatted(calendarId));
        
        var calendar = calendarService.get(calendarId);
        System.err.println("Loaded calendar - Name: %s".formatted(calendar.name()));

        var eventBuilder = client.builder(EventNewDTO.Builder.class);
        var eventNewDto = eventBuilder
            .title("Sample Event")
            .description("This is a sample event")
            .start(ZonedDateTime.parse("2024-01-01T10:00:00+01:00[Europe/Vienna]"))
            .end(ZonedDateTime.parse("2024-01-01T12:00:00+01:00[Europe/Vienna]"))
            .withRepeat(EventRepeatDailyDTO.Builder.class, b -> {
                return b
                    .endDate(LocalDate.parse("2024-05-01"))
                    .interval((short)1)
                    .timeZone(ZoneId.of("Europe/Vienna"))
                    .build();
            })
            .build();
        var eventId = eventService.create(calendarId, eventNewDto);
        
        eventService.cancel(calendarId, eventId+"_2024-01-02");
        eventService.move(calendarId, 
            eventId+"_2024-01-03", 
            ZonedDateTime.parse("2024-01-03T15:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-03T17:00:00+01:00[Europe/Vienna]"));
        
        var events = calendarService.eventView(
            calendarId, 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-01-05"), 
            ZoneId.of("Europe/Vienna"));
        
        events.forEach( e -> {
            System.err.println(e.title() + ":" + e.start() + " - " + e.end() + " - " + e.status());
        });
        System.err.println("========");
        eventService.uncancel(calendarId, eventId+"_2024-01-02");
        events = calendarService.eventView(
            calendarId, 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-01-05"), 
            ZoneId.of("Europe/Vienna"));
        
        events.forEach( e -> {
            System.err.println(e.title() + ":" + e.start() + " - " + e.end() + " - " + e.status());
        });
    }
}