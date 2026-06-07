package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.contact.patch.UpdatePhoneDTO;
import com.shayankhanani.connexio.entity.Contact;
import com.shayankhanani.connexio.entity.ContactPhone;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import com.shayankhanani.connexio.repository.PhoneRepo;
import com.shayankhanani.connexio.service.contact.PhoneServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

    @Mock
    private PhoneRepo phoneRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Userprincipal owner;
    @Mock
    private Contact contact;
    @Mock
    private User user;
    @InjectMocks
    private PhoneServiceImpl phoneService;

    @Test
    void patchPhones_shouldThrowException_whenDuplicatePhonesExist() {

        UpdatePhoneDTO p1 = new UpdatePhoneDTO();
        p1.setPhone("123456");

        UpdatePhoneDTO p2 = new UpdatePhoneDTO();
        p2.setPhone("123456");

        List<UpdatePhoneDTO> updates = List.of(p1, p2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> phoneService.patchPhones(contact, updates)
        );

        assertEquals(
                "Duplicate phones found in request",
                exception.getMessage()
        );
    }

    @Test
    void patchPhones_shouldCallCreatePhones_whenOnlyNewPhonesExist() {


        UpdatePhoneDTO dto1 = new UpdatePhoneDTO();
        dto1.setPhone("123456");

        UpdatePhoneDTO dto2 = new UpdatePhoneDTO();
        dto2.setPhone("789012");

        List<UpdatePhoneDTO> updates = List.of(dto1, dto2);

        PhoneServiceImpl spy = spy(phoneService);

        doNothing().when(spy)
                .createPhones(any(), anyList(), anyList());

        spy.patchPhones(contact, updates);

        verify(spy).createPhones(
                contact,
                List.of("123456", "789012"),
                updates
        );

        verify(spy, never())
                .updatePhones(any(), any(), any());

    }

    @Test
    void patchPhones_shouldCallUpdatePhones_whenOnlyExistingPhonesExist() {

        UpdatePhoneDTO dto1 = new UpdatePhoneDTO();
        dto1.setId(1L);
        dto1.setPhone("123456");

        UpdatePhoneDTO dto2 = new UpdatePhoneDTO();
        dto2.setId(2L);
        dto2.setPhone("789012");

        List<UpdatePhoneDTO> updates = List.of(dto1, dto2);

        PhoneServiceImpl spy = spy(phoneService);

        doNothing().when(spy)
                .updatePhones(any(), anyList(), anyList());

        spy.patchPhones(contact, updates);

        verify(spy).updatePhones(
                contact,
                updates,
                List.of(1L, 2L)
        );

        verify(spy, never())
                .createPhones(any(), any(), any());
    }

    @Test
    void patchPhones_shouldCallCreateAndUpdatePhones_whenMixedDataProvided() {

        UpdatePhoneDTO newPhone = new UpdatePhoneDTO();
        newPhone.setPhone("123456");

        UpdatePhoneDTO existingPhone = new UpdatePhoneDTO();
        existingPhone.setId(1L);
        existingPhone.setPhone("789012");
        existingPhone.setLabel("HOME");

        List<UpdatePhoneDTO> updates =
                List.of(newPhone, existingPhone);

        PhoneServiceImpl spy = spy(phoneService);

        doNothing().when(spy)
                .createPhones(any(), anyList(), anyList());

        doNothing().when(spy)
                .updatePhones(any(), anyList(), anyList());

        spy.patchPhones(contact, updates);

        verify(spy).createPhones(
                eq(contact),
                eq(List.of("123456")),
                anyList()
        );

        verify(spy).updatePhones(
                eq(contact),
                anyList(),
                eq(List.of(1L))
        );
    }

    @Test
    void createPhones_shouldSavePhones_whenNoDuplicatesExist() {

        UpdatePhoneDTO dto1 = new UpdatePhoneDTO();
        dto1.setPhone("1111111111");
        dto1.setLabel("Home");

        UpdatePhoneDTO dto2 = new UpdatePhoneDTO();
        dto2.setPhone("2222222222");
        dto2.setLabel("Work");

        List<UpdatePhoneDTO> phonesToAdd = List.of(dto1, dto2);
        List<String> phones = List.of("1111111111", "2222222222");

        when(phoneRepo.findByContactAndPhoneIn(contact, phones))
                .thenReturn(Collections.emptyList());

        phoneService.createPhones(contact, phones, phonesToAdd);

        ArgumentCaptor<List<ContactPhone>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(phoneRepo).saveAll(captor.capture());

        List<ContactPhone> savedPhones = captor.getValue();

        assertEquals(2, savedPhones.size());

        assertEquals(contact, savedPhones.get(0).getContact());
        assertEquals("1111111111", savedPhones.get(0).getPhone());
        assertEquals("Home", savedPhones.get(0).getLabel());

        assertEquals(contact, savedPhones.get(1).getContact());
        assertEquals("2222222222", savedPhones.get(1).getPhone());
        assertEquals("Work", savedPhones.get(1).getLabel());
    }


    @Test
    void createPhones_shouldThrowException_whenPhoneAlreadyExists() {

        ContactPhone existingPhone = new ContactPhone();
        existingPhone.setPhone("1111111111");

        List<String> phones = List.of("1111111111");

        when(phoneRepo.findByContactAndPhoneIn(contact, phones))
                .thenReturn(List.of(existingPhone));

        List<UpdatePhoneDTO> emptyPhones = Collections.emptyList();

       DuplicatePhoneException exception = assertThrows(
                DuplicatePhoneException.class,
                () -> phoneService.createPhones(
                        contact,
                        phones,
                        emptyPhones
                )
        );

        assertEquals(
                "Phone Already Exists: 1111111111",
                exception.getMessage()
        );

        verify(phoneRepo, never()).saveAll(any());
    }


    @Test
    void updatePhones_shouldUpdateOnlyPhone() {

        ContactPhone existingPhone = new ContactPhone();
        existingPhone.setId(1L);
        existingPhone.setPhone("123456");
        existingPhone.setLabel("Home");

        UpdatePhoneDTO dto = new UpdatePhoneDTO();
        dto.setId(1L);
        dto.setPhone("999999");
        dto.setLabel(null);

        when(phoneRepo.findAllByIdInAndContact(
                List.of(1L),
                contact
        )).thenReturn(List.of(existingPhone));

        phoneService.updatePhones(
                contact,
                List.of(dto),
                List.of(1L)
        );

        assertEquals("999999", existingPhone.getPhone());
        assertEquals("Home", existingPhone.getLabel());

        verify(phoneRepo).saveAll(List.of(existingPhone));
    }

    @Test
    void updatePhones_shouldUpdateOnlyLabel() {


        ContactPhone existingPhone = new ContactPhone();
        existingPhone.setId(1L);
        existingPhone.setPhone("123456");
        existingPhone.setLabel("Home");

        UpdatePhoneDTO dto = new UpdatePhoneDTO();
        dto.setId(1L);
        dto.setPhone(null);
        dto.setLabel("Work");

        when(phoneRepo.findAllByIdInAndContact(
                List.of(1L),
                contact
        )).thenReturn(List.of(existingPhone));

        phoneService.updatePhones(
                contact,
                List.of(dto),
                List.of(1L)
        );

        assertEquals("123456", existingPhone.getPhone());
        assertEquals("Work", existingPhone.getLabel());

        verify(phoneRepo).saveAll(List.of(existingPhone));
    }
}

