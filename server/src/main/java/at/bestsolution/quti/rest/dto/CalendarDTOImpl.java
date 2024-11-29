// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import at.bestsolution.quti.service.dto.CalendarDTO;

public record CalendarDTOImpl(
    String key,
    String name,
    String owner) implements CalendarDTO {

    public static CalendarDTOImpl of(CalendarDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof CalendarDTOImpl) {
            return (CalendarDTOImpl)source;
        }
        return new CalendarDTOImpl(
            source.key(),
            source.name(),
            source.owner()
        );
    }
    public static class BuilderImpl implements Builder {
        String key;
        String name;
        String owner;

        public Builder key(String key) {
            this.key = key;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public at.bestsolution.quti.service.dto.CalendarDTO build() {
            return new CalendarDTOImpl(key, name, owner);
        }
    }

    public static Builder builder() {
        return new BuilderImpl();
    }
}