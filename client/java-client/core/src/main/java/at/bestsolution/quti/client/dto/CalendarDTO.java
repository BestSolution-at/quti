// Generated by RSD - Do not modify
package at.bestsolution.quti.client.dto;

public interface CalendarDTO extends BaseDTO {
    public String key();
    public String name();
    public String owner();

    public interface Builder extends BaseDTO.Builder {
        public Builder key(String key);
        public Builder name(String name);
        public Builder owner(String owner);
        public CalendarDTO build();
    }
}
