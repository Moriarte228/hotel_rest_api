package com.vyshnikova.propertyview.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}