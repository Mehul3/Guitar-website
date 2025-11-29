package com.guitarshop.product_service.controller;

import com.guitarshop.product_service.model.Guitar;
import com.guitarshop.product_service.repository.GuitarRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guitars")
public class GuitarController {

    private final GuitarRepository repository;

    public GuitarController(GuitarRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Guitar> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Guitar getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Guitar create(@RequestBody Guitar guitar) {
        return repository.save(guitar);
    }
}
