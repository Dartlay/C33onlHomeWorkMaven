package org.example.service;

public interface PaymentService {
    boolean processPayment(String customerId, double amount);
}