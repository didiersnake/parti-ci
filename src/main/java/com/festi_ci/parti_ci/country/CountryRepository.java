package com.festi_ci.parti_ci.country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryModel, Long> {

    List<CountryModel> findAll();


}