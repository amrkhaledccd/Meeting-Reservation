package com.marketlogicsoftware.meetingreservation.reservation

import com.marketlogicsoftware.meetingreservation.model.OfficeHours
import spock.lang.Specification

import java.time.LocalTime

class ReservationRequestProcessorTest extends Specification {

    ReservationRequestProcessor processor
    MeetingCalendar meetingCalendar

    def setup() {
        meetingCalendar = Mock(MeetingCalendar)
        meetingCalendar.allReservationsAsString() >> "Mocked reservations text"
        processor = new ReservationRequestProcessor(meetingCalendar)

    }

    def "Reservation processor should set office hours, call makeReservation and return reservations as text" (){
        given:
            def batch = "0900 1730\n" +
                        "2018-05-17 10:17:06 EMP001\n" +
                        "2018-05-21 09:00 2\n" +
                        "2018-05-16 12:34:56 EMP002\n" +
                        "2018-05-21 09:00 2"

            def officeHours = new OfficeHours(LocalTime.of(9, 0), LocalTime.of(17, 30))

        when:
        def result = processor.processBatchReservations(batch)

        then:
            result == "Mocked reservations text"
            1 * meetingCalendar.setOfficeHours(officeHours)
            2 * meetingCalendar.makeReservation(_)
    }
}
