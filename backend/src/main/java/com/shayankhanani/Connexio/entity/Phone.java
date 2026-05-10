package com.shayankhanani.Connexio.entity;

import jakarta.persistence.*;


@Entity
@Table(
        name = "contact_phone",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"contact_id","phone"})
        },
        indexes = {
                @Index(name = "idx_phone_number", columnList = "phone")
        }
)

public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;

    @Column(nullable = false)
    private String label;


    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
