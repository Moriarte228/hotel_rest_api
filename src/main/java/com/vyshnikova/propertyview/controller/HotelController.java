package com.vyshnikova.propertyview.controller;

import com.vyshnikova.propertyview.dto.HotelShortDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HotelController {

    @GetMapping("/hotels")
    public List<HotelShortDto> getAll() {
        return List.of(new HotelShortDto(
                1L,
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        ));
    }
}
