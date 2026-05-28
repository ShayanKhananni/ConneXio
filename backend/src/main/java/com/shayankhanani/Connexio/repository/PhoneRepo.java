package com.shayankhanani.Connexio.repository;

import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PhoneRepo extends JpaRepository<Phone,Long> {


    List<Phone> findAllByIdInAndContact(List<Long> ids, Contact contact);
    List<Phone> findByContactAndPhoneIn(Contact contact, List<String> phones);



}
