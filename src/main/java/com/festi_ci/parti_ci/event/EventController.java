package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.fileManager.FileValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final FileValidationService fileValidationService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<?> createEvent(@Valid @ModelAttribute Request request) throws Exception {
        EventModel event = eventService.createEvent(request);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }
}