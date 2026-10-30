package com.vyshnikova.propertyview.repository;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("""
        select new com.vyshnikova.propertyview.dto.HistogramEntry(h.brand, count(h))
        from Hotel h
        where h.brand is not null
        group by h.brand
        order by h.brand
        """)
    List<HistogramEntry> countByBrand();

    @Query("""
        select new com.vyshnikova.propertyview.dto.HistogramEntry(h.address.city, count(h))
        from Hotel h
        where h.address.city is not null
        group by h.address.city
        order by h.address.city
        """)
    List<HistogramEntry> countByCity();

    @Query("""
        select new com.vyshnikova.propertyview.dto.HistogramEntry(h.address.country, count(h))
        from Hotel h
        where h.address.country is not null
        group by h.address.country
        order by h.address.country
        """)
    List<HistogramEntry> countByCountry();

    @Query("""
        select new com.vyshnikova.propertyview.dto.HistogramEntry(a, count(h))
        from Hotel h join h.amenities a
        where a is not null
        group by a
        order by a
        """)
    List<HistogramEntry> countByAmenities();
}