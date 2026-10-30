package com.vyshnikova.propertyview.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public class ArrivalTime {
    private LocalTime checkIn;
    private LocalTime checkOut;

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }
}