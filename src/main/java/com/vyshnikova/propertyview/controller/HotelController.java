package com.vyshnikova.propertyview.controller;

import com.vyshnikova.propertyview.dto.CreateHotelRequest;
import com.vyshnikova.propertyview.dto.HotelDetailsDto;
import com.vyshnikova.propertyview.dto.HotelShortDto;
import com.vyshnikova.propertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hotels")
@RestController
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @Operation(summary = "Get all hotels (short info)")
    @GetMapping("/hotels")
    public List<HotelShortDto> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get detailed information about a hotel")
    @GetMapping("/hotels/{id}")
    public HotelDetailsDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Create a new hotel")
    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelShortDto create(@Valid @RequestBody CreateHotelRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Add amenities to a hotel (duplicates are ignored)")
    @GetMapping("/search")
    public List<HotelShortDto> search(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String brand,
                                      @RequestParam(required = false) String city,
                                      @RequestParam(required = false) String country,
                                      @RequestParam(required = false) List<String> amenities) {
        return service.search(name, brand, city, country, amenities);
    }

    @Operation(summary = "Search hotels by name, brand, city, country and amenities")
    @PostMapping("/hotels/{id}/amenities")
    public void addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        service.addAmenities(id, amenities);
    }
}