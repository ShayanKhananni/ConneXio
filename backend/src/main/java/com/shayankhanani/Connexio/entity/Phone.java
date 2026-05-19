package com.shayankhanani.Connexio.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "contact_phone",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_contact_phone",
                        columnNames = {"contact_id", "phone"}
                )
        },
        indexes = {
                @Index(name = "idx_phone_number", columnList = "phone")
        }
)

@Setter
@Getter

public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;

//    @Column(nullable = false)
//    private String label;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
