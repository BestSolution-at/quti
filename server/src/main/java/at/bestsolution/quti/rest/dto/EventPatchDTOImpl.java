// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.ZonedDateTime;
import java.util.EnumSet;

import at.bestsolution.quti.service.dto.EventDTO;

public class EventPatchDTOImpl implements EventDTO.Patch {
    private String key;
    private String title;
    private String description;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private boolean fullday;
    private EventRepeatPatchDTOImpl repeat;

    private final EnumSet<Props> dataSet = EnumSet.noneOf(Props.class);

    public EventPatchDTOImpl() {}

    @Override
    public boolean isSet(EventDTO.Patch.Props prop) {
        return dataSet.contains(prop);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return this.key;
    }
    public void setTitle(String title) {
        this.title = title;
        this.dataSet.add(Props.TITLE);
    }

    @Override
    public String title() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
        this.dataSet.add(Props.DESCRIPTION);
    }

    @Override
    public String description() {
        return this.description;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
        this.dataSet.add(Props.START);
    }

    @Override
    public ZonedDateTime start() {
        return this.start;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
        this.dataSet.add(Props.END);
    }

    @Override
    public ZonedDateTime end() {
        return this.end;
    }

    public void setFullday(boolean fullday) {
        this.fullday = fullday;
        this.dataSet.add(Props.FULLDAY);
    }

    @Override
    public boolean fullday() {
        return this.fullday;
    }

    public void setRepeat(EventRepeatPatchDTOImpl repeat) {
        this.repeat = repeat;
        this.dataSet.add(Props.REPEAT);
    }

    @Override
    public EventRepeatPatchDTOImpl repeat() {
        return this.repeat;
    }

}
