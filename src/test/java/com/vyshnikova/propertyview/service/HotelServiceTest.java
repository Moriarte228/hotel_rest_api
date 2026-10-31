package com.vyshnikova.propertyview.service;

import com.vyshnikova.propertyview.entity.Hotel;
import com.vyshnikova.propertyview.exception.HotelNotFoundException;
import com.vyshnikova.propertyview.mapper.HotelMapper;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository repository;

    private HotelService service;

    @BeforeEach
    void setUp() {
        service = new HotelService(repository, new HotelMapper());
    }

    @Test
    void addAmenities_skipsDuplicates() {
        Hotel hotel = new Hotel();
        hotel.getAmenities().add("Free WiFi");
        when(repository.findById(1L)).thenReturn(Optional.of(hotel));

        service.addAmenities(1L, List.of("Free WiFi", "Spa", "Spa"));

        assertThat(hotel.getAmenities()).containsExactly("Free WiFi", "Spa");
    }

    @Test
    void getById_unknownId_throwsNotFound() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(42L))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessage("Hotel with id 42 not found");
    }
}