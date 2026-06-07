package com.shayankhanani.connexio.service;


import com.shayankhanani.connexio.dto.contact.patch.UpdateEmailDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactEmail;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.repository.EmailRepo;
import com.shayankhanani.connexio.service.contact.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepo emailRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Userprincipal owner;
    @Mock
    private Contact contact;
    @Mock
    private User user;
    @InjectMocks
    private EmailServiceImpl emailService;


    @Test
    void patchEmails_shouldThrowException_whenDuplicateEmailsExist() {

        UpdateEmailDTO email1 = new UpdateEmailDTO();
        email1.setEmail("test@mail.com");

        UpdateEmailDTO email2 = new UpdateEmailDTO();
        email2.setEmail("test@mail.com");

        List<UpdateEmailDTO> updates = List.of(email1, email2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emailService.patchEmails(contact, updates)
        );

        assertEquals(
                "Duplicate emails found in request",
                exception.getMessage()
        );
    }

    @Test
    void patchEmails_shouldCallCreateEmails_whenOnlyNewEmailsExist() {

        UpdateEmailDTO dto1 = new UpdateEmailDTO();
        dto1.setEmail("a@test.com");

        UpdateEmailDTO dto2 = new UpdateEmailDTO();
        dto2.setEmail("b@test.com");

        List<UpdateEmailDTO> updates = List.of(dto1, dto2);

        EmailServiceImpl spy = spy(emailService);

        doNothing().when(spy)
                .createEmails(any(), anyList(), anyList());

        spy.patchEmails(contact, updates);

        verify(spy).createEmails(
                contact,
                List.of("a@test.com", "b@test.com"),
                updates
        );

        verify(spy, never())
                .updateEmails(any(), any(), any());
    }

    @Test
    void patchEmails_shouldCallUpdateEmails_whenOnlyExistingEmailsExist() {

        UpdateEmailDTO dto1 = new UpdateEmailDTO();
        dto1.setId(1L);
        dto1.setEmail("a@test.com");

        UpdateEmailDTO dto2 = new UpdateEmailDTO();
        dto2.setId(2L);
        dto2.setEmail("b@test.com");

        List<UpdateEmailDTO> updates = List.of(dto1, dto2);

        EmailServiceImpl spy = spy(emailService);

        doNothing().when(spy)
                .updateEmails(any(), anyList(), anyList());

        spy.patchEmails(contact, updates);

        verify(spy).updateEmails(
                contact,
                updates,
                List.of(1L, 2L)
        );

        verify(spy, never())
                .createEmails(any(), any(), any());
    }

    @Test
    void patchEmails_shouldCallCreateAndUpdateEmails_whenMixedDataProvided() {

        UpdateEmailDTO newEmail = new UpdateEmailDTO();
        newEmail.setEmail("new@test.com");

        UpdateEmailDTO existingEmail = new UpdateEmailDTO();
        existingEmail.setId(1L);
        existingEmail.setEmail("old@test.com");
        existingEmail.setLabel("HOME");

        List<UpdateEmailDTO> updates =
                List.of(newEmail, existingEmail);

        EmailServiceImpl spy = spy(emailService);

        doNothing().when(spy)
                .createEmails(any(), anyList(), anyList());

        doNothing().when(spy)
                .updateEmails(any(), anyList(), anyList());

        spy.patchEmails(contact, updates);

        verify(spy).createEmails(
                eq(contact),
                eq(List.of("new@test.com")),
                anyList()
        );

        verify(spy).updateEmails(
                eq(contact),
                anyList(),
                eq(List.of(1L))
        );
    }

    @Test
    void createEmails_shouldSaveEmails_whenNoDuplicatesExist() {


        UpdateEmailDTO dto1 = new UpdateEmailDTO();
        dto1.setEmail("a@test.com");
        dto1.setLabel("Personal");

        UpdateEmailDTO dto2 = new UpdateEmailDTO();
        dto2.setEmail("b@test.com");
        dto2.setLabel("Work");

        List<UpdateEmailDTO> emailsToAdd = List.of(dto1, dto2);
        List<String> emails = List.of("a@test.com", "b@test.com");

        when(emailRepo.findByContactAndEmailIn(contact, emails))
                .thenReturn(Collections.emptyList());

        emailService.createEmails(contact, emails, emailsToAdd);

        ArgumentCaptor<List<ContactEmail>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(emailRepo).saveAll(captor.capture());

        List<ContactEmail> savedEmails = captor.getValue();

        assertEquals(2, savedEmails.size());

        assertEquals(contact, savedEmails.get(0).getContact());
        assertEquals("a@test.com", savedEmails.get(0).getEmail());
        assertEquals("Personal", savedEmails.get(0).getLabel());

        assertEquals(contact, savedEmails.get(1).getContact());
        assertEquals("b@test.com", savedEmails.get(1).getEmail());
        assertEquals("Work", savedEmails.get(1).getLabel());
    }

    @Test
    void updateEmails_shouldUpdateExistingEmails() {


        ContactEmail email = new ContactEmail();
        email.setId(1L);
        email.setEmail("old@test.com");
        email.setLabel("Old");

        UpdateEmailDTO dto = new UpdateEmailDTO();
        dto.setId(1L);
        dto.setEmail("new@test.com");
        dto.setLabel("New");

        when(emailRepo.findAllByIdInAndContact(
                List.of(1L), contact))
                .thenReturn(List.of(email));

        emailService.updateEmails(
                contact,
                List.of(dto),
                List.of(1L)
        );

        assertEquals("new@test.com", email.getEmail());
        assertEquals("New", email.getLabel());

        verify(emailRepo).saveAll(List.of(email));
    }


    @Test
    void createEmails_shouldThrowDuplicateEmailException_whenEmailAlreadyExists() {

        List<String> emails = List.of("test@example.com");

        List<UpdateEmailDTO> emailstoAdd = List.of(
                new UpdateEmailDTO()
        );

        ContactEmail existing = new ContactEmail();
        existing.setEmail("test@example.com");

        when(emailRepo.findByContactAndEmailIn(contact, emails))
                .thenReturn(List.of(existing));

        // when + then
        assertThrows(DuplicateEmailException.class,
                () -> emailService.createEmails(contact, emails, emailstoAdd)
        );

        // verify save never happens
        verify(emailRepo, never()).saveAll(any());
    }

    @Test
    void updateEmails_shouldThrowResourceNotFoundException_whenEmailsNotFound() {

        List<Long> updateIds = List.of(1L, 2L);

        UpdateEmailDTO dto1 = new UpdateEmailDTO();
        dto1.setId(1L);
        dto1.setEmail("a@test.com");
        dto1.setLabel("work");

        UpdateEmailDTO dto2 = new UpdateEmailDTO();
        dto2.setId(2L);
        dto2.setEmail("b@test.com");
        dto2.setLabel("home");

        List<UpdateEmailDTO> updates = List.of(dto1, dto2);

        ContactEmail email = new ContactEmail();
        email.setId(1L);

        when(emailRepo.findAllByIdInAndContact(updateIds, contact))
                .thenReturn(List.of(email)); // only 1 returned

        // when + then
        assertThrows(ResourceNotFoundException.class,
                () -> emailService.updateEmails(contact, updates, updateIds)
        );

        verify(emailRepo, never()).saveAll(any());
    }


}
