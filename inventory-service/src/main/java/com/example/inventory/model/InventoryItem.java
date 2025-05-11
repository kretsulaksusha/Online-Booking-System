package com.example.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class InventoryItem {
    @Id
    private String id;
    private String type;
    private int availableCount;
    private double price;

    // Порожній конструктор потрібен для десеріалізації
    public InventoryItem() {}

    // Конструктор за бажанням
    public InventoryItem(String type, int availableCount, double price) {
        this.type = type;
        this.availableCount = availableCount;
        this.price = price;
    }

    // === ГЕТТЕРИ ===
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public int getAvailableCount() {
        return availableCount;
    }
    public double getPrice() {
        return price;
    }

    // === СЕТТЕРИ ===
    public void setId(String id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
