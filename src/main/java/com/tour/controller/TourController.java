package com.tour.controller;

import com.tour.service.BookingService;
import com.tour.service.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tours")
public class TourController {

    private final TourService tourService;
    private final BookingService bookingService;

    public TourController(TourService tourService, BookingService bookingService) {
        this.tourService = tourService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String showTours(Model model,
                            @RequestParam(value = "message", required = false) String message) {
        model.addAttribute("tours", tourService.getAvailableTours());
        model.addAttribute("message", message);
        return "tours";
    }

    @PostMapping("/book")
    public String bookTour(@RequestParam("name") String name) {
        boolean booked = bookingService.bookTour(name);
        return "redirect:/tours?message=" + (booked ? "Tour+booked+successfully" : "Tour+not+found");
    }
}
