package com.vyshnikova.propertyview.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Contacts {
    private String phone;
    private String email;
}