package at.bestsolution.quti.rest.dto;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
		@JsonbSubtype(alias = "daily", type = EventRepeatDailyDTOImpl.class),
		@JsonbSubtype(alias = "weekly", type = EventRepeatWeeklyDTOImpl.class),
		@JsonbSubtype(alias = "absolute-monthly", type = EventRepeatAbsoluteMonthlyDTOImpl.class),
		@JsonbSubtype(alias = "absolute-yearly", type = EventRepeatAbsoluteYearlyDTOImpl.class),
		@JsonbSubtype(alias = "relative-monthly", type = EventRepeatRelativeMonthlyDTOImpl.class),
		@JsonbSubtype(alias = "relative-yearly", type = EventRepeatRelativeYearlyDTOImpl.class),

		@JsonbSubtype(alias = "daily-patch", type = EventRepeatDailyPatchDTOImpl.class),
		@JsonbSubtype(alias = "weekly-patch", type = EventRepeatWeeklyPatchDTOImpl.class),
		@JsonbSubtype(alias = "absolute-monthly-patch", type = EventRepeatAbsoluteMonthlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "absolute-yearly-patch", type = EventRepeatAbsoluteYearlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "relative-monthly-patch", type = EventRepeatRelativeMonthlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "relative-yearly-patch", type = EventRepeatRelativeYearlyPatchDTOImpl.class),
})
public class EventRepeatDTOBaseImpl {

}
