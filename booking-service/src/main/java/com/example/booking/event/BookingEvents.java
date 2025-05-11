package com.example.booking.event;

public class BookingEvents {
  public static final String REQUESTED = "booking_requested";
  public record BookingRequested(String bookingId, String itemId) {}
  public record InventoryReserved(String bookingId, boolean success) {}
}
