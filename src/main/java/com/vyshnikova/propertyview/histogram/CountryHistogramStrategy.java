package com.vyshnikova.propertyview.histogram;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryHistogramStrategy implements HistogramStrategy{

    private final HotelRepository repository;

    CountryHistogramStrategy(HotelRepository repository){
        this.repository = repository;
    }

    @Override
    public String param() {
        return "country";
    }

    @Override
    public List<HistogramEntry> count() {
        return repository.countByCountry();
    }

}
