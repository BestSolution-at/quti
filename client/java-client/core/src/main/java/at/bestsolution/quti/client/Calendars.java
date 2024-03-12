package at.bestsolution.quti.client;

import java.util.function.Function;

import at.bestsolution.quti.client.dto.CalendarNewDTO;

public interface Calendars {
    public Calendar calendar(String key);
    public String create(CalendarNewDTO calendar);
    public String create(Function<CalendarNewDTO.Builder, CalendarNewDTO> factory);
}
