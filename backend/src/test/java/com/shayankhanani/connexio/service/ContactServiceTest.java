package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.repository.ContactRepo;
import com.shayankhanani.connexio.repository.EmailRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)


class ContactServiceTest {

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







}
