package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.contact.AddContactDTO;
import com.shayankhanani.connexio.dto.contact.ContactDetailDTO;
import com.shayankhanani.connexio.dto.contact.patch.*;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.InvalidValueException;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.connexio.repository.ContactRepo;
import com.shayankhanani.connexio.service.contact.ContactServiceImpl;
import com.shayankhanani.connexio.service.contact.EmailService;
import com.shayankhanani.connexio.service.contact.PhoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepo contactRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Userprincipal owner;
    @Mock
    private User user;
    @Mock
    private EmailService emailService;
    @Mock
    private PhoneService phoneService;
    @InjectMocks
    private ContactServiceImpl contactService;


    @Test
    void getPagedContacts_success_withSearch() {

        when(owner.getUser()).thenReturn(user);

        Contact contact = new Contact();
        contact.setContactId(1L);
        contact.setFirstName("John");

        Page<Contact> page = new PageImpl<>(List.of(contact));

        when(contactRepo.searchContacts(eq(user), eq("john"), any(Pageable.class)))
                .thenReturn(page);

        Page<ContactDetailDTO> result = contactService.getPagedContacts(
                owner,
                1,
                10,
                "id",
                "ASC",
                "john"
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(contactRepo).searchContacts(eq(user), eq("john"), any(Pageable.class));
    }

    @Test
    void getContactDetails_success() {

        when(owner.getUser()).thenReturn(user);

        Contact contact = new Contact();
        contact.setContactId(1L);
        contact.setFirstName("John");

        ContactDetailDTO dto = new ContactDetailDTO();
        dto.setContactId(1L);
        dto.setFirstName("John");

        when(contactRepo.findByContactIdAndOwner(1L, user))
                .thenReturn(Optional.of(contact));

        when(modelMapper.map(contact, ContactDetailDTO.class))
                .thenReturn(dto);

        ContactDetailDTO result =
                contactService.getContactDetails(owner, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getContactId());
        assertEquals("John", result.getFirstName());

        verify(contactRepo).findByContactIdAndOwner(1L, user);
        verify(modelMapper).map(contact, ContactDetailDTO.class);
    }


    // Test Cases for addContact service
    // Validating more than 2 values of emails must no allowed per contact
    @Test
    void shouldThrowException_whenMoreThanTwoEmailsProvided() {

        AddContactDTO dto = new AddContactDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");

        AddEmailDTO e1 = new AddEmailDTO();
        e1.setEmail("a@gmail.com");

        AddEmailDTO e2 = new AddEmailDTO();
        e2.setEmail("b@gmail.com");

        AddEmailDTO e3 = new AddEmailDTO();
        e3.setEmail("c@gmail.com");

        dto.setEmails(List.of(e1, e2, e3));

        AddPhoneDTO p1 = new AddPhoneDTO();
        p1.setPhone("12345");

        dto.setPhones(List.of(p1));

        assertThrows(InvalidValueException.class,
                () -> contactService.addContact(dto, owner));
    }

    // Validating more than 2 values of phones must no allowed per contact
    @Test
    void shouldThrowException_whenMoreThanTwoPhonesProvided() {

        AddContactDTO dto = new AddContactDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");

        AddEmailDTO e1 = new AddEmailDTO();
        e1.setEmail("test@gmail.com");

        dto.setEmails(List.of(e1));

        AddPhoneDTO p1 = new AddPhoneDTO();
        p1.setPhone("11111");

        AddPhoneDTO p2 = new AddPhoneDTO();
        p2.setPhone("22222");

        AddPhoneDTO p3 = new AddPhoneDTO();
        p3.setPhone("33333"); // 3rd phone (invalid)

        dto.setPhones(List.of(p1, p2, p3));

        assertThrows(InvalidValueException.class,
                () -> contactService.addContact(dto, owner));
    }

    @Test
    void shouldThrowException_whenDuplicateEmailsProvided() {

        AddContactDTO dto = new AddContactDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");

        AddEmailDTO e1 = new AddEmailDTO();
        e1.setEmail("test@gmail.com");

        AddEmailDTO e2 = new AddEmailDTO();
        e2.setEmail("test@gmail.com"); // duplicate

        dto.setEmails(List.of(e1, e2));

        AddPhoneDTO p1 = new AddPhoneDTO();
        p1.setPhone("12345");

        dto.setPhones(List.of(p1));

        assertThrows(DuplicateEmailException.class,
                () -> contactService.addContact(dto, owner));
    }

    @Test
    void shouldThrowException_whenDuplicatePhonesProvided() {

        AddContactDTO dto = new AddContactDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");

        AddEmailDTO e1 = new AddEmailDTO();
        e1.setEmail("test@gmail.com");

        dto.setEmails(List.of(e1));

        AddPhoneDTO p1 = new AddPhoneDTO();
        p1.setPhone("12345");

        AddPhoneDTO p2 = new AddPhoneDTO();
        p2.setPhone("12345"); // duplicate

        dto.setPhones(List.of(p1, p2));

        assertThrows(DuplicatePhoneException.class,
                () -> contactService.addContact(dto, owner));
    }

    // Success Test Case
    @Test
    void shouldSaveContact_whenInputIsValid() {

        AddContactDTO dto = new AddContactDTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");

        AddEmailDTO e1 = new AddEmailDTO();
        e1.setEmail("test@gmail.com");
        AddEmailDTO e2 = new AddEmailDTO();
        e2.setEmail("test2@gmail.com");
        dto.setEmails(List.of(e1,e2));


        AddPhoneDTO p1 = new AddPhoneDTO();
        p1.setPhone("12345");
        AddPhoneDTO p2 = new AddPhoneDTO();
        p2.setPhone("12346");
        dto.setPhones(List.of(p1,p2));

        Contact contact = new Contact();
        Contact savedContact = new Contact();

        ContactDetailDTO response = new ContactDetailDTO();
        when(owner.getUser()).thenReturn(user);
        when(modelMapper.map(dto, Contact.class)).thenReturn(contact);
        when(contactRepo.save(any(Contact.class))).thenReturn(savedContact);
        when(modelMapper.map(savedContact, ContactDetailDTO.class)).thenReturn(response);


        ContactDetailDTO result = contactService.addContact(dto, owner);

        assertNotNull(result);
        verify(contactRepo, times(1)).save(any(Contact.class));
    }


    // Update Contact Test Cases
    @Test
    void updateContactInfo_shouldCallDependencies_andReturnMappedDTO() {

        Long contactId = 1L;

        when(owner.getUser()).thenReturn(user);

        Contact contact = new Contact();
        when(contactRepo.findByContactIdAndOwner(contactId, user))
                .thenReturn(Optional.of(contact));

        UpdateContactDTO dto = new UpdateContactDTO();
        dto.setFirstName("New");
        dto.setLastName("Name");

        List<UpdateEmailDTO> emailUpdates = new ArrayList<>();
        dto.setEmailUpdates(emailUpdates);

        List<UpdatePhoneDTO> phoneUpdates = new ArrayList<>();
        dto.setPhoneUpdates(phoneUpdates);

        when(contactRepo.save(any(Contact.class))).thenReturn(contact);
        when(modelMapper.map(contact, ContactDetailDTO.class))
                .thenReturn(new ContactDetailDTO());

        contactService.updateContactInfo(contactId, dto, owner);

        verify(contactRepo).findByContactIdAndOwner(contactId, user);

        verify(emailService).patchEmails(contact, emailUpdates);
        verify(phoneService).patchPhones(contact, phoneUpdates);

        verify(contactRepo).save(contact);
        verify(modelMapper).map(contact, ContactDetailDTO.class);
    }

    @Test
    void updateContactInfo_shouldThrowException_whenContactNotFound() {

        Long contactId = 1L;
        when(owner.getUser()).thenReturn(user);

        when(contactRepo.findByContactIdAndOwner(contactId, user))
                .thenReturn(Optional.empty());


        UpdateContactDTO dto = new UpdateContactDTO();

        assertThrows(
                ResourceNotFoundException.class,
                () -> contactService.updateContactInfo(contactId, dto, owner)
        );

        verify(contactRepo).findByContactIdAndOwner(contactId, user);
        verifyNoInteractions(emailService);
        verifyNoInteractions(phoneService);
        verify(contactRepo, never()).save(any());
    }


    // Delete Contact Test Cases
    @Test
    void deleteById_shouldDeleteContactSuccessfully() {

        Contact contact = new Contact();
        contact.setContactId(10L);

        when(owner.getUser()).thenReturn(user);

        when(contactRepo.findByContactIdAndOwner(10L, user))
                .thenReturn(Optional.of(contact));

        contactService.deleteById(10L, owner);

        verify(contactRepo).deleteById(10L);
    }


    @Test
    void saveBatchContacts_ShouldSaveAllContactsAndReturnDtos() {


        AddEmailDTO email = new AddEmailDTO();
        email.setEmail("john.doe@example.com");
        email.setLabel("WORK");

        AddPhoneDTO phone = new AddPhoneDTO();
        phone.setPhone("923001234567");
        phone.setLabel("WORK");

        AddContactDTO dto1 = new AddContactDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmails(List.of(email));
        dto1.setPhones(List.of(phone));

        AddContactDTO dto2 = new AddContactDTO();
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setEmails(List.of(email));
        dto2.setPhones(List.of(phone));

        List<AddContactDTO> dtos = List.of(dto1, dto2);

        Contact contact1 = new Contact();
        Contact contact2 = new Contact();

        ContactDetailDTO detail1 = new ContactDetailDTO();
        ContactDetailDTO detail2 = new ContactDetailDTO();

        when(modelMapper.map(any(AddContactDTO.class), eq(Contact.class)))
                .thenReturn(new Contact());

        when(contactRepo.saveAll(anyList()))
                .thenReturn(List.of(contact1, contact2));

        when(modelMapper.map(contact1, ContactDetailDTO.class))
                .thenReturn(detail1);

        when(modelMapper.map(contact2, ContactDetailDTO.class))
                .thenReturn(detail2);

        List<ContactDetailDTO> result =
                contactService.saveBatchContacts(owner, dtos);

        assertEquals(2, result.size());
        assertEquals(detail1, result.get(0));
        assertEquals(detail2, result.get(1));

        verify(contactRepo).saveAll(anyList());
    }


}


