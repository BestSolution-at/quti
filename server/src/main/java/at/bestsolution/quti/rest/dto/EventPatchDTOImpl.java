package at.bestsolution.quti.rest.dto;

import java.time.ZonedDateTime;
import java.util.EnumSet;

import at.bestsolution.quti.service.dto.EventDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO;

public class EventPatchDTOImpl implements EventDTO.Patch {
	private final EnumSet<Props> dataSet = EnumSet.noneOf(Props.class);

	private String key;
	private String title;
	private String description;
	private ZonedDateTime start;
	private ZonedDateTime end;
	private boolean fullday;
	private EventRepeatDTO.Patch repeat;

	public EventPatchDTOImpl() {}

	public EventPatchDTOImpl(String key) {
		this.key = key;
	}

	@Override
	public boolean isSet(Props prop) {
		return dataSet.contains(prop);
	}

	@Override
	public String key() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String title() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.dataSet.add(Props.TITLE);
	}

	@Override
	public String description() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.dataSet.add(Props.DESCRIPTION);
	}

	@Override
	public ZonedDateTime start() {
		return this.start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
		this.dataSet.add(Props.START);
	}

	@Override
	public ZonedDateTime end() {
		return this.end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
		this.dataSet.add(Props.END);
	}

	@Override
	public boolean fullday() {
		return this.fullday;
	}

	public void setFullday(boolean fullday) {
		this.fullday = fullday;
		this.dataSet.add(Props.FULLDAY);
	}

	@Override
	public EventRepeatDTO.Patch repeat() {
		return this.repeat;
	}

	public void setRepeat(EventRepeatDTO.Patch repeat) {
		this.repeat = repeat;
		this.dataSet.add(Props.REPEAT);
	}
}
