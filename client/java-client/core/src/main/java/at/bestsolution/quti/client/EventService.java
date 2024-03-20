package at.bestsolution.quti.client;

import java.time.ZonedDateTime;

public interface EventService {
    public void delete();
    public void move(ZonedDateTime start, ZonedDateTime end);
    public void cancel();
}