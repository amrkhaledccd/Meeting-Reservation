package com.marketlogicsoftware.meetingreservation.util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
 * Utility class for all date and time operations
 * @author Amr Saleh
 */
public class DateTimeUtil {

    private static DateTimeUtil instance;

    private DateTimeUtil(){}

    public static DateTimeUtil getInstance(){
        if(instance == null) {
            instance = new DateTimeUtil();
        }
        return instance;
    }

    public LocalTime time24ToLocalTime(String time24) {
        return LocalTime.parse(time24, DateTimeFormatter.ISO_TIME);
    }

    public LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDateTime buildLocalDateTime(String date, String time) {
        return LocalDateTime.of(stringToLocalDate(date), time24ToLocalTime(time));
    }

    public boolean compareDays(LocalDateTime day1, LocalDateTime day2) {
        return day1.toLocalDate().compareTo(day2.toLocalDate()) == 0;
    }

    /*
     * Compares two time with inclusive start and end
     */
    public boolean compareTimeRangeInclusive(LocalTime time, LocalTime rangeFrom, LocalTime rangeTo) {
       return (time.compareTo(rangeFrom) == 0 || time.isAfter(rangeFrom)) &&
               (time.compareTo(rangeTo) == 0 || time.isBefore(rangeTo));
    }

    /*
     * Compares two time with inclusive start
     */
    public boolean compareTimeRangeInclusiveStart(LocalTime time, LocalTime rangeFrom, LocalTime rangeTo) {
        return (time.compareTo(rangeFrom) == 0 || time.isAfter(rangeFrom)) && time.isBefore(rangeTo);
    }

    /*
     * Compares two time with inclusive end
     */
    public boolean compareTimeRangeInclusiveEnd(LocalTime time, LocalTime rangeFrom, LocalTime rangeTo) {
        return time.isAfter(rangeFrom) && (time.compareTo(rangeTo) == 0 || time.isBefore(rangeTo));
    }

    /*
     * Checks overlap between two time periods
     */
    public boolean checkOverlap(LocalTime thisTimeFrom, LocalTime thisTimeTo,
                                LocalTime thatTimeFrom, LocalTime thatTimeTo ) {

        return compareTimeRangeInclusiveStart(thisTimeFrom, thatTimeFrom, thatTimeTo) ||
                compareTimeRangeInclusiveEnd(thisTimeTo, thatTimeFrom, thatTimeTo) ||
                compareTimeRangeInclusiveStart(thatTimeFrom, thisTimeFrom, thisTimeTo) ||
                compareTimeRangeInclusiveEnd(thatTimeTo, thisTimeFrom, thisTimeTo);
    }
}
