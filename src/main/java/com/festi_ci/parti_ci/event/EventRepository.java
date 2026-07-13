package com.festi_ci.parti_ci.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {

    Optional<EventModel> findById(Long id);

    List<EventModel> findAll();

    Optional<EventModel> findBySlug(String slug);
}