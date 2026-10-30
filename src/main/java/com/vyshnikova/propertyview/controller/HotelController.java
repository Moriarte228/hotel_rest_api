package com.vyshnikova.propertyview.controller;

import com.vyshnikova.propertyview.dto.CreateHotelRequest;
import com.vyshnikova.propertyview.dto.HotelDetailsDto;
import com.vyshnikova.propertyview.dto.HotelShortDto;
import com.vyshnikova.propertyview.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping("/hotels")
    public List<HotelShortDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/hotels/{id}")
    public HotelDetailsDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelShortDto create(@Valid @RequestBody CreateHotelRequest request) {
        return service.create(request);
    }

    @GetMapping("/search")
    public List<HotelShortDto> search(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String brand,
                                      @RequestParam(required = false) String city,
                                      @RequestParam(required = false) String country,
                                      @RequestParam(required = false) List<String> amenities) {
        return service.search(name, brand, city, country, amenities);
    }
}