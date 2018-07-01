package com.marketlogicsoftware.meetingreservation.util

import spock.lang.Specification

import java.util.stream.Collectors

class ListUtilTest extends Specification {

    def "Should divide the list into paris"(){

        given: "List of Integers"
            def list = Arrays.asList(1, 2, 3, 4, 5, 6)
            def expectedList = Arrays.asList(Arrays.asList(1, 2),
                                             Arrays.asList(3, 4),
                                             Arrays.asList(5, 6))

        when: "Calling sliding method"
            def result = ListUtil.getInstance().sliding(list).collect(Collectors.toList())

        then: "list should be divided into paris"
            result == expectedList

    }
}
