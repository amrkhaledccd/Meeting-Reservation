package com.marketlogicsoftware.meetingreservation.util;


import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Utility class for Lists
 * @author Amr Saleh
 */
public class ListUtil {

    private static ListUtil instance;

    private ListUtil(){}

    public static ListUtil getInstance(){
        if(instance == null){
            instance = new ListUtil();
        }
        return instance;
    }

    /*
    * Converts the list into a list of list of paris
    * Each list contains the two consecutive elements
    * I've stolen the name from Scala
    */
    public <T> Stream<List<T>> sliding(List<T> list) {

        return IntStream
                .range(1, list.size())
                .filter(idx -> idx % 2 != 0) // Filter out even indices
                .mapToObj(idx -> Arrays.asList(list.get(idx - 1), list.get(idx)));
    }
}
