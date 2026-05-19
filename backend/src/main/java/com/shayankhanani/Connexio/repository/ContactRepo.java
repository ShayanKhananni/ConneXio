package com.shayankhanani.Connexio.repository;


import com.shayankhanani.Connexio.DTO.Contact.UserContactListDTO;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepo extends JpaRepository<Contact,Long> {

    Optional<Contact> findByContactIdAndOwner(Long contactId, User owner);
    List<Contact> findContactByOwner(User owner);

}
