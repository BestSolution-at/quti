// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.EnumSet;

import at.bestsolution.quti.service.dto.EventRepeatRelativeYearlyDTO;

public class EventRepeatRelativeYearlyPatchDTOImpl extends EventRepeatPatchDTOImpl implements EventRepeatRelativeYearlyDTO.Patch {
    private Month month;
    private short interval;
    private LocalDate endDate;
    private ZoneId timeZone;

    private final EnumSet<Props> dataSet = EnumSet.noneOf(Props.class);

    public EventRepeatRelativeYearlyPatchDTOImpl() {}

    @Override
    public boolean isSet(EventRepeatRelativeYearlyDTO.Patch.Props prop) {
        return dataSet.contains(prop);
    }

    public void setMonth(Month month) {
        this.month = month;
        this.dataSet.add(Props.MONTH);
    }

    @Override
    public Month month() {
        return this.month;
    }

    public void setInterval(short interval) {
        this.interval = interval;
        this.dataSet.add(Props.INTERVAL);
    }

    @Override
    public short interval() {
        return this.interval;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.dataSet.add(Props.ENDDATE);
    }

    @Override
    public LocalDate endDate() {
        return this.endDate;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
        this.dataSet.add(Props.TIMEZONE);
    }

    @Override
    public ZoneId timeZone() {
        return this.timeZone;
    }

}
