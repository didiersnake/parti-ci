package com.festi_ci.parti_ci.event;
import com.festi_ci.parti_ci.fileManager.FileModel;
import com.festi_ci.parti_ci.fileManager.FileValidationService;
import com.festi_ci.parti_ci.fileManager.MiniOService;
import com.festi_ci.parti_ci.location.LocationModel;
import com.festi_ci.parti_ci.location.LocationService;
import com.festi_ci.parti_ci.user.UserModel;
import com.festi_ci.parti_ci.user.UserRepository;
import com.festi_ci.parti_ci.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MiniOService miniOService;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final FileValidationService fileValidationService;
    private final LocationService locationService;

    @Transactional
    public EventModel createEvent(Request request) {
        // TODO: Get current user from security context
        UserModel user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        EventCategoryModel category = eventCategoryRepository.findByCategoryCode(request.categoryCode())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if(request.file() != null){
            fileValidationService.validateFile(request.file());
        }

        String fileId = null;
        try{
            // Add event cover to database if file exists
            if(request.file() != null){
                String eventCoverFolder = "event-covers";
                String filePath =  eventCoverFolder + "/" + category.getCategoryCode();
                FileModel file = miniOService.uploadFile(
                        request.file(), filePath, user.getId()
                );
                fileId = file.getStoredName();
            }
            String slug = StringUtils.generateEventKey() + "-" + StringUtils.slugify(request.title());
            LocationModel location = locationService.createLocation(request.location());
            EventModel event = EventModel.builder().title(request.title())
                    .templatePath(fileId == null ? request.templateCoverUUID() : fileId)
                    .monetizationType(request.feeType())
                    .dressCode(request.dressCode())
                    .startAt(OffsetDateTime.parse(request.date()).toLocalDateTime())
                    .maxCapacityEnabled(request.maxCapacityEnabled())
                    .host(request.host())
                    .maxCapacity(request.maxCapacity())
                    .startTime(request.startTime())
                    .isPrivate(request.isPrivate())
                    .waitListEnabled(request.waitListEnabled())
                    .description(request.description())
                    .locationName(request.locationName())
                    .location(location)
                    .eventCategory(category)
                    .slug(slug)
                    .organizer(user)
                    .build();
            return eventRepository.save(event);
        }catch (Exception e){
            log.error("Failed to create event", e);
            throw new RuntimeException("Failed to create event", e);
        }
    }

    public List<Response> findAllPublicEvents() {
        List<EventModel> eventModel = eventRepository.findPublicEventsWithDetails();
        return eventModel.stream().map(el -> {
            try {
                String templatePath = miniOService.getCoverUrl(el.getTemplatePath());
                el.setTemplatePath(templatePath);
                return Response.fromEntity(el);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public Optional<EventModel> findById(Long id) {
        return eventRepository.findById(id);
    }

    public ResponseDetails findBySlug(String slug) {
        EventModel eventModel = eventRepository.findEventBySlug(slug).orElse(null);
        if(eventModel != null){
            try{
                String templatePath = miniOService.getCoverUrl(eventModel.getTemplatePath());
                eventModel.setTemplatePath(templatePath);
                return ResponseDetails.fromEntity(eventModel);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}