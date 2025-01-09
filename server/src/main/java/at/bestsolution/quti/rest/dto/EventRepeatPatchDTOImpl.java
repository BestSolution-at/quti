package at.bestsolution.quti.rest.dto;

import at.bestsolution.quti.service.dto.EventRepeatDTO;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
		@JsonbSubtype(alias = "daily-patch", type = EventRepeatDailyPatchDTOImpl.class),
		@JsonbSubtype(alias = "weekly-patch", type = EventRepeatWeeklyPatchDTOImpl.class),
		@JsonbSubtype(alias = "absolute-monthly-patch", type = EventRepeatAbsoluteMonthlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "absolute-yearly-patch", type = EventRepeatAbsoluteYearlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "relative-monthly-patch", type = EventRepeatRelativeMonthlyPatchDTOImpl.class),
		@JsonbSubtype(alias = "relative-yearly-patch", type = EventRepeatRelativeYearlyPatchDTOImpl.class),
})
public class EventRepeatPatchDTOImpl extends EventRepeatDTOBaseImpl implements EventRepeatDTO.Patch {

}
