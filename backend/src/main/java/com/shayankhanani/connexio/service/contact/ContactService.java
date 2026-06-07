package com.shayankhanani.connexio.service.contact;


import com.shayankhanani.connexio.dto.contact.AddContactDTO;
import com.shayankhanani.connexio.dto.contact.ContactDetailDTO;
import com.shayankhanani.connexio.dto.contact.patch.UpdateContactDTO;
import com.shayankhanani.connexio.entity.Userprincipal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {

    Page<ContactDetailDTO> getPagedContacts(
            Userprincipal owner,
            int pageNum,
            int pageSize,
            String sortBy,
            String sortDir,
            String search
    );

    ContactDetailDTO getContactDetails(Userprincipal owner, Long id);
    ContactDetailDTO addContact(AddContactDTO addContactDTO, Userprincipal owner);
    ContactDetailDTO updateContactInfo(Long id, UpdateContactDTO updateContactInfoDTO, Userprincipal owner);
    void deleteById(Long id, Userprincipal owner);
    List<ContactDetailDTO> saveBatchContacts(Userprincipal owner, List<AddContactDTO> contacts);

}
