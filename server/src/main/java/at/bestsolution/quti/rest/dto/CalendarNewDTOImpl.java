// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import at.bestsolution.quti.service.dto.CalendarNewDTO;

public class CalendarNewDTOImpl implements CalendarNewDTO {
    public String name;
    public String owner;

    public String name() {
        return this.name;
    }

    public String owner() {
        return this.owner;
    }

    public static CalendarNewDTOImpl of(CalendarNewDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof CalendarNewDTOImpl) {
            return (CalendarNewDTOImpl)source;
        }
        var rv = new CalendarNewDTOImpl();
        rv.name = source.name();
        rv.owner = source.owner();
        return rv;
    }
    public static class BuilderImpl implements Builder {
        public String name;
        public String owner;

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public at.bestsolution.quti.service.dto.CalendarNewDTO build() {
            var rv = new CalendarNewDTOImpl();
            rv.name = name;
            rv.owner = owner;
            return rv;
        }
    }

    public static Builder builder() {
        return new BuilderImpl();
    }
}