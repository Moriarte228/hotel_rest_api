package com.vyshnikova.propertyview.histogram;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandHistogramStrategy implements HistogramStrategy {

    private final HotelRepository repository;

    public BrandHistogramStrategy(HotelRepository repository) {
        this.repository = repository;
    }

    @Override
    public String param() {
        return "brand";
    }

    @Override
    public List<HistogramEntry> count() {
        return repository.countByBrand();
    }
}