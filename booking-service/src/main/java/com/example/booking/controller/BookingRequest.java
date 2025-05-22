package com.example.booking.controller;

public class BookingRequest {
    private String itemId;
    private int quantity;

    public BookingRequest() {}

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
