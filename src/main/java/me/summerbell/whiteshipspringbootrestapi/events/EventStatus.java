package me.summerbell.whiteshipspringbootrestapi.events;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
public enum EventStatus {

    DRAFT, PUBLISHED, BEGIN_ENROLLMENT;

}
