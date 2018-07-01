package com.marketlogicsoftware.meetingreservation.reservation;

import com.marketlogicsoftware.meetingreservation.model.OfficeHours;
import com.marketlogicsoftware.meetingreservation.model.Reservation;
import com.marketlogicsoftware.meetingreservation.util.DateTimeUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;


/*
 * Creating, Validating and retrieving reservations
 * @author Amr Saleh
 */
@Component
@Data
public class MeetingCalendar {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingCalendar.class);

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    Map<String, ArrayList<Reservation>> reservationsMap = new HashMap<>();

    private OfficeHours officeHours;
    private DateTimeUtil dateTimeUtil = DateTimeUtil.getInstance();

    /*
     * Check overlaps and create reservation
     * @Param reservation object
     */
    public void makeReservation(Reservation reservation) {
        LOGGER.info(String.format("Received a reservation for empId: %s - request date: %s",
                reservation.getEmployeeId(), reservation.getRequestDate().toString()));

        if(validateReservation(reservation)){
            String key = reservation.getReservationFrom().toLocalDate().toString();

            // If there is a reservations already exists add the reservation to list
            // otherwise create new reservation list
            reservationsMap.compute(key, (day, dayReservations) ->

                 Optional.ofNullable(dayReservations)
                        .map(reservationList -> {

                            // Find if there is an overlap with an existing reservation
                            Optional<Reservation> overlapedReservation = Optional.empty();
                            for(int idx = 0; idx < reservationList.size(); idx++){
                                if(isReservationsOverlap(reservation, dayReservations.get(idx))){
                                    overlapedReservation = Optional.of(reservationList.get(idx));
                                    break;
                                }
                            }

                            // if there is an overlap keep the one requested earlier
                            return overlapedReservation
                                    .map(existingReservation -> {
                                        if(reservation.getRequestDate().compareTo(
                                                existingReservation.getRequestDate()) == -1 ){

                                            reservationList.remove(existingReservation);
                                            reservationList.add(reservation);
                                        }
                                        return reservationList;
                                    })
                                    .orElseGet(() -> {
                                        reservationList.add(reservation);
                                        return reservationList;
                                    });
                        })
                        .orElse(new ArrayList<>(Arrays.asList(reservation)))
            );
        }
    }

    /*
     * I thought about two cases
     *      1. Meeting should be during office hours (mentioned in the assignment doc)
     *      2. Make sure that meeting starts and ends on the same day (Not sure if this possible case)
     *      @Param reservation object
     *      TODO I omitted logging to make it easier for you to read, I could've added a warn log statement
     *      TODO in each case of the below
     */
    private boolean validateReservation(Reservation reservation){
        LOGGER.info(String.format("Validating reservation for empId: %s - request date: %s",
                reservation.getEmployeeId(), reservation.getRequestDate().toString()));

        boolean isSameDay = dateTimeUtil.compareDays(reservation.getReservationFrom(), reservation.getReservationTo());

        boolean isStartWithinOfficeHours = dateTimeUtil.compareTimeRangeInclusive(reservation.getReservationFrom().toLocalTime(),
                officeHours.getFrom(), officeHours.getTo());

        boolean isEndWithinOfficeHours = dateTimeUtil.compareTimeRangeInclusive(reservation.getReservationTo().toLocalTime(),
                officeHours.getFrom(), officeHours.getTo());

        return isSameDay && isStartWithinOfficeHours && isEndWithinOfficeHours;
    }

    /*
     * Check overlap between two reservations
     * @Param thisReservation
     * @Param thatReservation
     */
    private boolean isReservationsOverlap(Reservation thisReservation, Reservation thatReservation){
        return dateTimeUtil.checkOverlap(thisReservation.getReservationFrom().toLocalTime(),
                thisReservation.getReservationTo().toLocalTime(),
                thatReservation.getReservationFrom().toLocalTime(),
                thatReservation.getReservationTo().toLocalTime());
    }

    /*
    * return all reservations as String
    * @Return String
    */
    public String allReservationsAsString(){
        StringBuilder reservationString = new StringBuilder();

        reservationsMap.keySet()
                .stream()
                .sorted()
                .forEach(key -> {
                    reservationString
                            .append(key)
                            .append(System.getProperty("line.separator"));

                    reservationsMap.get(key)
                            .stream()
                            .forEach(reservation ->
                                reservationString
                                        .append(reservation.getReservationFrom().toLocalTime().toString())
                                        .append(" ")
                                        .append(reservation.getReservationTo().toLocalTime().toString())
                                        .append(" ")
                                        .append(reservation.getEmployeeId())
                                        .append(System.getProperty("line.separator"))

                            );
                });

        return reservationString.toString();
    }
}
