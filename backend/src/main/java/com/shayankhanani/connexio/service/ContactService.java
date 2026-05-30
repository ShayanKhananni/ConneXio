package com.shayankhanani.connexio.service;


import com.shayankhanani.connexio.dto.contact.*;
import com.shayankhanani.connexio.dto.contact.patch.UpdateContactDTO;
import com.shayankhanani.connexio.dto.contact.patch.UpdateEmailDTO;
import com.shayankhanani.connexio.dto.contact.patch.UpdatePhoneDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.Userprincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    ContactDetailDTO getContactDetails(Userprincipal owner, Long id);
    List<ContactInfoDTO> getContactsInfo(Userprincipal owner);
    ContactDetailDTO addContact(AddContactDTO addContactDTO, Userprincipal owner);
    ContactDetailDTO updateContactInfo(Long id, UpdateContactDTO updateContactInfoDTO, Userprincipal owner);
    void patchEmails(Contact contact, List<UpdateEmailDTO> updateEmailDTOS);
    void patchPhones(Contact contact, List<UpdatePhoneDTO> phoneUpdates);
    void deleteById(Long id, Userprincipal owner);
    Page<ContactInfoDTO> getPagedContacts(Userprincipal owner, Pageable pageable, String search);

}
