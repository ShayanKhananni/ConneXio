package com.shayankhanani.connexio.repository;

import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepo extends JpaRepository<ContactEmail,Long> {

    List<ContactEmail> findAllByIdInAndContact(List<Long> ids, Contact contact);
    List<ContactEmail> findByContactAndEmailIn(Contact contact, List<String> emails);


}
