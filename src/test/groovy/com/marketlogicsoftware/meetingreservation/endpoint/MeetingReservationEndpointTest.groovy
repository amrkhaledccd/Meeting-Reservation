package com.marketlogicsoftware.meetingreservation.endpoint

import com.marketlogicsoftware.meetingreservation.reservation.ReservationRequestProcessor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class MeetingReservationEndpointTest extends Specification {

    MockMvc mockMvc

    def setup(){
        def reservationRequestProcessor = Mock(ReservationRequestProcessor)
        reservationRequestProcessor.processBatchReservations(_) >> "mocked batch processing"
        mockMvc = MockMvcBuilders.standaloneSetup(new MeetingReservationEndpoint(reservationRequestProcessor)).build()
    }

    def "Should processes batch reservation request and return reservations as text" (){

        when: "a Post request is performed and request body is provided"
         def result = mockMvc.perform(
                            post("/timetable-creation")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("Test content")).andReturn()

        then: "Response status 200 (Ok) and response body is what've been returned from the processor"
            with(result.response) {
                status == HttpStatus.OK.value()
                contentAsString == "mocked batch processing"
            }
    }

    def "Should return bad request when body is empty" (){

        when: "a Post request is performed with empty body"
        def result = mockMvc.perform(
                post("/timetable-creation")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("")).andReturn()

        then: "Response status 400 (Bad request) and error description as body"
        with(result.response) {
            status == HttpStatus.BAD_REQUEST.value()
            contentAsString == "Missing request body"
        }
    }
}
