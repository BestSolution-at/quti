package at.bestsolution.quti.client;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.client.dto.CalendarDTO;
import at.bestsolution.quti.client.dto.EventViewDTO;

public interface Calendar {
    public Events events();
    public Event event(String key);

    public CalendarDTO get();
    public List<EventViewDTO> eventView(LocalDate start, LocalDate end, ZoneId timezone);
}
