package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ShipService {
    private ShipRepository repo;

    public ShipService(ShipRepository repo) {
        this.repo = repo;
    }

    public List<Ship> listAllShips(
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
            Double maxRating,
            ShipOrder order,
            Integer pageNumber,
            Integer pageSize) {

        if (order == null) order = ShipOrder.ID;
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()).ascending());
        return repo.findAll(ShipSpecifications.getFullSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating),
                pageable).getContent();
    }

    public Integer getCount(
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

        return (int) repo.count(ShipSpecifications.getFullSpecification(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating));
    }

    public Ship getShipById(Long id) {
        if (id == null || id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Ship saveShip(Ship ship) {
        if (ship.getSpeed() != null) ship.setSpeed(round2d(ship.getSpeed()));
        if (ship.getUsed() == null) ship.setUsed(false);
        if (!isShipValid(ship)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        ship.setRating(getRating(ship));
        return repo.save(ship);
    }

    public void deleteShipById(Long id) {
        if (id == null || id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }

    public Ship updateShip(Long id, Ship shipData) {
        Ship ship = getShipById(id);
        if(shipData.getName() != null) ship.setName(shipData.getName());
        if(shipData.getPlanet() != null) ship.setPlanet(shipData.getPlanet());
        if(shipData.getShipType() != null) ship.setShipType(shipData.getShipType());
        if(shipData.getProdDate() != null) ship.setProdDate(shipData.getProdDate());
        if(shipData.getUsed() != null) ship.setUsed(shipData.getUsed());
        if(shipData.getSpeed() != null) ship.setSpeed(shipData.getSpeed());
        if(shipData.getCrewSize() != null) ship.setCrewSize(shipData.getCrewSize());
        return saveShip(ship);
    }

    private boolean isShipValid(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null) {
            return false;
        }
        if (ship.getName().equals("") || ship.getName().length() > 50) return false;
        if (ship.getPlanet().equals("") || ship.getPlanet().length() > 50) return false;
        if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) return false;
        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) return false;
        if (ship.getProdDate().getTime() < 0) return false; //Not sure if this is necessary :)
        int year = ship.getProdDate().getYear() + 1900;
        if (year < 2800 || year > 3019) return false;
        return true;
    }

    private Double getRating(Ship ship) {
        int year = ship.getProdDate().getYear() + 1900;
        double speed = ship.getSpeed();
        boolean isUsed = ship.getUsed();
        int currentYear = 3019;
        double rating = 80.0 * speed / (currentYear - year + 1);
        if (isUsed) rating /= 2;
        return round2d(rating);
    }

    private Double round2d(Double value) {
        return Math.floor(value * 100.0 + 0.5) / 100;
    }
}
