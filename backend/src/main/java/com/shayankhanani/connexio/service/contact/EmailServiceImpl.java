package com.shayankhanani.connexio.service.contact;

import com.shayankhanani.connexio.dto.contact.patch.UpdateEmailDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactEmail;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.repository.EmailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepo emailRepo;

    @Override
    public void patchEmails(Contact contact, List<UpdateEmailDTO> emailUpdates) {

        Set<String> seen = new HashSet<>();

        boolean hasDuplicate = emailUpdates.stream()
                .map(UpdateEmailDTO::getEmail)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(email -> !seen.add(email));


        if (hasDuplicate) {
            throw new IllegalArgumentException("Duplicate emails found in request");
        }

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


    public void createEmails(
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



    public void updateEmails(
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



}
