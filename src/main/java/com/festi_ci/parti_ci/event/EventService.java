package com.festi_ci.parti_ci.event;
import com.festi_ci.parti_ci.fileManager.FileModel;
import com.festi_ci.parti_ci.fileManager.FileValidationService;
import com.festi_ci.parti_ci.fileManager.MiniOService;
import com.festi_ci.parti_ci.user.UserModel;
import com.festi_ci.parti_ci.user.UserRepository;
import com.festi_ci.parti_ci.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Transactional
    public EventModel createEvent(Request request) {
        // TODO: Get current user from security context
        UserModel user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        EventCategoryModel category = eventCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if(!request.file().isEmpty()){
            fileValidationService.validateFile(request.file());
        }

        String fileId = null;
        try{
            // Add event cover to database if file exists
            if(!request.file().isEmpty()){
                String eventCoverFolder = "event-covers";
                String filePath =  eventCoverFolder + "/" + category.getName();
                FileModel file = miniOService.uploadFile(
                        request.file(), filePath, user.getId()
                );
                fileId = file.getId();
            }
            String slug = "events" + "/" +
                    StringUtils.generateEventKey() + "/" + StringUtils.slugify(request.title());
            //TODO: Add event location landmark and pinpoint address

            EventModel event = EventModel.builder().title(request.title())
                    .templateCoverUUID(fileId == null ? request.templateCoverUUID() : fileId)
                    .privacyStatus(request.privacyStatus())
                    .feeType(request.feeType())
                    .dressCode(request.dressCode())
                    .startAt(request.startAt())
                    .maxCapacity(request.maxCapacity())
                    .eventCategoryId(category)
                    .slug(slug)
                    .organizerId(user)
                    .build();
            return eventRepository.save(event);
        }catch (Exception e){
            log.error("Failed to create event", e);
            throw new RuntimeException("Failed to create event", e);
        }
    }

    public Optional<EventModel> findById(Long id) {
        return eventRepository.findById(id);
    }

}