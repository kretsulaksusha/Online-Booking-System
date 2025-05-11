package com.example.booking.controller;

import com.example.booking.event.BookingEvents;
import com.example.booking.model.Booking;
import com.example.booking.controller.BookingRequest;
import com.example.booking.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingRepository repo;
    private final KafkaTemplate<String, Object> kafka;

    public BookingController(BookingRepository repo,
                             KafkaTemplate<String, Object> kafka) {
        this.repo  = repo;
        this.kafka = kafka;
    }

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody BookingRequest req) {
        Booking b = new Booking();
        b.setItemId(req.getItemId());
        b.setQuantity(req.getQuantity());
        // генеруємо id
        b.setId(UUID.randomUUID().toString());
        b.setCreatedAt(Instant.now());

        Booking saved = repo.save(b);
        kafka.send(BookingEvents.REQUESTED,
                   new BookingEvents.BookingRequested(saved.getId(), saved.getItemId()));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> get(@PathVariable String id) {
        return repo.findById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Booking> all() {
        return repo.findAll();
    }
}
