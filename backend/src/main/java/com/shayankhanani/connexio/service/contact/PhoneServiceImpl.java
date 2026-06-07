package com.shayankhanani.connexio.service.contact;

import com.shayankhanani.connexio.dto.contact.patch.UpdatePhoneDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactPhone;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.connexio.repository.PhoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepo phoneRepo;


    @Override
    public void patchPhones(Contact contact, List<UpdatePhoneDTO> phoneUpdates) {

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


    @Override
    public void createPhones(
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

    public void updatePhones(
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


}
