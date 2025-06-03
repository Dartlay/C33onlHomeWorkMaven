package com.tour.service;

import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private final TourService tourService;

    public BookingService(TourService tourService) {
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
