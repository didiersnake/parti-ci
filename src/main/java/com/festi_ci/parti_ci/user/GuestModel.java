package com.festi_ci.parti_ci.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GuestModel {
    private String name;
    private String email;
    private String phoneNumber;
    private String registeredAt;
}