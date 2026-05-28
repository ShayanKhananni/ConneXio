package com.shayankhanani.Connexio.service;
import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.DTO.Contact.Patch.*;
import com.shayankhanani.Connexio.DTO.Contact.Patch.UpdatePhoneDTO;
import com.shayankhanani.Connexio.entity.*;
import com.shayankhanani.Connexio.exception.ResourceNotFoundException;
import com.shayankhanani.Connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.Connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.Connexio.repository.ContactRepo;
import com.shayankhanani.Connexio.repository.EmailRepo;
import com.shayankhanani.Connexio.repository.PhoneRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ContactDetailDTO addContact(AddedContactDTO dto, Userprincipal owner) {

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

        List<Phone> phoneEntities = dto.getPhones().stream()
                .map(p -> {
                    Phone phone = new Phone();
                    phone.setPhone(p.getPhone());
                    phone.setLabel(p.getLabel());
                    phone.setContact(contact);
                    return phone;
                })
                .collect(Collectors.toList());

        List<Email> emailEntities = dto.getEmails().stream()
                .map(e -> {
                    Email email = new Email();
                    email.setEmail(e.getEmail());
                    email.setLabel(e.getLabel());
                    email.setContact(contact);
                    return email;
                })
                .collect(Collectors.toList());


        contact.setPhones(phoneEntities);
        contact.setEmails(emailEntities);

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

        List<Email> existingEmails =
                emailRepo.findByContactAndEmailIn(contact, emails);

        if (!existingEmails.isEmpty()) {
            throw new DuplicateEmailException(
                    "Email Already Exists: " +
                            existingEmails.get(0).getEmail()
            );
        }

        List<Email> newEmails = emailstoAdd.stream()
                .map(dto -> {
                    Email email = new Email();
                    email.setContact(contact);
                    email.setEmail(dto.getEmail());
                    email.setLabel(dto.getLabel());
                    return email;
                })
                .toList();

        emailRepo.saveAll(newEmails);
    }


    private void updateEmails(
            Contact contact,
            List<UpdateEmailDTO> updates,
            List<Long> updateIds
    ) {

        List<Email> existingEmails =
                emailRepo.findAllByIdInAndContact(updateIds, contact);

        if (existingEmails.size() != updateIds.size()) {
            throw new ResourceNotFoundException("emails not found");
        }

        Map<Long, Email> emailMap = existingEmails.stream()
                .collect(Collectors.toMap(Email::getId, e -> e));

        for (UpdateEmailDTO dto : updates) {

            Email email = emailMap.get(dto.getId());

            if (email != null) {

                if (dto.getEmail() != null) {
                    email.setEmail(dto.getEmail());
                }

                if (dto.getLabel() != null) {
                    email.setLabel(dto.getLabel());
                }
            }
        }

        emailRepo.saveAll(existingEmails);
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

        List<Phone> existingPhones =
                phoneRepo.findByContactAndPhoneIn(contact, phones);

        if (!existingPhones.isEmpty()) {
            throw new DuplicatePhoneException(
                    "Phone Already Exists: " +
                            existingPhones.get(0).getPhone()
            );
        }

        List<Phone> newPhones = phonesToAdd.stream()
                .map(dto -> {
                    Phone phone = new Phone();
                    phone.setContact(contact);
                    phone.setPhone(dto.getPhone());
                    phone.setLabel(dto.getLabel());
                    return phone;
                })
                .toList();

        phoneRepo.saveAll(newPhones);
    }

    private void updatePhones(
            Contact contact,
            List<UpdatePhoneDTO> updates,
            List<Long> updateIds
    ) {
        List<Phone> existingPhones =
                phoneRepo.findAllByIdInAndContact(updateIds, contact);

        if (existingPhones.size() != updateIds.size()) {
            throw new ResourceNotFoundException("phone not found");
        }

        Map<Long, Phone> phoneMap = existingPhones.stream()
                .collect(Collectors.toMap(Phone::getId, p -> p));

        for (UpdatePhoneDTO dto : updates) {

            Phone phone = phoneMap.get(dto.getId());

            if (phone != null) {

                if (dto.getPhone() != null) {
                    phone.setPhone(dto.getPhone());
                }

                if (dto.getLabel() != null) {
                    phone.setLabel(dto.getLabel());
                }
            }
        }

        phoneRepo.saveAll(existingPhones);
    }


    @Override
    public void deleteById(Long id, Userprincipal owner) {
       Contact contact = contactRepo.findByContactIdAndOwner(id,owner.getUser()).orElseThrow(()-> new
                ResourceNotFoundException("Contact not Found"));
       contactRepo.deleteById(contact.getContactId());
    }



    @Override
    public Page<ContactInfoDTO> getPagedContacts(Userprincipal owner, Pageable pageable, String search) {

        Page<Contact> contacts;

        if(search == null)
        {
             return contactRepo.findContactByOwner(owner.getUser(), pageable).map(contact ->
                     modelMapper.map(contact, ContactInfoDTO.class)
             );
        }

        /// searching By firstname

        return contactRepo.searchContacts(owner.getUser(), search, pageable).map(contact ->
                modelMapper.map(contact, ContactInfoDTO.class)
        );

    }




}
