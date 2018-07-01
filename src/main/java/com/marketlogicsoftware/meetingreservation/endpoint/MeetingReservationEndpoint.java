package com.marketlogicsoftware.meetingreservation.endpoint;

import com.marketlogicsoftware.meetingreservation.reservation.ReservationRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/*
 * Meeting reservation APIs
 * @author Amr Saleh
 */
@RestController
public class MeetingReservationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingReservationEndpoint.class);

    ReservationRequestProcessor reservationRequestProcessor;

    @Autowired
    public MeetingReservationEndpoint(ReservationRequestProcessor reservationRequestProcessor) {
        this.reservationRequestProcessor = reservationRequestProcessor;
    }

    @PostMapping(value = "timetable-creation",
                consumes = MediaType.TEXT_PLAIN_VALUE,
                produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> batchReservations(@RequestBody Optional<String> requestBody) {
        LOGGER.info("Received a request for reservations batch processing");

        return requestBody
                .map(body -> ResponseEntity.ok(reservationRequestProcessor.processBatchReservations(body)))
                .orElse(ResponseEntity.badRequest().body("Missing request body"));
    }
}
