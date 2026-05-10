package com.shayankhanani.Connexio.entity;

import jakarta.persistence.*;


@Entity
@Table(
        name = "contact_emails",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"contact_id","email"})
        }
)

public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(nullable = false)
    private String label;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
