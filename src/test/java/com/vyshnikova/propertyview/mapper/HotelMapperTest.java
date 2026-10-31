package com.vyshnikova.propertyview.mapper;

import com.vyshnikova.propertyview.dto.HotelShortDto;
import com.vyshnikova.propertyview.entity.Address;
import com.vyshnikova.propertyview.entity.Contacts;
import com.vyshnikova.propertyview.entity.Hotel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HotelMapperTest {

    private final HotelMapper mapper = new HotelMapper();

    @Test
    void toShortDto_formatsAddressAsOneLine() {
        Address address = new Address();
        address.setHouseNumber(9);
        address.setStreet("Pobediteley Avenue");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220004");

        Contacts contacts = new Contacts();
        contacts.setPhone("+375 17 309-80-00");

        Hotel hotel = new Hotel();
        hotel.setName("DoubleTree by Hilton Minsk");
        hotel.setAddress(address);
        hotel.setContacts(contacts);

        HotelShortDto dto = mapper.toShortDto(hotel);

        assertThat(dto.address()).isEqualTo("9 Pobediteley Avenue, Minsk, 220004, Belarus");
        assertThat(dto.phone()).isEqualTo("+375 17 309-80-00");
    }
}