package org.example.service;


public interface InventoryService {
    boolean checkInventory(String productId, int quantity);
    void updateInventory(String productId, int quantity);
}