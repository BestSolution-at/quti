// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.service.dto.SeriesMovedEventViewDTO;

public class SeriesMovedEventViewDTOImpl extends EventViewDTOImpl implements SeriesMovedEventViewDTO {
	public String masterEventKey;
	public ZonedDateTime originalStart;
	public ZonedDateTime originalEnd;

	public String masterEventKey() {
		return this.masterEventKey;
	}

	public ZonedDateTime originalStart() {
		return this.originalStart;
	}

	public ZonedDateTime originalEnd() {
		return this.originalEnd;
	}

	public static SeriesMovedEventViewDTOImpl of(SeriesMovedEventViewDTO source) {
		if (source == null) {
			return null;
		} else if (source instanceof SeriesMovedEventViewDTOImpl) {
			return (SeriesMovedEventViewDTOImpl) source;
		}
		var rv = new SeriesMovedEventViewDTOImpl();
		rv.masterEventKey = source.masterEventKey();
		rv.originalStart = source.originalStart();
		rv.originalEnd = source.originalEnd();
		rv.key = source.key();
		rv.calendarKey = source.calendarKey();
		rv.title = source.title();
		rv.description = source.description();
		rv.owner = source.owner();
		rv.status = source.status();
		rv.start = source.start();
		rv.end = source.end();
		rv.tags = source.tags();
		rv.referencedCalendars = source.referencedCalendars();
		return rv;
	}

	public static class BuilderImpl extends EventViewDTOImpl.BuilderImpl implements SeriesMovedEventViewDTO.Builder {
		public String masterEventKey;
		public ZonedDateTime originalStart;
		public ZonedDateTime originalEnd;
		public String key;
		public String calendarKey;
		public String title;
		public String description;
		public String owner;
		public Status status;
		public ZonedDateTime start;
		public ZonedDateTime end;
		public List<String> tags;
		public List<String> referencedCalendars;

		public SeriesMovedEventViewDTO.Builder masterEventKey(String masterEventKey) {
			this.masterEventKey = masterEventKey;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder originalStart(ZonedDateTime originalStart) {
			this.originalStart = originalStart;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder originalEnd(ZonedDateTime originalEnd) {
			this.originalEnd = originalEnd;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder key(String key) {
			this.key = key;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder calendarKey(String calendarKey) {
			this.calendarKey = calendarKey;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder title(String title) {
			this.title = title;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder description(String description) {
			this.description = description;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder owner(String owner) {
			this.owner = owner;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder status(Status status) {
			this.status = status;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder start(ZonedDateTime start) {
			this.start = start;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder end(ZonedDateTime end) {
			this.end = end;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder tags(List<String> tags) {
			this.tags = tags;
			return this;
		}

		public SeriesMovedEventViewDTO.Builder referencedCalendars(List<String> referencedCalendars) {
			this.referencedCalendars = referencedCalendars;
			return this;
		}

		public at.bestsolution.quti.service.dto.SeriesMovedEventViewDTO build() {
			var rv = new SeriesMovedEventViewDTOImpl();
			rv.masterEventKey = masterEventKey;
			rv.originalStart = originalStart;
			rv.originalEnd = originalEnd;
			rv.key = key;
			rv.calendarKey = calendarKey;
			rv.title = title;
			rv.description = description;
			rv.owner = owner;
			rv.status = status;
			rv.start = start;
			rv.end = end;
			rv.tags = tags;
			rv.referencedCalendars = referencedCalendars;
			return rv;
		}
	}

	public static SeriesMovedEventViewDTO.Builder builder() {
		return new BuilderImpl();
	}
}
