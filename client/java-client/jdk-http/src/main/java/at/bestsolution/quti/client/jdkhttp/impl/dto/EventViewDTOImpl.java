// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.dto.EventViewDTO;
import at.bestsolution.quti.client.dto.SeriesEventViewDTO;
import at.bestsolution.quti.client.dto.SeriesMovedEventViewDTO;
import at.bestsolution.quti.client.dto.SingleEventViewDTO;
import at.bestsolution.quti.client.dto.MixinEventViewDataDTO.Status;

public abstract class EventViewDTOImpl extends BaseDTOImpl implements EventViewDTO {
	EventViewDTOImpl(JsonObject data) {
		super(data);
	}

	@Override
	public String key() {
		return DTOUtils.mapString(data, "key");
	}

	@Override
	public String calendarKey() {
		return DTOUtils.mapString(data, "calendarKey");
	}

	@Override
	public String title() {
		return DTOUtils.mapString(data, "title");
	}

	@Override
	public String description() {
		return DTOUtils.mapString(data, "description");
	}

	@Override
	public String owner() {
		return DTOUtils.mapString(data, "owner");
	}

	@Override
	public Status status() {
		return DTOUtils.mapLiteral(data, "status", Status::valueOf);
	}

	@Override
	public ZonedDateTime start() {
		return DTOUtils.mapZonedDateTime(data, "start");
	}

	@Override
	public ZonedDateTime end() {
		return DTOUtils.mapZonedDateTime(data, "end");
	}

	@Override
	public List<String> tags() {
		return DTOUtils.mapStrings(data, "tags");
	}

	@Override
	public List<String> referencedCalendars() {
		return DTOUtils.mapStrings(data, "referencedCalendars");
	}

	public static EventViewDTO of(JsonObject data) {
		var descriminator = data.getString("@type");
		return switch (descriminator) {
			case "single" -> new SingleEventViewDTOImpl(data);
			case "series-moved" -> new SeriesMovedEventViewDTOImpl(data);
			case "series" -> new SeriesEventViewDTOImpl(data);
			default -> throw new IllegalArgumentException("Unexpected value: %s".formatted(descriminator));
		};
	}

	public static List<EventViewDTO> of(JsonArray data) {
		return DTOUtils.mapObjects(data, EventViewDTOImpl::of);
	}

	@Override
	public String toString() {
		return "%s[%s=%s]".formatted(getClass().getSimpleName(), "key", key());
	}

	public static abstract class BuilderImpl<T extends EventViewDTO> implements EventViewDTO.Builder {
		protected final JsonObjectBuilder $builder = Json.createObjectBuilder();

		@Override
		public Builder key(String key) {
			$builder.add("key", key);
			return this;
		}

		@Override
		public Builder calendarKey(String calendarKey) {
			$builder.add("calendarKey", calendarKey);
			return this;
		}

		@Override
		public Builder title(String title) {
			$builder.add("title", title);
			return this;
		}

		@Override
		public Builder description(String description) {
			$builder.add("description", description);
			return this;
		}

		@Override
		public Builder owner(String owner) {
			$builder.add("owner", owner);
			return this;
		}

		@Override
		public Builder status(Status status) {
			$builder.add("status", status.toString());
			return this;
		}

		@Override
		public Builder start(ZonedDateTime start) {
			$builder.add("start", start.toString());
			return this;
		}

		@Override
		public Builder end(ZonedDateTime end) {
			$builder.add("end", end.toString());
			return this;
		}

		@Override
		public Builder tags(List<String> tags) {
			$builder.add("tags", DTOUtils.toJsonStringArray(tags));
			return this;
		}

		@Override
		public Builder referencedCalendars(List<String> referencedCalendars) {
			$builder.add("referencedCalendars", DTOUtils.toJsonStringArray(referencedCalendars));
			return this;
		}
	}

	public static class SingleEventViewDTOImpl extends EventViewDTOImpl implements SingleEventViewDTO {
		SingleEventViewDTOImpl(JsonObject data) {
			super(data);
		}

		public static class BuilderImpl extends EventViewDTOImpl.BuilderImpl<SingleEventViewDTO>
				implements SingleEventViewDTO.Builder {
			public BuilderImpl() {
				$builder.add("@type", "single");
			}

			@Override
			public SingleEventViewDTO.Builder key(String key) {
				return (SingleEventViewDTO.Builder) super.key(key);
			}

			@Override
			public SingleEventViewDTO.Builder calendarKey(String calendarKey) {
				return (SingleEventViewDTO.Builder) super.calendarKey(calendarKey);
			}

			@Override
			public SingleEventViewDTO.Builder title(String title) {
				return (SingleEventViewDTO.Builder) super.title(title);
			}

			@Override
			public SingleEventViewDTO.Builder description(String description) {
				return (SingleEventViewDTO.Builder) super.description(description);
			}

			@Override
			public SingleEventViewDTO.Builder owner(String owner) {
				return (SingleEventViewDTO.Builder) super.owner(owner);
			}

			@Override
			public SingleEventViewDTO.Builder status(Status status) {
				return (SingleEventViewDTO.Builder) super.status(status);
			}

			@Override
			public SingleEventViewDTO.Builder start(ZonedDateTime start) {
				return (SingleEventViewDTO.Builder) super.start(start);
			}

			@Override
			public SingleEventViewDTO.Builder end(ZonedDateTime end) {
				return (SingleEventViewDTO.Builder) super.end(end);
			}

			@Override
			public SingleEventViewDTO.Builder tags(List<String> tags) {
				return (SingleEventViewDTO.Builder) super.tags(tags);
			}

			@Override
			public SingleEventViewDTO.Builder referencedCalendars(List<String> referencedCalendars) {
				return (SingleEventViewDTO.Builder) super.referencedCalendars(referencedCalendars);
			}

			public SingleEventViewDTO build() {
				return new SingleEventViewDTOImpl($builder.build());
			}
		}
	}

