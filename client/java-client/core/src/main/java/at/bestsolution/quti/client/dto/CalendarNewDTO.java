package at.bestsolution.quti.client.dto;

public interface CalendarNewDTO extends BaseDTO {
    String name();
    String owner();

    public interface Builder extends BaseDTO.Builder<CalendarNewDTO> {
        Builder name(String name);
        Builder owner(String owner);
    }
}
