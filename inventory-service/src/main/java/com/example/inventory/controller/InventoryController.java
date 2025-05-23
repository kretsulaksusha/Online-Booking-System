package com.example.inventory.controller;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryRepository repo;

    public InventoryController(InventoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<InventoryItem> all() {
        return repo.findAll();
    }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItem item) {
        if (item.getId() == null || item.getId().isBlank()) {
            item.setId(UUID.randomUUID().toString());
        }
        return repo.save(item);
    }

    @GetMapping("/{id}")
    public InventoryItem get(@PathVariable String id) {
        return repo.findById(id).orElseThrow();
    }

    @PatchMapping("/{id}")
    public InventoryItem update(@PathVariable String id,
                                @RequestBody InventoryItem upd) {
        InventoryItem ex = repo.findById(id).orElseThrow();
        ex.setAvailableCount(upd.getAvailableCount());
        ex.setPrice(upd.getPrice());
        return repo.save(ex);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
