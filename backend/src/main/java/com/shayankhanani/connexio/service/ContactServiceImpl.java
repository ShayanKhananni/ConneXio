package com.shayankhanani.connexio.service;
import com.shayankhanani.connexio.dto.contact.*;
import com.shayankhanani.connexio.dto.contact.patch.*;
import com.shayankhanani.connexio.dto.contact.patch.UpdatePhoneDTO;
import com.shayankhanani.connexio.entity.*;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.connexio.repository.ContactRepo;
import com.shayankhanani.connexio.repository.EmailRepo;
import com.shayankhanani.connexio.repository.PhoneRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ContactServiceImpl implements ContactService {

    private final ContactRepo contactRepo;
    private final ModelMapper modelMapper;
    private final EmailRepo emailRepo;
    private final PhoneRepo phoneRepo;
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);


    @Override
    public List<ContactInfoDTO> getContactsInfo(Userprincipal owner) {

        List<Contact> contacts = contactRepo.findContactByOwner(owner.getUser());

        if(contacts.isEmpty())
        {
            throw new ResourceNotFoundException("Contacts Does not exists!!");
        }

        return contacts.stream()
                .map(c -> modelMapper.map(c, ContactInfoDTO.class))
                .toList();
    }


    @Override
    public ContactDetailDTO getContactDetails(Userprincipal owner, Long id) {

        Contact contact = contactRepo.findByContactIdAndOwner(id, owner.getUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found!!"));

        return modelMapper.map(contact,ContactDetailDTO.class);
    }


    @Transactional
    @Override
    public ContactDetailDTO addContact(AddContactDTO dto, Userprincipal owner) {

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

        List<ContactPhone> contactPhoneEntities = dto.getPhones().stream()
                .map(p -> {
                    ContactPhone contactPhone = new ContactPhone();
                    contactPhone.setPhone(p.getPhone());
                    contactPhone.setLabel(p.getLabel());
                    contactPhone.setContact(contact);
                    return contactPhone;
                })
                .collect(Collectors.toList());

        List<ContactEmail> contactEmailEntities = dto.getEmails().stream()
                .map(e -> {
                    ContactEmail contactEmail = new ContactEmail();
                    contactEmail.setEmail(e.getEmail());
                    contactEmail.setLabel(e.getLabel());
                    contactEmail.setContact(contact);
                    return contactEmail;
                })
                .collect(Collectors.toList());


        contact.setContactPhones(contactPhoneEntities);
        contact.setContactEmails(contactEmailEntities);

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
            patchEmails(contact,dto.getEmailUpdates());
        }
        if (dto.getPhoneUpdates() != null) {
            patchPhones(contact, dto.getPhoneUpdates());
        }

        Contact updated = contactRepo.save(contact);

        return modelMapper.map(updated, ContactDetailDTO.class);

    }



    @Override
    public void patchEmails(Contact contact, List<UpdateEmailDTO> emailUpdates) {

        // 1. duplicate check in request
        Set<String> seen = new HashSet<>();
        boolean hasDuplicate = emailUpdates.stream()
                .map(UpdateEmailDTO::getEmail)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(email -> !seen.add(email));

        if (hasDuplicate) {
            throw new IllegalArgumentException("Duplicate emails found in request");
        }

        // 2. split create vs update
        List<UpdateEmailDTO> emailsToAdd = emailUpdates.stream()
                .filter(e -> e.getId() == null)
                .toList();

        List<UpdateEmailDTO> emailsToUpdate = emailUpdates.stream()
                .filter(e -> e.getId() != null)
                .toList();


        if (!emailsToAdd.isEmpty()) {
            createEmails(
                    contact,
                    emailsToAdd.stream().map(UpdateEmailDTO::getEmail).toList(),
                    emailsToAdd
            );
        }

        if (!emailsToUpdate.isEmpty()) {
            updateEmails(
                    contact,
                    emailsToUpdate,
                    emailsToUpdate.stream().map(UpdateEmailDTO::getId).toList()
            );
        }
    }

    private void createEmails(
            Contact contact,
            List<String> emails,
            List<UpdateEmailDTO> emailstoAdd
    ) {

        List<ContactEmail> existingContactEmails =
                emailRepo.findByContactAndEmailIn(contact, emails);

        if (!existingContactEmails.isEmpty()) {
            throw new DuplicateEmailException(
                    "Email Already Exists: " +
                            existingContactEmails.get(0).getEmail()
            );
        }

        List<ContactEmail> newContactEmails = emailstoAdd.stream()
                .map(dto -> {
                    ContactEmail contactEmail = new ContactEmail();
                    contactEmail.setContact(contact);
                    contactEmail.setEmail(dto.getEmail());
                    contactEmail.setLabel(dto.getLabel());
                    return contactEmail;
                })
                .toList();

        emailRepo.saveAll(newContactEmails);
    }


    private void updateEmails(
            Contact contact,
            List<UpdateEmailDTO> updates,
            List<Long> updateIds
    ) {

        List<ContactEmail> existingContactEmails =
                emailRepo.findAllByIdInAndContact(updateIds, contact);

        if (existingContactEmails.size() != updateIds.size()) {
            throw new ResourceNotFoundException("emails not found");
        }

        Map<Long, ContactEmail> emailMap = existingContactEmails.stream()
                .collect(Collectors.toMap(ContactEmail::getId, e -> e));

        for (UpdateEmailDTO dto : updates) {

            ContactEmail contactEmail = emailMap.get(dto.getId());

            if (contactEmail != null) {

                if (dto.getEmail() != null) {
                    contactEmail.setEmail(dto.getEmail());
                }

                if (dto.getLabel() != null) {
                    contactEmail.setLabel(dto.getLabel());
                }
            }
        }

        emailRepo.saveAll(existingContactEmails);
    }



    @Override
    public void patchPhones(Contact contact, List<UpdatePhoneDTO> phoneUpdates) {

        // 1. duplicate check in request
        Set<String> seen = new HashSet<>();
        boolean hasDuplicate = phoneUpdates.stream()
                .map(UpdatePhoneDTO::getPhone)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(phone -> !seen.add(phone));

        if (hasDuplicate) {
            throw new IllegalArgumentException("Duplicate phones found in request");
        }

        // 2. split create vs update
        List<UpdatePhoneDTO> phonesToAdd = phoneUpdates.stream()
                .filter(p -> p.getId() == null)
                .toList();

        List<UpdatePhoneDTO> phonesToUpdate = phoneUpdates.stream()
                .filter(p -> p.getId() != null)
                .toList();

        if (!phonesToAdd.isEmpty()) {
            createPhones(
                    contact,
                    phonesToAdd.stream().map(UpdatePhoneDTO::getPhone).toList(),
                    phonesToAdd
            );
        }

        if (!phonesToUpdate.isEmpty()) {
            updatePhones(
                    contact,
                    phonesToUpdate,
                    phonesToUpdate.stream().map(UpdatePhoneDTO::getId).toList()
            );
        }
    }

    private void createPhones(
            Contact contact,
            List<String> phones,
            List<UpdatePhoneDTO> phonesToAdd
    ) {

        List<ContactPhone> existingContactPhones =
                phoneRepo.findByContactAndPhoneIn(contact, phones);

        if (!existingContactPhones.isEmpty()) {
            throw new DuplicatePhoneException(
                    "Phone Already Exists: " +
                            existingContactPhones.get(0).getPhone()
            );
        }

        List<ContactPhone> newContactPhones = phonesToAdd.stream()
                .map(dto -> {
                    ContactPhone contactPhone = new ContactPhone();
                    contactPhone.setContact(contact);
                    contactPhone.setPhone(dto.getPhone());
                    contactPhone.setLabel(dto.getLabel());
                    return contactPhone;
                })
                .toList();

        phoneRepo.saveAll(newContactPhones);
    }

    private void updatePhones(
            Contact contact,
            List<UpdatePhoneDTO> updates,
            List<Long> updateIds
    ) {
        List<ContactPhone> existingContactPhones =
                phoneRepo.findAllByIdInAndContact(updateIds, contact);

        if (existingContactPhones.size() != updateIds.size()) {
            throw new ResourceNotFoundException("phone not found");
        }

        Map<Long, ContactPhone> phoneMap = existingContactPhones.stream()
                .collect(Collectors.toMap(ContactPhone::getId, p -> p));

        for (UpdatePhoneDTO dto : updates) {

            ContactPhone contactPhone = phoneMap.get(dto.getId());

            if (contactPhone != null) {

                if (dto.getPhone() != null) {
                    contactPhone.setPhone(dto.getPhone());
                }

                if (dto.getLabel() != null) {
                    contactPhone.setLabel(dto.getLabel());
                }
            }
        }

        phoneRepo.saveAll(existingContactPhones);
    }


    @Override
    public void deleteById(Long id, Userprincipal owner) {
       Contact contact = contactRepo.findByContactIdAndOwner(id,owner.getUser()).orElseThrow(()-> new
                ResourceNotFoundException("Contact not Found"));
       contactRepo.deleteById(contact.getContactId());
    }



    @Override
    public Page<ContactInfoDTO> getPagedContacts(Userprincipal owner, Pageable pageable, String search) {


        if(search == null)
        {
             return contactRepo.findContactByOwner(owner.getUser(), pageable).map(contact ->
                     modelMapper.map(contact, ContactInfoDTO.class)
             );
        }

        // searching By firstname

        return contactRepo.searchContacts(owner.getUser(), search, pageable).map(contact ->
                modelMapper.map(contact, ContactInfoDTO.class)
        );

    }




}
