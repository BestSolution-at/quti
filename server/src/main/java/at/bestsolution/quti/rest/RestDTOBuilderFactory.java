// Generated by RSD - Do not modify
package at.bestsolution.quti.rest;

import jakarta.inject.Singleton;

import at.bestsolution.quti.rest.dto.CalendarDTOImpl;
import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.rest.dto.EventDTOImpl;
import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatDailyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl;
import at.bestsolution.quti.rest.dto.EventViewDTOImpl.SeriesEventViewDTOImpl;
import at.bestsolution.quti.rest.dto.EventViewDTOImpl.SeriesMovedEventViewDTOImpl;
import at.bestsolution.quti.rest.dto.EventViewDTOImpl.SingleEventViewDTOImpl;
import at.bestsolution.quti.rest.dto.EventViewFilterDTOImpl;
import at.bestsolution.quti.service.dto.BaseDTO;
import at.bestsolution.quti.service.dto.CalendarDTO;
import at.bestsolution.quti.service.dto.CalendarNewDTO;
import at.bestsolution.quti.service.dto.EventDTO;
import at.bestsolution.quti.service.dto.EventNewDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.quti.service.dto.EventViewDTO.SeriesEventViewDTO;
import at.bestsolution.quti.service.dto.EventViewDTO.SeriesMovedEventViewDTO;
import at.bestsolution.quti.service.dto.EventViewDTO.SingleEventViewDTO;
import at.bestsolution.quti.service.dto.EventViewFilterDTO;
import at.bestsolution.quti.service.DTOBuilderFactory;

@Singleton
public class RestDTOBuilderFactory implements DTOBuilderFactory {
    @Override
    public <T extends BaseDTO.Builder> T builder(Class<T> type) {
        if( type == CalendarDTO.Builder.class) {
            return type.cast(CalendarDTOImpl.builder());
        }
        if( type == CalendarNewDTO.Builder.class) {
            return type.cast(CalendarNewDTOImpl.builder());
        }
        if( type == EventNewDTO.Builder.class) {
            return type.cast(EventNewDTOImpl.builder());
        }
        if( type == EventDTO.Builder.class) {
            return type.cast(EventDTOImpl.builder());
        }
        if( type == EventViewFilterDTO.Builder.class) {
            return type.cast(EventViewFilterDTOImpl.builder());
        }
        if( type == EventRepeatDailyDTO.Builder.class) {
            return type.cast(EventRepeatDailyDTOImpl.builder());
        }
        if( type == EventRepeatWeeklyDTO.Builder.class) {
            return type.cast(EventRepeatWeeklyDTOImpl.builder());
        }
        if( type == EventRepeatAbsoluteMonthlyDTO.Builder.class) {
            return type.cast(EventRepeatAbsoluteMonthlyDTOImpl.builder());
        }
        if( type == EventRepeatAbsoluteYearlyDTO.Builder.class) {
            return type.cast(EventRepeatAbsoluteYearlyDTOImpl.builder());
        }
        if( type == EventRepeatRelativeMonthlyDTO.Builder.class) {
            return type.cast(EventRepeatRelativeMonthlyDTOImpl.builder());
        }
        if( type == EventRepeatRelativeYearlyDTO.Builder.class) {
            return type.cast(EventRepeatRelativeYearlyDTOImpl.builder());
        }
        if( type == SingleEventViewDTO.Builder.class) {
            return type.cast(SingleEventViewDTOImpl.builder());
        }
        if( type == SeriesMovedEventViewDTO.Builder.class) {
            return type.cast(SeriesMovedEventViewDTOImpl.builder());
        }
        if( type == SeriesEventViewDTO.Builder.class) {
            return type.cast(SeriesEventViewDTOImpl.builder());
        }
        throw new IllegalArgumentException("Unsupported Builder '%s'".formatted(type));
    }
}
