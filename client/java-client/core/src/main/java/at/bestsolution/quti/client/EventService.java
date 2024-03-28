package at.bestsolution.quti.client;

import java.time.ZonedDateTime;
import java.util.function.Function;

import at.bestsolution.quti.client.dto.EventNewDTO;

public interface EventService extends BaseService {
    public String create(String calendarKey, EventNewDTO newEvent);
    public String create(String calendarKey, Function<EventNewDTO.Builder, EventNewDTO> factory);
    
    public void delete(String calendarKey, String key);
    public void move(String calendarKey, String key, ZonedDateTime start, ZonedDateTime end);
    public void cancel(String calendarKey, String key);
}