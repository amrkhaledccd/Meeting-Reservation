package com.marketlogicsoftware.meetingreservation.util

import spock.lang.Specification

import java.time.LocalTime

/*
 * TODO write tests for other methods
 * I only wrote a tests for checkOverlap
 */
class DateTimeUtilTest extends Specification {

    def "DateTimeUtil should return true for overlapped periods" () {
        given: "two overlapped period"
            def periodOneFrom = LocalTime.parse("09:30")
            def periodOneTo = LocalTime.parse("13:30")
            def periodTwoFrom = LocalTime.parse("11:30")
            def periodTwoTo = LocalTime.parse("14:30")

        and: "another overlapped period"
            def periodThreeFrom = LocalTime.parse("09:30")
            def periodThreeTo = LocalTime.parse("16:30")

        when: "calling check overlap method"
            def result1 = DateTimeUtil.getInstance().checkOverlap(periodOneFrom,
                    periodOneTo, periodTwoFrom, periodTwoTo)

        and:
            def result2 = DateTimeUtil.getInstance().checkOverlap(periodThreeFrom,
                    periodThreeTo, periodTwoFrom, periodTwoTo)

        then: "should return true"
            result1 == true
            result2 == true

    }

    def "DateTimeUtil should return false for non overlapped periods" () {
        given:
        def periodOneFrom = LocalTime.parse("09:30")
        def periodOneTo = LocalTime.parse("13:30")
        def periodTwoFrom = LocalTime.parse("13:30")
        def periodTwoTo = LocalTime.parse("14:30")

        when:
        def result = DateTimeUtil.getInstance().checkOverlap(periodOneFrom,
                periodOneTo, periodTwoFrom, periodTwoTo)


        then:
        result == false
    }

}
