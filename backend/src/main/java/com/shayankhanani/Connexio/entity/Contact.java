package com.shayankhanani.Connexio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        name = "contacts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"owner_id", "contact"})
        }
)



@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String firsName;
    private String lastName;

    private String linkdUrl;
    private String instaUrl;
    private String profUrl;
    private String fbUrl;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User LinkedUser;


    @OneToMany(mappedBy = "contact")
    private List<Phone> phones = new ArrayList<>();
    @OneToMany(mappedBy = "contact")
    private List<Email> emails = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}