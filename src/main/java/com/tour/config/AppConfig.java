package com.tour.config;

import com.tour.service.BookingService;
import com.tour.service.TourService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public TourService tourService() {
        return new TourService();
    }

    @Bean
    public BookingService bookingService() {
        BookingService bookingService = new BookingService();
        bookingService.setTourService(tourService());
        return bookingService;
    }
}