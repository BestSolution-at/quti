package at.bestsolution.quti.rest.dto;

import at.bestsolution.quti.service.dto.CalendarDTO;

public record CalendarPatchDTOImpl(String key, String name, String owner) implements CalendarDTO.Patch {

}
