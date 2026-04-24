package com.shayankhanani.Connexio.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String contact;

    private String email;
    private String username;

    private String linkedinUrl;
    private String instaUrl;
    private String profUrl;
    private String fbUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "owner")
    private List<Contact> contacts;
}