package com.shayankhanani.Connexio.repository;


import com.shayankhanani.Connexio.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact,Long> {

}
