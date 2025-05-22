package com.tour;

import com.tour.config.AppConfig;
import com.tour.service.BookingService;
import com.tour.service.TourService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        BookingService bookingService = context.getBean(BookingService.class);
        TourService tourService = context.getBean(TourService.class);

        bookingService.displayAvailableTours();
        bookingService.bookTour("Paris Adventure");
        bookingService.bookTour("Unknown Tour");

        tourService.addTour(new com.tour.model.Tour("African Safari", 1999.99));
        System.out.println("\nAfter adding new tour:");
        bookingService.displayAvailableTours();

        context.close();
    }
}