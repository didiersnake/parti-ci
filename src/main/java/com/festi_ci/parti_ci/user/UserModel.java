package com.festi_ci.parti_ci.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity @Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String userName;
    private String keycloakId;
    @Column(nullable = false) private String phoneNumber;
    @Email(message = "Email is not valid")
    @Column(unique = true, nullable = false)
    private String email;

}