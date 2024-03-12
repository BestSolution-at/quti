package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.client.dto.EventRepeatDTO;
import at.bestsolution.quti.client.jdkhttp.impl.Utils;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class EventRepeatDTOImpl extends BaseDTOImpl implements EventRepeatDTO {

    public EventRepeatDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public short interval() {
        return (short) this.data.getInt("interval");
    }

    @Override
    public LocalDate endDate() {
        return data.containsKey("endDate") ? LocalDate.parse(data.getString("endDate")) : null;
    }

    @Override
    public ZoneId timeZone() {
        return ZoneId.of(data.getString("timeZone"));
    }

    public static EventRepeatDTO of(JsonObject data) {
		return switch (data.getString("@type")) {
		case "daily" -> new EventRepeatDailyDTOImpl(data);
		case "weekly" -> new EventRepeatWeeklyDTOImpl(data);
		case "absolute-monthly" -> new EventRepeatAbsoluteMonthlyDTOImpl(data);
		case "absolute-yearly" -> new EventRepeatAbsoluteYearlyDTOImpl(data);
		case "relative-monthly" -> new EventRepeatRelativeMonthlyDTOImpl(data);
		case "relative-yearly" -> new EventRepeatRelativeYearlyDTOImpl(data);
		default -> throw new IllegalArgumentException("Unexpected value: " + data.getString("@type"));
		};
	}

    public static abstract class BuilderImpl<T extends EventRepeatDTO> implements Builder<T> {
		JsonObjectBuilder builder = Json.createObjectBuilder();

		public Builder<T> interval(short interval) {
			builder.add("interval", interval);
			return this;
		}

		public Builder<T> endDate(LocalDate endDate) {
			if( endDate != null ) {
				builder.add("endDate", endDate.toString() );	
			}
			return this;
		}

		public Builder<T> timeZone(ZoneId timeZone) {
			builder.add("timeZone", timeZone.toString() );
			return this;
		}
	}

    public static class EventRepeatDailyDTOImpl extends EventRepeatDTOImpl implements EventRepeatDailyDTO {

		EventRepeatDailyDTOImpl(JsonObject data) {
			super(data);
		}

		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatDailyDTO> implements EventRepeatDailyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "daily");
			}
			@Override
			public EventRepeatDailyDTO build() {
				return new EventRepeatDailyDTOImpl(builder.build());
			}

		}
	}

	public static class EventRepeatWeeklyDTOImpl extends EventRepeatDTOImpl implements EventRepeatWeeklyDTO {
		EventRepeatWeeklyDTOImpl(JsonObject data) {
			super(data);
		}

		public List<DayOfWeek> daysOfWeek() {
			return Utils.mapStrings(data, "daysOfWeek", DayOfWeek::valueOf);
		}

		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatWeeklyDTO> implements EventRepeatWeeklyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "weekly");
			}

			@Override
			public EventRepeatWeeklyDTO.Builder endDate(LocalDate endDate) {
				return (EventRepeatWeeklyDTO.Builder) super.endDate(endDate);
			}

			@Override
			public EventRepeatWeeklyDTO.Builder interval(short interval) {
				return (EventRepeatWeeklyDTO.Builder) super.interval(interval);
			}

			@Override
			public EventRepeatWeeklyDTO.Builder timeZone(ZoneId timeZone) {
				return (EventRepeatWeeklyDTO.Builder) super.timeZone(timeZone);
			}

			public EventRepeatWeeklyDTO.Builder daysOfWeek(List<DayOfWeek> daysOfWeek) {
				builder.add("daysOfWeek",daysOfWeek.stream().map(DayOfWeek::toString).collect(Utils.toStringArray()));
				return this;
			}

			@Override
			public EventRepeatWeeklyDTO build() {
				return new EventRepeatWeeklyDTOImpl(builder.build());
			}
		}
		
	}

	public static class EventRepeatAbsoluteMonthlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatAbsoluteMonthlyDTO {
		EventRepeatAbsoluteMonthlyDTOImpl(JsonObject data) {
			super(data);
		}

		public short dayOfMonth() {
			return (short) data.getInt("dayOfMonth");
		}

		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatAbsoluteMonthlyDTO> implements EventRepeatAbsoluteMonthlyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "absolute-monthly");
			}

			@Override
			public EventRepeatAbsoluteMonthlyDTO.Builder endDate(LocalDate endDate) {
				return (EventRepeatAbsoluteMonthlyDTO.Builder) super.endDate(endDate);
			}

			@Override
			public EventRepeatAbsoluteMonthlyDTO.Builder interval(short interval) {
				return (EventRepeatAbsoluteMonthlyDTO.Builder) super.interval(interval);
			}

			@Override
			public EventRepeatAbsoluteMonthlyDTO.Builder timeZone(ZoneId timeZone) {
				return (EventRepeatAbsoluteMonthlyDTO.Builder) super.timeZone(timeZone);
			}
			
			public EventRepeatAbsoluteMonthlyDTO.Builder dayOfMonth(short dayOfMonth) {
				this.builder.add("dayOfMonth", dayOfMonth);
				return this;
			}

			@Override
			public EventRepeatAbsoluteMonthlyDTO build() {
				return new EventRepeatAbsoluteMonthlyDTOImpl(builder.build());
			}
		}
	}

	public static class EventRepeatAbsoluteYearlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatAbsoluteYearlyDTO {
		EventRepeatAbsoluteYearlyDTOImpl(JsonObject data) {
			super(data);
		}

		public short dayOfMonth() {
			return (short) data.getInt("dayOfMonth");
		}

		public Month month() {
			return Month.valueOf(data.getString("month"));
		}

		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatAbsoluteYearlyDTO> implements EventRepeatAbsoluteYearlyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "absolute-yearly");
			}

			@Override
			public EventRepeatAbsoluteYearlyDTO.Builder endDate(LocalDate endDate) {
				return (EventRepeatAbsoluteYearlyDTO.Builder) super.endDate(endDate);
			}

			@Override
			public EventRepeatAbsoluteYearlyDTO.Builder interval(short interval) {
				return (EventRepeatAbsoluteYearlyDTO.Builder) super.interval(interval);
			}

			@Override
			public EventRepeatAbsoluteYearlyDTO.Builder timeZone(ZoneId timeZone) {
				return (EventRepeatAbsoluteYearlyDTO.Builder) super.timeZone(timeZone);
			}

			public EventRepeatAbsoluteYearlyDTO.Builder dayOfMonth(short dayOfMonth) {
				builder.add("dayOfMonth", dayOfMonth);
				return this;
			}
			public EventRepeatAbsoluteYearlyDTO.Builder month(Month month) {
				builder.add("month", month.toString());
				return this;
			}
			@Override
			public EventRepeatAbsoluteYearlyDTO build() {
				return new EventRepeatAbsoluteYearlyDTOImpl(builder.build());
			}
		}
	}

	public static class EventRepeatRelativeMonthlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatRelativeMonthlyDTO {
		EventRepeatRelativeMonthlyDTOImpl(JsonObject data) {
			super(data);
		}

		public List<DayOfWeek> daysOfWeek() {
			return Utils.mapStrings(data, "daysOfWeek", DayOfWeek::valueOf);
		}
		
		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatRelativeMonthlyDTO> implements EventRepeatRelativeMonthlyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "relative-monthly");
			}

			@Override
			public EventRepeatRelativeMonthlyDTO.Builder endDate(LocalDate endDate) {
				return (EventRepeatRelativeMonthlyDTO.Builder) super.endDate(endDate);
			}

			@Override
			public EventRepeatRelativeMonthlyDTO.Builder interval(short interval) {
				return (EventRepeatRelativeMonthlyDTO.Builder) super.interval(interval);
			}

			@Override
			public EventRepeatRelativeMonthlyDTO.Builder timeZone(ZoneId timeZone) {
				return (EventRepeatRelativeMonthlyDTO.Builder) super.timeZone(timeZone);
			}

			@Override
			public EventRepeatRelativeMonthlyDTO.Builder daysOfWeek(
					List<DayOfWeek> daysOfWeek) {
				builder.add("daysOfWeek",daysOfWeek.stream().map(DayOfWeek::toString).collect(Utils.toStringArray()));
				return this;
			}
			
			@Override
			public EventRepeatRelativeMonthlyDTO build() {
				return new EventRepeatRelativeMonthlyDTOImpl(builder.build());
			}
		}
	}

	public static class EventRepeatRelativeYearlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatRelativeYearlyDTO {
		EventRepeatRelativeYearlyDTOImpl(JsonObject data) {
			super(data);
		}

		public List<DayOfWeek> daysOfWeek() {
			return Utils.mapStrings(data, "daysOfWeek", DayOfWeek::valueOf);
		}

		public Month month() {
			return Month.valueOf(data.getString("month"));
		}
		
		public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl<EventRepeatRelativeYearlyDTO> implements EventRepeatRelativeYearlyDTO.Builder {
			public BuilderImpl() {
				builder.add("@type", "relative-yearly");
			}

			@Override
			public EventRepeatRelativeYearlyDTO.Builder endDate(LocalDate endDate) {
				return (EventRepeatRelativeYearlyDTO.Builder) super.endDate(endDate);
			}

			@Override
			public EventRepeatRelativeYearlyDTO.Builder interval(short interval) {
				return (EventRepeatRelativeYearlyDTO.Builder) super.interval(interval);
			}

			@Override
			public EventRepeatRelativeYearlyDTO.Builder timeZone(ZoneId timeZone) {
				return (EventRepeatRelativeYearlyDTO.Builder) super.timeZone(timeZone);
			}

			@Override
			public EventRepeatRelativeYearlyDTO.Builder daysOfWeek(
					List<DayOfWeek> daysOfWeek) {
				builder.add("daysOfWeek",daysOfWeek.stream().map(DayOfWeek::toString).collect(Utils.toStringArray()));
				return this;
			}

			@Override
			public EventRepeatRelativeYearlyDTO.Builder month(
					Month month) {
				builder.add("month", month.toString());
				return this;
			}
			
			@Override
			public EventRepeatRelativeYearlyDTO build() {
				return new EventRepeatRelativeYearlyDTOImpl(builder.build());
			}
		}

	}
}
