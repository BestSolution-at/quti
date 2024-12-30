// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.EnumSet;

import at.bestsolution.quti.service.dto.EventRepeatAbsoluteMonthlyDTO;

public class EventRepeatAbsoluteMonthlyPatchDTOImpl extends EventRepeatPatchDTOImpl implements EventRepeatAbsoluteMonthlyDTO.Patch {
    private short dayOfMonth;
    private short interval;
    private LocalDate endDate;
    private ZoneId timeZone;

    private final EnumSet<Props> dataSet = EnumSet.noneOf(Props.class);

    public EventRepeatAbsoluteMonthlyPatchDTOImpl() {}

    @Override
    public boolean isSet(EventRepeatAbsoluteMonthlyDTO.Patch.Props prop) {
        return dataSet.contains(prop);
    }

    public void setDayOfMonth(short dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.dataSet.add(Props.DAYOFMONTH);
    }

    @Override
    public short dayOfMonth() {
        return this.dayOfMonth;
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
