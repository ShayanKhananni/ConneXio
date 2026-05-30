package com.shayankhanani.connexio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        name = "contact_emails",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_contact_email",
                        columnNames = {"contact_id", "email"}
                )
        }
)

@Setter
@Getter
public class ContactEmail {
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
