package com.marketlogicsoftware.meetingreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class OfficeHours {
    LocalTime from;
    LocalTime to;
}
