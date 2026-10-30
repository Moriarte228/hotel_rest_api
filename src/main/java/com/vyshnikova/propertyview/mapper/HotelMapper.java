package com.vyshnikova.propertyview.mapper;

import com.vyshnikova.propertyview.dto.*;
import com.vyshnikova.propertyview.entity.Address;
import com.vyshnikova.propertyview.entity.ArrivalTime;
import com.vyshnikova.propertyview.entity.Contacts;
import com.vyshnikova.propertyview.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelMapper {

    public HotelShortDto toShortDto(Hotel hotel) {
        return new HotelShortDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                formatAddress(hotel.getAddress()),
                hotel.getContacts().getPhone());
    }

    public HotelDetailsDto toDetailsDto(Hotel hotel) {
        Address a = hotel.getAddress();
        Contacts c = hotel.getContacts();
        ArrivalTime t = hotel.getArrivalTime();
        return new HotelDetailsDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                new AddressDto(a.getHouseNumber(), a.getStreet(), a.getCity(), a.getCountry(), a.getPostCode()),
                new ContactsDto(c.getPhone(), c.getEmail()),
                new ArrivalTimeDto(t.getCheckIn(), t.getCheckOut()),
                List.copyOf(hotel.getAmenities()));
    }

    public Hotel toEntity(CreateHotelRequest request) {
        Address address = new Address();
        address.setHouseNumber(request.address().houseNumber());
        address.setStreet(request.address().street());
        address.setCity(request.address().city());
        address.setCountry(request.address().country());
        address.setPostCode(request.address().postCode());

        Contacts contacts = new Contacts();
        contacts.setPhone(request.contacts().phone());
        contacts.setEmail(request.contacts().email());

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(request.arrivalTime().checkIn());
        arrivalTime.setCheckOut(request.arrivalTime().checkOut());

        Hotel hotel = new Hotel();
        hotel.setName(request.name());
        hotel.setDescription(request.description());
        hotel.setBrand(request.brand());
        hotel.setAddress(address);
        hotel.setContacts(contacts);
        hotel.setArrivalTime(arrivalTime);
        return hotel;
    }

    private String formatAddress(Address a) {
        return a.getHouseNumber() + " " + a.getStreet() + ", " + a.getCity() + ", "
                + a.getPostCode() + ", " + a.getCountry();
    }
}