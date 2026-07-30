package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.fileManager.FileValidationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {
    private final EventService eventService;
    private final FileValidationService fileValidationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(@Valid @ModelAttribute Request request) throws Exception {
        EventModel event = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() throws Exception {
        List<Response> events = eventService.findAllPublicEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getEventBySlug(@PathVariable String slug){
        ResponseDetails event = eventService.findBySlug(slug);
        return ResponseEntity.ok(event);
    }
}