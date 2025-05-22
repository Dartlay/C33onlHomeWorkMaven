package com.tour;

import com.tour.service.BookingService;
import com.tour.service.TourService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        BookingService bookingService = context.getBean("bookingService", BookingService.class);
        TourService tourService = context.getBean("tourService", TourService.class);


        bookingService.displayAvailableTours();


        bookingService.bookTour("Paris Adventure");


        bookingService.bookTour("Unknown Tour");

       
        tourService.addTour(new com.tour.model.Tour("African Safari", 1999.99));
        System.out.println("\nAfter adding new tour:");
        bookingService.displayAvailableTours();
    }
}