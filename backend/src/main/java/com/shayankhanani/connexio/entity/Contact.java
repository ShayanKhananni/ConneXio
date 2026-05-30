package com.shayankhanani.connexio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "contact")


@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String firstName;
    private String lastName;


    //optionals
    private String linkedinUrl;
    private String instagramUrl;
    private String profImageUrl;
    private String facebookUrl;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User linkedUser;


    @OneToMany(mappedBy = "contact", cascade = {CascadeType.PERSIST, CascadeType.MERGE
    ,                CascadeType.REMOVE})

    private List<ContactPhone> contactPhones = new ArrayList<>();

    @OneToMany(mappedBy = "contact", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private List<ContactEmail> contactEmails = new ArrayList<>();


    private LocalDateTime createdAt = LocalDateTime.now();
}