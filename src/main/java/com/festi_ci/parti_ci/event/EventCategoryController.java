package com.festi_ci.parti_ci.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/event-categories")
public class EventCategoryController {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryController(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @GetMapping
    public List<EventCategoryModel> getAllEventCategories() {
        return eventCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EventCategoryModel> getEventCategoryById(@PathVariable Long id) {
        return eventCategoryRepository.findById(id);
    }
}