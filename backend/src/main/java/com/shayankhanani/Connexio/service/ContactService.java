package com.shayankhanani.Connexio.service;


import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.DTO.Contact.Patch.UpdateContactDTO;
import com.shayankhanani.Connexio.DTO.Contact.Patch.UpdateEmailDTO;
import com.shayankhanani.Connexio.DTO.Contact.Patch.UpdatePhoneDTO;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Userprincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    ContactDetailDTO getContactDetails(Userprincipal owner, Long id);
    List<ContactInfoDTO> getContactsInfo(Userprincipal owner);
    ContactDetailDTO addContact(AddedContactDTO addedContactDTO, Userprincipal owner);
    ContactDetailDTO updateContactInfo(Long id, UpdateContactDTO updateContactInfoDTO, Userprincipal owner);
    void patchEmails(Contact contact, List<UpdateEmailDTO> updateEmailDTOS);
    void patchPhones(Contact contact, List<UpdatePhoneDTO> phoneUpdates);
    void deleteById(Long id, Userprincipal owner);
    Page<ContactInfoDTO> getPagedContacts(Userprincipal owner, Pageable pageable, String search);

}
