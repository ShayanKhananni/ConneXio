package com.shayankhanani.connexio.repository;


import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepo extends JpaRepository<Contact,Long> {

    Optional<Contact> findByContactIdAndOwner(Long contactId, User owner);
    List<Contact> findContactByOwner(User owner);
    Page<Contact> findContactByOwner(User owner, Pageable pageable);


    @Query("""
    SELECT c FROM Contact c
    WHERE c.owner = :owner
    AND (
        LOWER(c.firstName) LIKE LOWER(CONCAT(:search, '%'))
    )
""")
    Page<Contact> searchContacts(
            @Param("owner") User owner,
            @Param("search") String search,
            Pageable pageable
    );
}
