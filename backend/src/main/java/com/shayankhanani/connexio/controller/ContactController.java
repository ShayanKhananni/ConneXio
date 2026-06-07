package com.shayankhanani.connexio.controller;

import com.shayankhanani.connexio.dto.contact.AddContactDTO;
import com.shayankhanani.connexio.dto.contact.ContactDetailDTO;
import com.shayankhanani.connexio.dto.contact.patch.UpdateContactDTO;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.service.contact.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;


    @GetMapping("/paged")
    public ResponseEntity<Page<ContactDetailDTO>> getPagedContacts(
            @AuthenticationPrincipal Userprincipal owner,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "contactId") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) String search
            ) {
        return ResponseEntity.ok(
                contactService.getPagedContacts(owner,pageNum,pageSize,sortBy,sortDir,search)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDetailDTO> getContactDetails(@PathVariable Long id,
                                                              @AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.getContactDetails(owner,id));
    }


    @PostMapping()
    public ResponseEntity<ContactDetailDTO> addContact(@AuthenticationPrincipal Userprincipal owner,
                                                       @RequestBody @Valid AddContactDTO addContactDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.addContact(addContactDTO,owner));
    }


    @PostMapping("/batch")
    public ResponseEntity<List<ContactDetailDTO>> batchContact(@AuthenticationPrincipal Userprincipal owner,
                                                             @RequestBody  List<AddContactDTO> contacts )
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.saveBatchContacts(owner,contacts));
    }



    @PatchMapping("/{id}")
    public ResponseEntity<ContactDetailDTO> updateContactInfo(@PathVariable Long id,
                                                              @RequestBody @Valid UpdateContactDTO updateContactInfoDTO,
                                                              @AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.updateContactInfo(id,updateContactInfoDTO,owner));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactById(@PathVariable Long id,
                                                  @AuthenticationPrincipal Userprincipal owner)
    {
        contactService.deleteById(id,owner);
        return ResponseEntity.noContent().build();
    }


}
