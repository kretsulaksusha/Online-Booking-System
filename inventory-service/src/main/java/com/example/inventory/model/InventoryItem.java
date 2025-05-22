package com.example.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_item")
public class InventoryItem {
    @Id
    private String id;
    private String type;
    private int availableCount;
    private double price;

    public InventoryItem() {}

    public InventoryItem(String type, int availableCount, double price) {
        this.id = id;
        this.type = type;
        this.availableCount = availableCount;
        this.price = price;
    }

    // === ГЕТТЕРИ ===
    public String getId() { return id; }
    public String getType() { return type; }
    public int getAvailableCount() { return availableCount; }
    public double getPrice() { return price; }

    // === СЕТТЕРИ ===
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }
    public void setPrice(double price) { this.price = price; }
}
