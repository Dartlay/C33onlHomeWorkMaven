package com.tour.service;

import com.tour.model.Tour;

public class BookingService {
    private TourService tourService;

    public void setTourService(TourService tourService) {
        this.tourService = tourService;
    }

    public boolean bookTour(String tourName) {
        System.out.println("Attempting to book tour: " + tourName);

        return tourService.findTourByName(tourName)
                .map(tour -> {
                    System.out.println("Successfully booked tour: " + tour.getName() + " for $" + tour.getPrice());
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Tour not found: " + tourName);
                    return false;
                });
    }

    public void displayAvailableTours() {
        System.out.println("Available Tours:");
        tourService.getAvailableTours().forEach(tour ->
                System.out.println("- " + tour.getName() + " ($" + tour.getPrice() + ")")
        );
    }
}