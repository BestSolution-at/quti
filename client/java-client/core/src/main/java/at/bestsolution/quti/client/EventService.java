// Generated by RSD - Do not modify
package at.bestsolution.quti.client;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import at.bestsolution.quti.client.dto.EventDTO;
import at.bestsolution.quti.client.dto.EventNewDTO;

public interface EventService extends BaseService {
    public String create(String calendar,EventNewDTO event)
        throws NotFoundException,
            InvalidArgumentException;

    public EventDTO get(String calendar,String key,ZoneId timezone);

    public void delete(String calendar,String key);

    public void cancel(String calendar,String key);

    public void uncancel(String calendar,String key);

    public void move(String calendar,String key,ZonedDateTime start,ZonedDateTime end);

    public void endRepeat(String calendar,String key,LocalDate end);

    public void description(String calendar,String key,String description);

}