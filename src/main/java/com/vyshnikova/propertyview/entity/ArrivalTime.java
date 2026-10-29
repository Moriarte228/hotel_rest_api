package com.vyshnikova.propertyview.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public class ArrivalTime {
    private LocalTime checkIn;
    private LocalTime checkOut;
}