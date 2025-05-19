package org.example.facade;


public interface OrderFacade {
    String placeOrder(String customerId, String productId,
                      int quantity, double amount);
}