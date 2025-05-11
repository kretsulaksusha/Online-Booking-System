package com.example.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    private String id;
    private String itemId;
    private int quantity;
    private Instant createdAt;

    public Booking() {}

    // === ГЕТТЕРИ/СЕТТЕРИ ===
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
