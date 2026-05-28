package com.shayankhanani.Connexio.service;

import com.shayankhanani.Connexio.entity.Contact;
import com.shayankhanani.Connexio.entity.User;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.exception.ResourceNotFoundException;
import com.shayankhanani.Connexio.repository.ContactRepo;
import com.shayankhanani.Connexio.repository.EmailRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ContactServiceTest {


    @Mock
    private ContactRepo contactRepo;
    @Mock
    private EmailRepo emailRepo;


    @InjectMocks
    private ContactServiceImpl contactService;


    @Test
    void shouldThrowExceptionWhenNoContactsFound() {

        // Arrange
        User user = new User();
        Userprincipal principal = new Userprincipal(user);

        when(contactRepo.findContactByOwner(user))
                .thenReturn(Collections.emptyList());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> contactService.getContactsInfo(principal));

        verify(contactRepo).findContactByOwner(user);
    }


    /// Tests For Contact Patch Service
//    @Test
//    void shouldCreateEmail()
//    {
//        Contact contact = new Contact();
//        UpdateEmailDTO dto = new UpdateEmailDTO();
//        dto.setId(null);
//        dto.setEmail("test@gmail.com");
//
//        List<UpdateEmailDTO> updates = List.of(dto);
//        contactService.updateEmails(contact,updates);
//
//        // Assert
//        verify(emailRepo).saveAll(anyList());
//        verify(emailRepo, never()).deleteByIdIn(anyList());
//
//    }





}
