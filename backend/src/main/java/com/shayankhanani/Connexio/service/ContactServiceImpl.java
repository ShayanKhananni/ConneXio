package com.shayankhanani.Connexio.services;


import com.shayankhanani.Connexio.DTO.Contact.*;
import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.Email;
import com.shayankhanani.Connexio.entity.Phone;
import com.shayankhanani.Connexio.entity.Userprincipal;
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
    public ContactDetailDTO addContact(AddedContactDTO addedContactDTO, Userprincipal owner) {

        Set<String> seen = new HashSet<>(addedContactDTO.getPhones());

        if (seen.size() != addedContactDTO.getPhones().size()) {
            throw new DuplicatePhoneException("Duplicate phone number for contact");
        }

        seen = new HashSet<>(addedContactDTO.getEmails());

        if (seen.size() != addedContactDTO.getPhones().size()) {
            throw new DuplicateEmailException("Duplicate email for contact");
        }

        Contact newContact = modelMapper.map(addedContactDTO,Contact.class);
        newContact.setOwner(owner.getUser());


        List<Phone> phoneEntities = addedContactDTO.getPhones().stream()
                .map(phoneData -> {
                    Phone phone = new Phone();
                    phone.setPhone(phoneData);
                    phone.setContact(newContact);
                    return phone;
                }).toList();

        List<Email> emailEntities = addedContactDTO.getEmails().stream()
                .map(emailData -> {
                    Email email = new Email();
                    email.setEmail(emailData);
                    email.setContact(newContact);
                    return email;
                }).toList();

          newContact.setPhones(phoneEntities);
          newContact.setEmails(emailEntities);

            Contact savedContact = contactRepo.save(newContact);
            ContactDetailDTO contactDTO = new ContactDetailDTO();
            modelMapper.map(savedContact,contactDTO);
            contactDTO.setContactId(savedContact.getContactId());
            return contactDTO;

    }


    @Transactional
    @Override
    public ContactDetailDTO updateContactInfo(Long id,
                                              UpdateContactInfoDTO dto,
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


        if (dto.getEmailUpdates() != null && !dto.getEmailUpdates().isEmpty()) {
            updateEmails(contact, dto.getEmailUpdates());
        }

        if (dto.getPhoneUpdates() != null && !dto.getPhoneUpdates().isEmpty()) {
            updatePhones(contact, dto.getPhoneUpdates());
        }
        Contact updated = contactRepo.save(contact);

        return modelMapper.map(updated, ContactDetailDTO.class);

    }
    @Transactional
    @Override
    public void updatePhones(Contact contact,
                             List<UpdatePhoneDTO> updates) {

        List<Long> ids = updates.stream()
                .map(UpdatePhoneDTO::getId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Phone> existingPhones = phoneRepo
                .findAllByIdInAndContact(ids, contact)
                .stream()
                .collect(Collectors.toMap(Phone::getId, p -> p));


        List<Phone> toInsert = new ArrayList<>();
        List<Phone> toUpdate = new ArrayList<>();
        List<Phone> toDelete = new ArrayList<>();

        for (UpdatePhoneDTO dto : updates) {

            // CREATE
            if (dto.getId() == null) {

                Phone phone = new Phone();
                phone.setPhone(dto.getPhone());
                phone.setContact(contact);

                toInsert.add(phone);
                continue;
            }

            // validate ownership + existence
            Phone existing = existingPhones.get(dto.getId());

            if (existing == null) {
                throw new ResourceNotFoundException(
                        String.format("Phone with id not found: %d", dto.getId()));

            }


            // DELETE
            if (Boolean.TRUE.equals(dto.getDelete())) {
                toDelete.add(existing);
            }

            // UPDATE
            else {
                existing.setPhone(dto.getPhone());
                toUpdate.add(existing);
            }
        }
        if (!toInsert.isEmpty()) {
            phoneRepo.saveAll(toInsert);
        }

        if (!toUpdate.isEmpty()) {
            phoneRepo.saveAll(toUpdate);
        }

        if (!toDelete.isEmpty()) {

            phoneRepo.deleteByIdIn(
                    toDelete.stream()
                            .map(Phone::getId)
                            .toList());
        }
    }


    @Override
    public void deleteById(Long id, Userprincipal owner) {
       Contact contact = contactRepo.findByContactIdAndOwner(id,owner.getUser()).orElseThrow(()-> new
                ResourceNotFoundException("Contact not Found"));
       contactRepo.deleteById(contact.getContactId());
    }


    @Transactional
    @Override
    public void updateEmails(Contact contact,
                             List<UpdateEmailDTO> updates) {

        List<Long> ids = updates.stream()
                .map(UpdateEmailDTO::getId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Email> existingEmails = emailRepo
                .findAllByIdInAndContact(ids, contact)
                .stream()
                .collect(Collectors.toMap(Email::getId, e -> e));

        List<Email> toInsert = new ArrayList<>();
        List<Email> toUpdate = new ArrayList<>();
        List<Email> toDelete = new ArrayList<>();

        for (UpdateEmailDTO dto : updates) {

            // CREATE
            if (dto.getId() == null) {

                Email email = new Email();
                email.setEmail(dto.getEmail());
                email.setContact(contact);

                toInsert.add(email);
                continue;
            }

            // validate ownership + existence
            Email existing = existingEmails.get(dto.getId());

            if (existing == null) {
                throw new ResourceNotFoundException(
                        String.format("Email with id not found: %d", dto.getId()));
            }


            System.out.println(Boolean.TRUE.equals(dto.getDelete()));

            // DELETE
            if (Boolean.TRUE.equals(dto.getDelete())) {
                toDelete.add(existing);
            }

            // UPDATE
            else {
                existing.setEmail(dto.getEmail());
                toUpdate.add(existing);
            }
        }

        if (!toInsert.isEmpty()) {
            emailRepo.saveAll(toInsert);
        }

        if (!toUpdate.isEmpty()) {
            emailRepo.saveAll(toUpdate);
        }

        if (!toDelete.isEmpty()) {
            emailRepo.deleteByIdIn(
                    toDelete.stream()
                            .map(Email::getId)
                            .toList()
            );

        }
    }

}
