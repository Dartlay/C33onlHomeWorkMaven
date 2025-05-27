package com.tour.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MonitoringAspect {

    @Around("execution(* com.tour.service.BookingService.bookTour(..))")
    public Object measureBookingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        System.out.println("[MONITOR] Начало бронирования тура...");
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        System.out.println("[MONITOR] Бронирование заняло " + (endTime - startTime) + " мс");

        return result;
    }
}