import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.quti.client.jdkhttp.JDKQutiClient;

public class Sample {
    public static void main(String[] args) {
        var client = JDKQutiClient.create(URI.create("http://localhost:8080/api/calendar"));
        
        var calendar = client.calendars().calendar("4640e34c-fd3d-4ee6-b83d-d5641d279ad7");
        /*var key = client.calendars().create( builder -> {
            return builder
                .name("Demo")
                .owner("quti@quti.dev")
                .build();
        });
        System.err.println(key);
        
        var event = calendar.events().create(builder -> {
            return builder
                .title("Sample Event")
                .description("A sample event")
                .start(ZonedDateTime.parse("2024-03-01T10:00:00+01:00[Europe/Paris]"))
                .end(ZonedDateTime.parse("2024-03-01T14:00:00+01:00[Europe/Paris]"))
                .withRepeat(EventRepeatWeeklyDTO.Builder.class, rBuilder -> {
                    return rBuilder
                        .interval((short)1)
                        .timeZone(ZoneId.of("Europe/Paris"))
                        .daysOfWeek(List.of(DayOfWeek.FRIDAY))
                        .build();
                })
                .build();
        });
        System.err.println(event);*/
        
        var events = calendar.eventView(
            LocalDate.of(2024, 2, 1), 
            LocalDate.of(2024, 4, 29), 
            ZoneId.systemDefault()
        );
        events.stream()
            .map( e -> String.format("%s %s - %s: %s", e.start().toLocalDate(), e.start().toLocalTime(), e.end().toLocalTime(), e.title()))
            .forEach(System.err::println);
    }
}
