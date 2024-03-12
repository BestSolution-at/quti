package at.bestsolution.quti.client.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

public interface EventNewDTO extends BaseDTO {
    public String title();
	public String description();
	public ZonedDateTime start();
	public ZonedDateTime end();
	public boolean fullday();
	public EventRepeatDTO repeat();
	public List<String> tags();
	public List<String> referencedCalendars();

	public interface Builder extends BaseDTO.Builder<EventNewDTO> {
		public Builder title(String title);
		public Builder description(String description);
		public Builder start(ZonedDateTime start);
		public Builder end(ZonedDateTime end);
		public Builder fullday(boolean fullday);
		public Builder tags(List<String> tags);
		public Builder referencedCalendars(List<String> referencedCalendars);

		public <T extends EventRepeatDTO.Builder<?>> Builder withRepeat(Class<T> clazz, Function<T, EventRepeatDTO> block);
	}
}
