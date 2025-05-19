package org.example.facade;

import org.example.service.*;

import javax.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class OrderFacadeImpl implements OrderFacade {
    @Inject
    private InventoryService inventoryService;

    @Inject
    private PaymentService paymentService;

    @Inject
    private ShippingService shippingService;

    @Override
    public String placeOrder(String customerId, String productId, int quantity, double amount) {
        if (!inventoryService.checkInventory(productId, quantity)) {
            return "Product out of stock";
        }

        if (!paymentService.processPayment(customerId, amount)) {
            return "Payment failed";
        }

        inventoryService.updateInventory(productId, quantity);
        String trackingNumber = shippingService.scheduleShipping(customerId, productId);

        return "Order placed successfully. Tracking number: " + trackingNumber;
    }
}