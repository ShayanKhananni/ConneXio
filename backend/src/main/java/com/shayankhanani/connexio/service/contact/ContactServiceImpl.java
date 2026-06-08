package com.shayankhanani.connexio.service.contact;

import com.shayankhanani.connexio.dto.contact.AddContactDTO;
import com.shayankhanani.connexio.dto.contact.ContactDetailDTO;
import com.shayankhanani.connexio.dto.contact.patch.UpdateContactDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactEmail;
import com.shayankhanani.connexio.entity.ContactPhone;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.InvalidValueException;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.connexio.repository.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class ContactServiceImpl implements ContactService {


    private final ContactRepo contactRepo;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final PhoneService phoneService;


    private ContactDetailDTO toDTO(Contact contact) {
        return modelMapper.map(contact, ContactDetailDTO.class);
    }

    @Override
    public Page<ContactDetailDTO> getPagedContacts(
            Userprincipal owner,
            int pageNum,
            int pageSize,
            String sortBy,
            String sortDir,
            String search
    ) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        if (search == null || search.trim().isEmpty()) {
            return contactRepo.findContactByOwner(owner.getUser(), pageable)
                    .map(this::toDTO);
        }

        return contactRepo.searchContacts(owner.getUser(), search, pageable)
                .map(this::toDTO);
    }



    @Override
    public ContactDetailDTO getContactDetails(Userprincipal owner, Long id) {

        Contact contact = contactRepo.findByContactIdAndOwner(id, owner.getUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found!!"));

        return modelMapper.map(contact,ContactDetailDTO.class);
    }


    private Contact buildContact(AddContactDTO dto, Userprincipal owner) {

        if (dto.getEmails().size() > 2 || dto.getPhones().size() > 2) {
            throw new InvalidValueException(
                    "Only two emails and phones are allowed per contact");
        }

        Set<String> emailSet = dto.getEmails().stream()
                .map(e -> e.getEmail().trim().toLowerCase())
                .collect(Collectors.toSet());

        if (emailSet.size() != dto.getEmails().size()) {
            throw new DuplicateEmailException("Duplicate email found");
        }

        Set<String> phoneSet = dto.getPhones().stream()
                .map(p -> p.getPhone().trim())
                .collect(Collectors.toSet());

        if (phoneSet.size() != dto.getPhones().size()) {
            throw new DuplicatePhoneException("Duplicate phone number found");
        }

        Contact contact = modelMapper.map(dto, Contact.class);
        contact.setOwner(owner.getUser());

        List<ContactPhone> phones = dto.getPhones().stream()
                .map(p -> {
                    ContactPhone phone = new ContactPhone();
                    phone.setPhone(p.getPhone());
                    phone.setLabel(p.getLabel());
                    phone.setContact(contact);
                    return phone;
                })
                .toList();

        List<ContactEmail> emails = dto.getEmails().stream()
                .map(e -> {
                    ContactEmail email = new ContactEmail();
                    email.setEmail(e.getEmail());
                    email.setLabel(e.getLabel());
                    email.setContact(contact);
                    return email;
                })
                .toList();

        contact.setContactPhones(phones);
        contact.setContactEmails(emails);

        return contact;
    }


    @Transactional
    @Override
    public ContactDetailDTO addContact(
            AddContactDTO dto,
            Userprincipal owner) {

        Contact contact = buildContact(dto, owner);
        Contact saved = contactRepo.save(contact);
        return modelMapper.map(saved, ContactDetailDTO.class);
    }


    @Transactional
    @Override
    public ContactDetailDTO updateContactInfo(Long id,
                                              UpdateContactDTO dto,
                                              Userprincipal owner) {

        Contact contact = contactRepo.findByContactIdAndOwner(id, owner.getUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found!!"));

        if (dto.getFirstName() != null) {
            contact.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            contact.setLastName(dto.getLastName());
        }
        if (dto.getLinkedinUrl() != null) {
            contact.setLinkedinUrl(dto.getLinkedinUrl());
        }
        if (dto.getInstagramUrl() != null) {
            contact.setInstagramUrl(dto.getInstagramUrl());
        }
        if (dto.getProfImageUrl() != null) {
            contact.setProfImageUrl(dto.getProfImageUrl());
        }
        if (dto.getFacebookUrl() != null) {
            contact.setFacebookUrl(dto.getFacebookUrl());
        }
        if (dto.getEmailUpdates() != null) {
          emailService.patchEmails(contact,dto.getEmailUpdates());
        }
        if (dto.getPhoneUpdates() != null) {
           phoneService.patchPhones(contact, dto.getPhoneUpdates());
        }
        Contact updated = contactRepo.save(contact);

        return modelMapper.map(updated, ContactDetailDTO.class);

    }
    @Override
    public void deleteById(Long id, Userprincipal owner) {
       Contact contact = contactRepo.findByContactIdAndOwner(id,owner.getUser()).orElseThrow(()-> new
                ResourceNotFoundException("Contact not Found"));
       contactRepo.deleteById(contact.getContactId());
    }

    @Override
    @Transactional
    public List<ContactDetailDTO> saveBatchContacts(
            Userprincipal owner,
            List<AddContactDTO> dtos) {

        List<Contact> contacts = dtos.stream()
                .map(dto -> buildContact(dto, owner))
                .toList();

        List<Contact> savedContacts = contactRepo.saveAll(contacts);

        return savedContacts.stream()
                .map(contact -> modelMapper.map(contact, ContactDetailDTO.class))
                .toList();
    }





}
