package com.example.booking.repository;

import com.example.booking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {}
