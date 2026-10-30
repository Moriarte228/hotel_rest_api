package com.vyshnikova.propertyview.specification;

import com.vyshnikova.propertyview.entity.Hotel;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public final class HotelSpecifications {

    private HotelSpecifications() {
    }

    public static Specification<Hotel> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.<String>get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> brandEquals(String brand) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.<String>get("brand")), brand.toLowerCase());
    }

    public static Specification<Hotel> cityEquals(String city) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("address").<String>get("city")), city.toLowerCase());
    }

    public static Specification<Hotel> countryEquals(String country) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("address").<String>get("country")), country.toLowerCase());
    }

    public static Specification<Hotel> hasAmenity(String amenity) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Hotel, String> amenities = root.join("amenities");
            return cb.equal(cb.lower(amenities), amenity.toLowerCase());
        };
    }
}