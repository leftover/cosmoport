package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public final class ShipSpecifications {
    public static Specification<Ship> name(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Ship> planet(String planet) {
        return (root, query, builder) -> planet == null ? null : builder.like(root.get("planet"), "%" + planet + "%");
    }

    public static Specification<Ship> shipType(ShipType shipType) {
        return (root, query, builder) -> shipType == null ? null : builder.equal(root.get("shipType"), shipType);
    }

    public static Specification<Ship> dateAfter(Long after) {
        return (root, query, builder) -> after == null ? null : builder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after));
    }

    public static Specification<Ship> dateBefore(Long before) {
        return (root, query, builder) -> before == null ? null : builder.lessThanOrEqualTo(root.get("prodDate"), new Date(before));
    }

    public static Specification<Ship> used(Boolean isUsed) {
        return (root, query, builder) -> isUsed == null ? null : builder.equal(root.get("isUsed"), isUsed);
    }

    public static Specification<Ship> speedMin(Double minSpeed) {
        return (root, query, builder) -> minSpeed == null ? null : builder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
    }

    public static Specification<Ship> speedMax(Double maxSpeed) {
        return (root, query, builder) -> maxSpeed == null ? null : builder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
    }

    public static Specification<Ship> crewMin(Integer minCrewSize) {
        return (root, quiery, builder) -> minCrewSize == null ? null : builder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
    }

    public static Specification<Ship> crewMax(Integer maxCrewSize) {
        return (root, quiery, builder) -> maxCrewSize == null ? null : builder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
    }

    public static Specification<Ship> ratingMin(Double minRating) {
        return (root, query, builder) -> minRating == null ? null : builder.greaterThanOrEqualTo(root.get("rating"), minRating);
    }

    public static Specification<Ship> ratingMax(Double maxRating) {
        return (root, query, builder) -> maxRating == null ? null : builder.lessThanOrEqualTo(root.get("rating"), maxRating);
    }

    public static Specification<Ship> getFullSpecification(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating) {
        return Specification
                .where(name(name))
                .and(planet(planet))
                .and(shipType(shipType))
                .and(dateAfter(after))
                .and(dateBefore(before))
                .and(used(isUsed))
                .and(speedMin(minSpeed))
                .and(speedMax(maxSpeed))
                .and(crewMin(minCrewSize))
                .and(crewMax(maxCrewSize))
                .and(ratingMin(minRating))
                .and(ratingMax(maxRating));
    }
}

