package com.festi_ci.parti_ci.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategoryModel, Long> {

    Optional<EventCategoryModel> findById(Long id);

    List<EventCategoryModel> findAll();

    Optional<EventCategoryModel> findByCategoryCode(String categoryCode);
}