package at.bestsolution.quti.client.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

public interface EventRepeatDTO extends BaseDTO {
    public short interval();
    public LocalDate endDate();
    public ZoneId timeZone();
    
    public interface EventRepeatDailyDTO extends EventRepeatDTO {
        public interface Builder extends EventRepeatDTO.Builder<EventRepeatDailyDTO> {
        }
    }

    public interface EventRepeatWeeklyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek();

        public interface Builder extends EventRepeatDTO.Builder<EventRepeatWeeklyDTO> {
            public Builder interval(short interval);
            public Builder endDate(LocalDate endDate);
            public Builder timeZone(ZoneId timeZone);

            public Builder daysOfWeek(List<DayOfWeek> daysOfWeek);
        }
    }

    public interface EventRepeatAbsoluteMonthlyDTO extends EventRepeatDTO {
        public short dayOfMonth();
        public interface Builder extends EventRepeatDTO.Builder<EventRepeatAbsoluteMonthlyDTO> {
            public Builder interval(short interval);
            public Builder endDate(LocalDate endDate);
            public Builder timeZone(ZoneId timeZone);

            public Builder dayOfMonth(short dayOfMonth);
        }
    }

    public interface EventRepeatAbsoluteYearlyDTO extends EventRepeatDTO {
        public short dayOfMonth();
        public Month month();

        public interface Builder extends EventRepeatDTO.Builder<EventRepeatAbsoluteYearlyDTO> {
            public Builder interval(short interval);
            public Builder endDate(LocalDate endDate);
            public Builder timeZone(ZoneId timeZone);

            public Builder dayOfMonth(short dayOfMonth);
            public Builder month(Month month);
        }
    }

    public interface EventRepeatRelativeMonthlyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek();

        public interface Builder extends EventRepeatDTO.Builder<EventRepeatRelativeMonthlyDTO> {
            public Builder interval(short interval);
            public Builder endDate(LocalDate endDate);
            public Builder timeZone(ZoneId timeZone);

            public Builder daysOfWeek(List<DayOfWeek> daysOfWeek);
        }
    }

    public interface EventRepeatRelativeYearlyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek();
        public Month month();

        public interface Builder extends EventRepeatDTO.Builder<EventRepeatRelativeYearlyDTO> {
            public Builder interval(short interval);
            public Builder endDate(LocalDate endDate);
            public Builder timeZone(ZoneId timeZone);

            public Builder daysOfWeek(List<DayOfWeek> daysOfWeek);
            public Builder month(Month month);    
        }
    }

    public interface Builder<T extends EventRepeatDTO> extends BaseDTO.Builder<T> {
        public Builder<T> interval(short interval);
        public Builder<T> endDate(LocalDate endDate);
        public Builder<T> timeZone(ZoneId timeZone);
    }
}
