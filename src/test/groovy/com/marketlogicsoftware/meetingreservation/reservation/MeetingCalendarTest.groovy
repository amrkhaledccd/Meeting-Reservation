package com.marketlogicsoftware.meetingreservation.reservation

import com.marketlogicsoftware.meetingreservation.model.OfficeHours
import com.marketlogicsoftware.meetingreservation.model.Reservation
import spock.lang.Specification
import java.time.LocalTime


class MeetingCalendarTest extends Specification {

    MeetingCalendar meetingCalendar

    def setup(){
        meetingCalendar = new MeetingCalendar()
    }

    def "Should make a reservations"() {

        given:
            def officeHours = new OfficeHours(LocalTime.of(9, 0), LocalTime.of(17, 30))
            meetingCalendar.setOfficeHours(officeHours)

            def reservations = Arrays.asList(
                    Reservation.Builder.build("2018-05-17 10:17:06 EMP001", "2018-05-21 09:00 2"),
                    Reservation.Builder.build("2018-05-16 12:34:56 EMP002", "2018-05-21 09:00 2")
            )

            def expectedReservationText = "2018-05-21\n" +
                                          "09:00 11:00 EMP002"

        when:
            reservations.each({reservation -> meetingCalendar.makeReservation(reservation)})
            def result = meetingCalendar.allReservationsAsString()

        then:
            result != null
            result.trim() == expectedReservationText
    }
}
