package com.vyshnikova.propertyview.dto;

import java.util.List;

//От сервера к клиенту
public record HotelDetailsDto(
        Long id,
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        List<String> amenities) {}