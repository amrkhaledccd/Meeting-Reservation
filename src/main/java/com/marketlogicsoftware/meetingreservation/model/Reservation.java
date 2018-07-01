package com.marketlogicsoftware.meetingreservation.model;

import com.marketlogicsoftware.meetingreservation.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Reservation {
    private String employeeId;
    private LocalDateTime requestDate;
    private LocalDateTime reservationFrom;
    private LocalDateTime reservationTo;
    private Duration meetingDuration;

    public static class Builder {

        public static Reservation build(String reservationRequest, String reservationDate) {
            DateTimeUtil dateTimeUtil = DateTimeUtil.getInstance();

            // 0 = request day, 1 = request time, 2 = emp id
            String[] reservationRequestArray = reservationRequest.split(" ");

            // 0 = reservation day, 1 = reservation time, 2 = meeting hours
            String[] reservationDateArray = reservationDate.split(" ");

            LocalDateTime requestDate = dateTimeUtil.buildLocalDateTime(reservationRequestArray[0],
                    reservationRequestArray[1]);

            String empId = reservationRequestArray[2];

            LocalDateTime reservationFrom = dateTimeUtil.buildLocalDateTime(reservationDateArray[0],
                    reservationDateArray[1]);

            Duration duration = Duration.ofHours(Long.parseLong(reservationDateArray[2]));

            LocalDateTime reservationTo = reservationFrom.plusHours(duration.toHours());

            return new Reservation(empId, requestDate, reservationFrom, reservationTo, duration);
        }
    }
}
