package com.shayankhanani.Connexio.services;


import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Userprincipal;

import java.util.List;

public interface ContactService {

    ContactDetailDTO getContactDetails(Userprincipal owner, Long id);
    List<ContactInfoDTO> getContactsInfo(Userprincipal owner);

    ContactDetailDTO addContact(AddedContactDTO addedContactDTO, Userprincipal owner);
    ContactDetailDTO updateContactInfo(Long id, UpdateContactInfoDTO updateContactInfoDTO, Userprincipal owner);
    void updateEmails(Contact contact, List<UpdateEmailDTO> updates);
    void updatePhones(Contact contact, List<UpdatePhoneDTO> updates);
    void deleteById(Long id, Userprincipal owner);
}
