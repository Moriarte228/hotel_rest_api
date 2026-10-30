package com.vyshnikova.propertyview.histogram;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmenitiesHistogramStrategy implements HistogramStrategy{
    private final HotelRepository repository;

    public AmenitiesHistogramStrategy(HotelRepository repository) {
        this.repository = repository;
    }

    @Override
    public String param() {
        return "amenities";
    }

    @Override
    public List<HistogramEntry> count() {
        return repository.countByAmenities();
    }
}