	public static class SeriesMovedEventViewDTOImpl extends EventViewDTOImpl implements SeriesMovedEventViewDTO {
		SeriesMovedEventViewDTOImpl(JsonObject data) {
			super(data);
		}

		@Override
		public String masterEventKey() {
			return DTOUtils.mapString(data, "masterEventKey");
		}

		@Override
		public ZonedDateTime originalStart() {
			return DTOUtils.mapZonedDateTime(data, "originalStart");
		}

		@Override
		public ZonedDateTime originalEnd() {
			return DTOUtils.mapZonedDateTime(data, "originalEnd");
		}

		public static class BuilderImpl extends EventViewDTOImpl.BuilderImpl<SeriesMovedEventViewDTO>
				implements SeriesMovedEventViewDTO.Builder {
			public BuilderImpl() {
				$builder.add("@type", "series-moved");
			}

			@Override
			public SeriesMovedEventViewDTO.Builder key(String key) {
				return (SeriesMovedEventViewDTO.Builder) super.key(key);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder calendarKey(String calendarKey) {
				return (SeriesMovedEventViewDTO.Builder) super.calendarKey(calendarKey);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder title(String title) {
				return (SeriesMovedEventViewDTO.Builder) super.title(title);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder description(String description) {
				return (SeriesMovedEventViewDTO.Builder) super.description(description);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder owner(String owner) {
				return (SeriesMovedEventViewDTO.Builder) super.owner(owner);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder status(Status status) {
				return (SeriesMovedEventViewDTO.Builder) super.status(status);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder start(ZonedDateTime start) {
				return (SeriesMovedEventViewDTO.Builder) super.start(start);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder end(ZonedDateTime end) {
				return (SeriesMovedEventViewDTO.Builder) super.end(end);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder tags(List<String> tags) {
				return (SeriesMovedEventViewDTO.Builder) super.tags(tags);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder referencedCalendars(List<String> referencedCalendars) {
				return (SeriesMovedEventViewDTO.Builder) super.referencedCalendars(referencedCalendars);
			}

			@Override
			public SeriesMovedEventViewDTO.Builder masterEventKey(String masterEventKey) {
				$builder.add("masterEventKey", masterEventKey);
				return this;
			}

			@Override
			public SeriesMovedEventViewDTO.Builder originalStart(ZonedDateTime originalStart) {
				$builder.add("originalStart", originalStart.toString());
				return this;
			}

			@Override
			public SeriesMovedEventViewDTO.Builder originalEnd(ZonedDateTime originalEnd) {
				$builder.add("originalEnd", originalEnd.toString());
				return this;
			}

			public SeriesMovedEventViewDTO build() {
				return new SeriesMovedEventViewDTOImpl($builder.build());
			}
		}
	}

	public static class SeriesEventViewDTOImpl extends EventViewDTOImpl implements SeriesEventViewDTO {
		SeriesEventViewDTOImpl(JsonObject data) {
			super(data);
		}

		@Override
		public String masterEventKey() {
			return DTOUtils.mapString(data, "masterEventKey");
		}

		public static class BuilderImpl extends EventViewDTOImpl.BuilderImpl<SeriesEventViewDTO>
				implements SeriesEventViewDTO.Builder {
			public BuilderImpl() {
				$builder.add("@type", "series");
			}

			@Override
			public SeriesEventViewDTO.Builder key(String key) {
				return (SeriesEventViewDTO.Builder) super.key(key);
			}

			@Override
			public SeriesEventViewDTO.Builder calendarKey(String calendarKey) {
				return (SeriesEventViewDTO.Builder) super.calendarKey(calendarKey);
			}

			@Override
			public SeriesEventViewDTO.Builder title(String title) {
				return (SeriesEventViewDTO.Builder) super.title(title);
			}

			@Override
			public SeriesEventViewDTO.Builder description(String description) {
				return (SeriesEventViewDTO.Builder) super.description(description);
			}

			@Override
			public SeriesEventViewDTO.Builder owner(String owner) {
				return (SeriesEventViewDTO.Builder) super.owner(owner);
			}

			@Override
			public SeriesEventViewDTO.Builder status(Status status) {
				return (SeriesEventViewDTO.Builder) super.status(status);
			}

			@Override
			public SeriesEventViewDTO.Builder start(ZonedDateTime start) {
				return (SeriesEventViewDTO.Builder) super.start(start);
			}

			@Override
			public SeriesEventViewDTO.Builder end(ZonedDateTime end) {
				return (SeriesEventViewDTO.Builder) super.end(end);
			}

			@Override
			public SeriesEventViewDTO.Builder tags(List<String> tags) {
				return (SeriesEventViewDTO.Builder) super.tags(tags);
			}

			@Override
			public SeriesEventViewDTO.Builder referencedCalendars(List<String> referencedCalendars) {
				return (SeriesEventViewDTO.Builder) super.referencedCalendars(referencedCalendars);
			}

			@Override
			public SeriesEventViewDTO.Builder masterEventKey(String masterEventKey) {
				$builder.add("masterEventKey", masterEventKey);
				return this;
			}

			public SeriesEventViewDTO build() {
				return new SeriesEventViewDTOImpl($builder.build());
			}
		}
	}
}
