// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.util.EnumSet;

import at.bestsolution.quti.service.dto.CalendarDTO;

public class CalendarPatchDTOImpl implements CalendarDTO.Patch {
    private String key;
    private String name;
    private String owner;

    private final EnumSet<Props> dataSet = EnumSet.noneOf(Props.class);

    public CalendarPatchDTOImpl() {}

    @Override
    public boolean isSet(Props prop) {
        return dataSet.contains(prop);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return this.key;
    }
    public void setName(String name) {
        this.name = name;
        this.dataSet.add(Props.NAME);
    }

    @Override
    public String name() {
        return this.name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        this.dataSet.add(Props.OWNER);
    }

    @Override
    public String owner() {
        return this.owner;
    }

}
