# Meeting Reservation

## Assumptions

I assumed that the input is correctly provided, so I didn't do any input validations

I assumed that only one batch request processing at a time (No threading issues)

## Time taken

Time taken is 2 hours 30 min (I spent some time reviewing Java.time apis)

## Endpoints

Health:  http://localhost:8080/health  (I used the default spring actuator endpoints, no need for customizations) 

TimeTable: http://localhost:8080/timetable-creation

## Components

MetingReservationEndpoint - provides the reservations endpoints, It recieves reservation request and send it to the ReservationRequestProcessor .

ReservationRequestProcessor - It is responsible to parse the reservation request string, convert it to Reservation objects and send them to MeetingCalendar to make the reservations.

MeetingCalendar - It is responsible for validating that the reservation is within office hours, check for overlaps and make the reservation.

Calendar uses Map to group the reservations by day.

## Build and Run

Navigate to project folder 
    
       ./bulid.sh
       ./run.sh

 
