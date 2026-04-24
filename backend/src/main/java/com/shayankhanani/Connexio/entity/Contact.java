package com.shayankhanani.Connexio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    // who saved this contact
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contact;

    private String linkedinUrl;
    private String instaUrl;
    private String profUrl;
    private String fbUrl;

    // linked registered user
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User appUser;

    private LocalDateTime createdAt = LocalDateTime.now();
}