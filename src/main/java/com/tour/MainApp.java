package com.tour;

import com.tour.service.BookingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        BookingService bookingService = context.getBean("bookingService", BookingService.class);

        System.out.println("=== Демонстрация AOP ===");
        bookingService.displayAvailableTours();
        bookingService.bookTour("Paris Adventure");
        bookingService.bookTour("Unknown Tour");
    }
}