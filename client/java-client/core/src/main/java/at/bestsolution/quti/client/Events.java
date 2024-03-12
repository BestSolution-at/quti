package at.bestsolution.quti.client;

import java.util.function.Function;

import at.bestsolution.quti.client.dto.EventNewDTO;

public interface Events {
    public Event event(String key);
    public String create(EventNewDTO newEvent);
    public String create(Function<EventNewDTO.Builder, EventNewDTO> factory);
}
