package com.vyshnikova.propertyview.histogram;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityHistogramStrategy  implements HistogramStrategy {
    private final HotelRepository repository;

    public CityHistogramStrategy(HotelRepository repository) {
        this.repository = repository;
    }

    @Override
    public String param() {
        return "city";
    }

    @Override
    public List<HistogramEntry> count() {
        return repository.countByCity();
    }
}
