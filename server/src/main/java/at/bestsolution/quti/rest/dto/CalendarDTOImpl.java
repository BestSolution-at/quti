// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

public record CalendarDTOImpl(
    String key,
    String name,
    String owner) implements at.bestsolution.quti.service.dto.CalendarDTO {

    public static CalendarDTOImpl of(at.bestsolution.quti.service.dto.CalendarDTO source) {
        if(source instanceof CalendarDTOImpl) {
            return (CalendarDTOImpl)source;
        }
        return new CalendarDTOImpl(
            source.key(),
            source.name(),
            source.owner()
        );
    }}
