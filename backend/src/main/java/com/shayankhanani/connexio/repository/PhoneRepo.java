package com.shayankhanani.connexio.repository;

import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepo extends JpaRepository<ContactPhone,Long> {


    List<ContactPhone> findAllByIdInAndContact(List<Long> ids, Contact contact);
    List<ContactPhone> findByContactAndPhoneIn(Contact contact, List<String> phones);



}
