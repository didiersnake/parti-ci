package com.festi_ci.parti_ci.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findById(Long id);
//    Optional<UserModel> findByEmail(String email);
}