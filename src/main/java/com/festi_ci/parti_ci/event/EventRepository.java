package com.festi_ci.parti_ci.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {

    Optional<EventModel> findById(Long id);

    List<EventModel> findAll();

    @Query("SELECT e FROM EventModel e " +
            "JOIN FETCH e.eventCategory " + "WHERE e.isPrivate = false")
    List<EventModel> findPublicEventsWithDetails();

    @Query("SELECT e FROM EventModel e " +
            "JOIN FETCH e.eventCategory " +
            "JOIN FETCH e.location " + "WHERE e.slug = :slug")
    Optional<EventModel> findEventBySlug(String slug);
}