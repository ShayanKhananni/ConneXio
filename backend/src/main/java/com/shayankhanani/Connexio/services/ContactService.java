package com.shayankhanani.Connexio.services;


import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Userprincipal;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    List<ContactDTO> getUserContacts(Userprincipal owner);
    ContactDTO addContact(AddedContactDTO addedContactDTO, Userprincipal owner);
    ContactDTO updateContactInfo(Long id, UpdateContactInfoDTO updateContactInfoDTO, Userprincipal owner);
    void updateEmails(Contact contact, List<UpdateEmailDTO> updates);
    void updatePhones(Contact contact, List<UpdatePhoneDTO> updates);
    void deleteById(Long id, Userprincipal owner);
}
