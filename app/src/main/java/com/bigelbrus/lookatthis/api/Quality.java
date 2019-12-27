package com.bigelbrus.lookatthis.api;

public enum Quality {
    RAW("raw"),
    FULL("full"),
    REGULAR("regular"),
    SMALL("small"),
    THUMB("thumb"),
    TO_SCREEN_WIDTH("to screen width");

    String quality;

    Quality(String quality) {
        this.quality = quality;
    }
}
