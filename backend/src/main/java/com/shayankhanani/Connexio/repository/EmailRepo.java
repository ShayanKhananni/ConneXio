package com.shayankhanani.Connexio.repository;

import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface EmailRepo extends JpaRepository<Email,Long> {

    List<Email> findAllByIdInAndContact(List<Long> ids, Contact contact);
    List<Email> findByContactAndEmailIn(Contact contact, List<String> emails);


}
