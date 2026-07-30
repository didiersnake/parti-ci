package com.festi_ci.parti_ci.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/event-categories")
@CrossOrigin("*")
public class EventCategoryController {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryController(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<EventCategoryModel>> getAllEventCategories() {
        return ResponseEntity.ok().body(eventCategoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public Optional<EventCategoryModel> getEventCategoryById(@PathVariable Long id) {
        return eventCategoryRepository.findById(id);
    }
}