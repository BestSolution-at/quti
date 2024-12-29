// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO;
import at.bestsolution.quti.client.dto.EventRepeatDailyDTO;
import at.bestsolution.quti.client.dto.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatWeeklyDTO;

public class EventNewDTOImpl extends BaseDTOImpl implements EventNewDTO {

	EventNewDTOImpl(JsonObject data) {
		super(data);
	}

	@Override
	public String title() {
		return DTOUtils.mapString(data, "title");
	}

	@Override
	public String description() {
		return DTOUtils.mapString(data, "description", null);
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
	public boolean fullday() {
		return DTOUtils.mapBoolean(data, "fullday");
	}

	@Override
	public EventRepeatDTO repeat() {
		return DTOUtils.mapObject(data, "repeat", EventRepeatDTOImpl::of, null);
	}

	@Override
	public List<String> tags() {
		return DTOUtils.mapStrings(data, "tags");
	}

	@Override
	public List<String> referencedCalendars() {
		return DTOUtils.mapStrings(data, "referencedCalendars");
	}

	public static EventNewDTO of(JsonObject data) {
		return new EventNewDTOImpl(data);
	}

	public static List<EventNewDTO> of(JsonArray data) {
		return DTOUtils.mapObjects(data, EventNewDTOImpl::of);
	}

	public static class BuilderImpl implements Builder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		@Override
		public Builder title(String title) {
			$builder.add("title", title);
			return this;
		}

		@Override
		public Builder description(String description) {
			if (description == null) {
				return this;
			}
			$builder.add("description", description);
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
		public Builder fullday(boolean fullday) {
			$builder.add("fullday", fullday);
			return this;
		}

		@Override
		public Builder repeat(EventRepeatDTO repeat) {
			if (repeat == null) {
				return this;
			}
			$builder.add("repeat", ((BaseDTOImpl) repeat).data);
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

		public <T extends EventRepeatDTO.Builder> Builder withRepeat(Class<T> clazz, Function<T, EventRepeatDTO> block) {
			EventRepeatDTOImpl.Builder b = null;
			if (clazz == EventRepeatDailyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatDailyDTOImpl.BuilderImpl();
			} else if (clazz == EventRepeatWeeklyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl.BuilderImpl();
			} else if (clazz == EventRepeatAbsoluteMonthlyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl.BuilderImpl();
			} else if (clazz == EventRepeatAbsoluteYearlyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl.BuilderImpl();
			} else if (clazz == EventRepeatRelativeMonthlyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl.BuilderImpl();
			} else if (clazz == EventRepeatRelativeYearlyDTO.Builder.class) {
				b = new EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl.BuilderImpl();
			} else {
				throw new IllegalArgumentException();
			}
			$builder.add("repeat", ((EventRepeatDTOImpl) block.apply(clazz.cast(b))).data);
			return this;
		}

		public EventNewDTO build() {
			return new EventNewDTOImpl($builder.build());
		}
	}
}
