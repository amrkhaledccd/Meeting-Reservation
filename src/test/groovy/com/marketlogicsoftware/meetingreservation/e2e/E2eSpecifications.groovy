package com.marketlogicsoftware.meetingreservation.e2e

import com.marketlogicsoftware.meetingreservation.endpoint.MeetingReservationEndpoint
import com.marketlogicsoftware.meetingreservation.reservation.MeetingCalendar
import com.marketlogicsoftware.meetingreservation.reservation.ReservationRequestProcessor
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


class E2eSpecifications extends Specification {

    MockMvc mockMvc

    def setup(){
        def meetingCalendar  = new MeetingCalendar()
        def reservationRequestProcessor = new ReservationRequestProcessor(meetingCalendar)
        mockMvc = MockMvcBuilders.standaloneSetup(new MeetingReservationEndpoint(reservationRequestProcessor)).build()
    }

    def "Should make a reservations and return reservations as text"(){
        given:
            def requestBody =
                    "0900 1730\n" +
                    "2018-05-17 10:17:06 EMP001\n" +
                    "2018-05-21 09:00 2\n" +
                    "2018-05-16 12:34:56 EMP002\n" +
                    "2018-05-21 09:00 2\n" +
                    "2018-05-16 09:28:23 EMP003\n" +
                    "2018-05-22 14:00 2\n" +
                    "2018-05-17 11:23:45 EMP004\n" +
                    "2018-05-22 16:00 1\n" +
                    "2018-05-15 17:29:12 EMP005\n" +
                    "2018-05-21 16:00 3\n" +
                    "2018-05-30 17:29:12 EMP006\n" +
                    "2018-05-21 10:00 3"

            def expectedResponseBody =
                    "2018-05-21\n" +
                    "09:00 11:00 EMP002\n" +
                    "2018-05-22\n" +
                    "14:00 16:00 EMP003\n" +
                    "16:00 17:00 EMP004"

        when: "a Post request is performed and request body is provided"
        def result = mockMvc.perform(
                post("/timetable-creation")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)).andReturn()

        then: "Response status 200 (Ok) and response body is what've been returned from the processor"
        with(result.response) {
            status == HttpStatus.OK.value()
            contentAsString.trim() == expectedResponseBody
        }
    }
}
