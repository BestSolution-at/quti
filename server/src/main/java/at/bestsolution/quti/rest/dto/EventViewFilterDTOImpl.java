// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.util.List;

import at.bestsolution.quti.service.dto.EventViewFilterDTO;

public class EventViewFilterDTOImpl implements EventViewFilterDTO {
    public String owner;
    public String title;
    public String description;
    public boolean fullday;
    public List<String> tags;

    public String owner() {
        return this.owner;
    }

    public String title() {
        return this.title;
    }

    public String description() {
        return this.description;
    }

    public boolean fullday() {
        return this.fullday;
    }

    public List<String> tags() {
        return this.tags;
    }

    public static EventViewFilterDTOImpl of(EventViewFilterDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof EventViewFilterDTOImpl) {
            return (EventViewFilterDTOImpl)source;
        }
        var rv = new EventViewFilterDTOImpl();
        rv.owner = source.owner();
        rv.title = source.title();
        rv.description = source.description();
        rv.fullday = source.fullday();
        rv.tags = source.tags();
        return rv;
    }
    public static class BuilderImpl implements Builder {
        public String owner;
        public String title;
        public String description;
        public boolean fullday;
        public List<String> tags;

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder fullday(boolean fullday) {
            this.fullday = fullday;
            return this;
        }
        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public at.bestsolution.quti.service.dto.EventViewFilterDTO build() {
            var rv = new EventViewFilterDTOImpl();
            rv.owner = owner;
            rv.title = title;
            rv.description = description;
            rv.fullday = fullday;
            rv.tags = tags;
            return rv;
        }
    }

    public static Builder builder() {
        return new BuilderImpl();
    }
}