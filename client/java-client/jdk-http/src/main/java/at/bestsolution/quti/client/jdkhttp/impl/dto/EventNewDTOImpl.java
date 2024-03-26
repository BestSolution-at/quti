package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.quti.client.jdkhttp.impl.Utils;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatDailyDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class EventNewDTOImpl extends BaseDTOImpl implements EventNewDTO {

    public EventNewDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public String title() {
        return data.getString("title");
    }

    @Override
    public String description() {
        return data.getString("description");
    }

    @Override
    public ZonedDateTime start() {
        return ZonedDateTime.parse(data.getString("start"));
    }

    @Override
    public ZonedDateTime end() {
        return ZonedDateTime.parse(data.getString("end"));
    }

    @Override
    public boolean fullday() {
        return data.getBoolean("fullday");
    }

    @Override
    public EventRepeatDTO repeat() {
        return data.containsKey("repeat") ? EventRepeatDTOImpl.of(data.getJsonObject("repeat")) : null;
    }

    @Override
    public List<String> tags() {
        return Utils.mapStrings(data, "tags");
    }

    @Override
    public List<String> referencedCalendars() {
        return Utils.mapStrings(data, "referencedCalendars");
    }

    public static class BuilderImpl implements Builder {
		private JsonObjectBuilder builder = Json.createObjectBuilder();
		
		public Builder title(String title) {
			builder.add("title", title);
			return this;
		}
		
		public Builder description(String description) {
			builder.add("description", description);
			return this;
		}
		
		public Builder start(ZonedDateTime start) {
			builder.add("start", start.toString());
			return this;
		}
		
		public Builder end(ZonedDateTime end) {
			builder.add("end", end.toString());
			return this;
		}
		
		public Builder fullday(boolean fullday) {
			builder.add("fullday", fullday);
			return this;
		}

		@Override
		public Builder repeat(EventRepeatDTO repeat) {
			builder.add("repeat", ((EventRepeatDTOImpl)repeat).data);
			return this;
		}
		
		public <T extends EventRepeatDTO.Builder> Builder 
			withRepeat(Class<T> clazz, Function<T, EventRepeatDTO> block) {
			EventRepeatDTOImpl.Builder b = null;
			if( clazz == EventRepeatDailyDTO.Builder.class ) {
				b = new EventRepeatDailyDTOImpl.BuilderImpl();
			} else if( clazz == EventRepeatWeeklyDTO.Builder.class ) {
				b = new EventRepeatWeeklyDTOImpl.BuilderImpl();
			} else if( clazz == EventRepeatAbsoluteMonthlyDTO.Builder.class) {
				b= new EventRepeatAbsoluteMonthlyDTOImpl.BuilderImpl();
			} else if( clazz == EventRepeatAbsoluteYearlyDTO.Builder.class ) {
				b = new EventRepeatAbsoluteYearlyDTOImpl.BuilderImpl();
			} else if( clazz == EventRepeatRelativeMonthlyDTO.Builder.class ) {
				b = new EventRepeatRelativeMonthlyDTOImpl.BuilderImpl();
			} else if( clazz == EventRepeatRelativeYearlyDTO.Builder.class ) {
				b = new EventRepeatRelativeYearlyDTOImpl.BuilderImpl();
			}
			
			builder.add("repeat", ((EventRepeatDTOImpl)block.apply((T) b)).data);
			
			return this;
		}
		
		public Builder tags(List<String> tags) {
			builder.add("tags",tags.stream().collect(Utils.toStringArray()));
			return this;
		}
		
		public Builder referencedCalendars(List<String> referencedCalendars) {
			builder.add("referencedCalendars",referencedCalendars.stream().collect(Utils.toStringArray()));
			return this;
		}
		
		public EventNewDTO build() {
			return new EventNewDTOImpl(builder.build());
		}
	}
}
