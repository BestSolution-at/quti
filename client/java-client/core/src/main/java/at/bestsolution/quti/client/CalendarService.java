package at.bestsolution.quti.client;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Function;

import at.bestsolution.quti.client.dto.CalendarDTO;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventViewDTO;

public interface CalendarService extends BaseService {
    public String create(CalendarNewDTO calendar);
    public String create(Function<CalendarNewDTO.Builder, CalendarNewDTO> factory);
    
    public List<EventViewDTO> eventView(String key, LocalDate start, LocalDate end, ZoneId timezone);
    public CalendarDTO get(String key);
    
}
