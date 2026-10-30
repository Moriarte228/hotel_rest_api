package com.vyshnikova.propertyview.service;

import com.vyshnikova.propertyview.dto.CreateHotelRequest;
import com.vyshnikova.propertyview.dto.HotelDetailsDto;
import com.vyshnikova.propertyview.dto.HotelShortDto;
import com.vyshnikova.propertyview.entity.Hotel;
import com.vyshnikova.propertyview.exception.HotelNotFoundException;
import com.vyshnikova.propertyview.mapper.HotelMapper;
import com.vyshnikova.propertyview.repository.HotelRepository;
import com.vyshnikova.propertyview.specification.HotelSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository repository;
    private final HotelMapper mapper;

    public HotelService(HotelRepository repository, HotelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<HotelShortDto> getAll() {
        return repository.findAll(Sort.by("id")).stream()
                .map(mapper::toShortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelDetailsDto getById(Long id) {
        Hotel hotel = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return mapper.toDetailsDto(hotel);
    }

    @Transactional
    public HotelShortDto create(CreateHotelRequest request) {
        Hotel saved = repository.save(mapper.toEntity(request));
        return mapper.toShortDto(saved);
    }

    @Transactional(readOnly = true)
    public List<HotelShortDto> search(String name, String brand, String city,
                                      String country, List<String> amenities) {
        List<Specification<Hotel>> specs = new ArrayList<>();

        if (StringUtils.hasText(name)) specs.add(HotelSpecifications.nameContains(name));
        if (StringUtils.hasText(brand)) specs.add(HotelSpecifications.brandEquals(brand));
        if (StringUtils.hasText(city)) specs.add(HotelSpecifications.cityEquals(city));
        if (StringUtils.hasText(country)) specs.add(HotelSpecifications.countryEquals(country));
        if (amenities != null) {
            for (String amenity : amenities) {
                if (StringUtils.hasText(amenity)) specs.add(HotelSpecifications.hasAmenity(amenity));
            }
        }

        return repository.findAll(Specification.allOf(specs), Sort.by("id")).stream()
                .map(mapper::toShortDto)
                .toList();
    }
}