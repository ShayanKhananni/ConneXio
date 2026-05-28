package com.shayankhanani.Connexio.controller;

import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.DTO.Contact.Patch.UpdateContactDTO;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.repository.ContactRepo;
import com.shayankhanani.Connexio.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ContactRepo contactRepo;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<ContactInfoDTO>> getContactsInfo(@AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.getContactsInfo(owner));
    }



    @GetMapping("/paged")
    public ResponseEntity<Page<ContactInfoDTO>> getPagedContacts(
            @AuthenticationPrincipal Userprincipal owner,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "contactId") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) String search

            ) {
        Sort sort = null;

        if(sortDir.equalsIgnoreCase("ASC"))
        {
            sort = Sort.by(sortBy).ascending();
        }
        else
        {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return ResponseEntity.ok(
                contactService.getPagedContacts(owner, pageable, search)
        );
    }





    @GetMapping("/{id}")
    public ResponseEntity<ContactDetailDTO> getContactDetails(@PathVariable Long id, @AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.getContactDetails(owner,id));
    }


    @PostMapping()
    public ResponseEntity<ContactDetailDTO> addContact(@AuthenticationPrincipal Userprincipal owner,
                                                       @RequestBody @Valid AddedContactDTO addedContactDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.addContact(addedContactDTO,owner));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactDetailDTO> updateContactInfo(@PathVariable Long id,
                                                              @RequestBody @Valid UpdateContactDTO updateContactInfoDTO,
                                                              @AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.updateContactInfo(id,updateContactInfoDTO,owner));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactById(@PathVariable Long id, @AuthenticationPrincipal Userprincipal owner)
    {
        contactService.deleteById(id,owner);
        return ResponseEntity.noContent().build();
    }




}
