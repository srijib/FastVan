package com.fast.van.models.enums;

public enum ApiResponseFlags {

    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    USER_ALREADY_CHECKED_IN(409),
    LOCATION_NOT_SERVED(512),
    CARS_NOTAVAIABLE(425),
    NO_BOOKINGS_FOUND(421), CustomerNotFound(411),
    INTERNAL_SERVER_ERROR(500);

    private int ordinal;

    ApiResponseFlags(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}