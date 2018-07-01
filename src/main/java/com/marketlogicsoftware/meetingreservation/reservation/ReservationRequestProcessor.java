package com.marketlogicsoftware.meetingreservation.reservation;

import com.marketlogicsoftware.meetingreservation.model.OfficeHours;
import com.marketlogicsoftware.meetingreservation.model.Reservation;
import com.marketlogicsoftware.meetingreservation.util.DateTimeUtil;
import com.marketlogicsoftware.meetingreservation.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

/*
 * Processes reservation request batch
 * @author Amr Saleh
 */
@Component
public class ReservationRequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRequestProcessor.class);

    MeetingCalendar meetingCalendar;
    DateTimeUtil dateTimeUtil;
    ListUtil listUtil;

    private final String delimiter = "\\r?\\n";

    @Autowired
    public ReservationRequestProcessor(MeetingCalendar meetingCalendar) {
        this.meetingCalendar = meetingCalendar;
        this.dateTimeUtil = DateTimeUtil.getInstance();
        this.listUtil = ListUtil.getInstance();
    }

    /*
     * Converts the batch string to reservations and send it to calendar
     * Note: introduced coupling between Processor and Calendar to reduce number of loops
     *       I could've made it return a list<Reservation> and a ReservationManager send the reservations
     *       to Calendar.
     * @Param batch reservation batch string
     * @return Reservations as String
     */
    public String processBatchReservations(String batch){
        LOGGER.info("Received a request for reservations batch processing");

        List<String> batchList = Arrays.asList(batch.split(delimiter));

        //First line represents the office hours
        meetingCalendar.setOfficeHours(parseOfficeHours(batchList.get(0)));

        // Divides the list into stream of paris of lines - two lines represent reservation
        // Maps the String to reservation object and send it to Calendar to book reservation
        listUtil
                .sliding(batchList.subList(1, batchList.size()))
                .map(reservation -> Reservation.Builder.build(reservation.get(0), reservation.get(1)))
                .forEach(meetingCalendar::makeReservation);

        return meetingCalendar.allReservationsAsString();
    }

    /*
     * Converts String to OfficeHour object
     * @Param hours String represents office hours in format HHMM
     */
    private OfficeHours parseOfficeHours(String hours) {

        String[] hoursArray = hours.split(" ");
        String from = hoursArray[0].substring(0, 2) + ":" + hoursArray[0].substring(2, 4);
        String to = hoursArray[1].substring(0, 2) + ":" + hoursArray[1].substring(2, 4);

        return new OfficeHours(dateTimeUtil.time24ToLocalTime(from), dateTimeUtil.time24ToLocalTime(to));
    }
}
