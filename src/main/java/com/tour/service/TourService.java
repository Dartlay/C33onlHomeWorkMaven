package com.tour.service;

import com.tour.model.Tour;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    private List<Tour> availableTours;

    public TourService() {
        this.availableTours = new ArrayList<>();
        availableTours.add(new Tour("Paris Adventure", 999.99));
        availableTours.add(new Tour("Italian Dream", 1299.99));
        availableTours.add(new Tour("Asian Discovery", 1599.99));
    }

    public List<Tour> getAvailableTours() {
        return new ArrayList<>(availableTours);
    }

    public Optional<Tour> findTourByName(String name) {
        return availableTours.stream()
                .filter(tour -> tour.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public boolean addTour(Tour newTour) {
        if (newTour == null || newTour.getName() == null || newTour.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tour name cannot be null or empty");
        }
        if (newTour.getPrice() <= 0) {
            throw new IllegalArgumentException("Tour price must be positive");
        }
        return availableTours.add(newTour);
    }
}