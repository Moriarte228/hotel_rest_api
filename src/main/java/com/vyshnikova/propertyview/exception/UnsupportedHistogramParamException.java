package com.vyshnikova.propertyview.exception;

public class UnsupportedHistogramParamException extends RuntimeException {
    public UnsupportedHistogramParamException(String param) {
        super("Unsupported histogram parameter: " + param + ". Allowed: brand, city, country, amenities");
    }
}