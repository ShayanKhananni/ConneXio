package com.shayankhanani.Connexio.controller;

import com.shayankhanani.Connexio.DTO.Contact.AddedContactDTO;
import com.shayankhanani.Connexio.DTO.Contact.ContactDTO;
import com.shayankhanani.Connexio.DTO.Contact.UpdateContactInfoDTO;
import com.shayankhanani.Connexio.DTO.Contact.UserContactListDTO;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.services.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @GetMapping()
    public ResponseEntity<List<ContactDTO>> getContacts(@AuthenticationPrincipal Userprincipal owner)
    {
        return ResponseEntity.ok(contactService.getUserContacts(owner));
    }

    @PostMapping()
    public ResponseEntity<ContactDTO> addContact(@AuthenticationPrincipal Userprincipal owner,
                                                 @RequestBody @Valid AddedContactDTO addedContactDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.addContact(addedContactDTO,owner));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContactInfo(@PathVariable Long id,
                                                        @RequestBody @Valid UpdateContactInfoDTO updateContactInfoDTO,
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
