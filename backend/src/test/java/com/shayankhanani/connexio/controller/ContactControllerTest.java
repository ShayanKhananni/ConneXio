package com.shayankhanani.connexio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shayankhanani.connexio.dto.contact.AddContactDTO;
import com.shayankhanani.connexio.dto.contact.ContactDetailDTO;
import com.shayankhanani.connexio.dto.contact.patch.*;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.service.contact.ContactService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)

class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ContactService contactService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ModelMapper modelMapper;


    @Test
    void getPagedContacts_success() throws Exception {

        ContactDetailDTO dto = new ContactDetailDTO();
        dto.setContactId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");

        Page<ContactDetailDTO> page =
                new PageImpl<>(List.of(dto));

        when(contactService.getPagedContacts(
                any(),
                eq(1),
                eq(5),
                eq("contactId"),
                eq("DESC"),
                eq("john")
        )).thenReturn(page);

        mockMvc.perform(get("/contact/paged")
                        .param("pageNum", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "contactId")
                        .param("sortDir", "DESC")
                        .param("search", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].contactId").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"));
    }


    @Test
    void shouldReturnContactDetails() throws Exception {

        ContactDetailDTO dto = new ContactDetailDTO();
        dto.setContactId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(contactService.getContactDetails(any(), eq(1L)))
                .thenReturn(dto);

        mockMvc.perform(get("/contact/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldReturn404WhenContactNotFound() throws Exception {

        when(contactService.getContactDetails(any(), eq(999L)))
                .thenThrow(new ResourceNotFoundException("Contact not found"));

        mockMvc.perform(get("/contact/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addContact_shouldReturnCreated() throws Exception {

        AddEmailDTO email = new AddEmailDTO();
        email.setEmail("john@example.com");

        AddPhoneDTO phone = new AddPhoneDTO();
        phone.setPhone("1234567890");

        AddContactDTO request = new AddContactDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setLinkedinUrl("https://linkedin.com/in/johndoe");
        request.setInstagramUrl("https://instagram.com/johndoe");
        request.setFacebookUrl("https://facebook.com/johndoe");
        request.setProfImageUrl("https://example.com/profile.jpg");
        request.setEmails(List.of(email));
        request.setPhones(List.of(phone));

        ContactDetailDTO response = new ContactDetailDTO();
        response.setContactId(1L);
        response.setFirstName("John");
        response.setLastName("Doe");

        when(contactService.addContact(
                any(AddContactDTO.class),
                any()
        )).thenReturn(response);

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(contactService).addContact(
                any(AddContactDTO.class),
                any()
        );
    }

    @Test
    void addContact_shouldReturnBadRequest_whenFirstNameMissing() throws Exception {

        AddEmailDTO email = new AddEmailDTO();
        email.setEmail("john@example.com");

        AddPhoneDTO phone = new AddPhoneDTO();
        phone.setPhone("1234567890");

        AddContactDTO request = new AddContactDTO();
        request.setLastName("Doe");
        request.setEmails(List.of(email));
        request.setPhones(List.of(phone));

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(contactService, never())
                .addContact(any(), any());
    }

    @Test
    void updateContact_shouldReturnUpdatedContact() throws Exception {

        // -----------------------
        // Request DTO setup
        // -----------------------
        UpdateEmailDTO emailUpdate = new UpdateEmailDTO();
        emailUpdate.setId(1L);
        emailUpdate.setEmail("john.updated@example.com");
        emailUpdate.setLabel("work");

        UpdatePhoneDTO phoneUpdate = new UpdatePhoneDTO();
        phoneUpdate.setId(1L);
        phoneUpdate.setPhone("9876543210");
        phoneUpdate.setLabel("mobile");

        UpdateContactDTO request = new UpdateContactDTO();
        request.setFirstName("John");
        request.setLastName("Doe Updated");
        request.setLinkedinUrl("https://linkedin.com/in/johndoe");
        request.setInstagramUrl("https://instagram.com/johndoe");
        request.setFacebookUrl("https://facebook.com/johndoe");
        request.setProfImageUrl("https://example.com/profile.jpg");
        request.setEmailUpdates(List.of(emailUpdate));
        request.setPhoneUpdates(List.of(phoneUpdate));

        ContactDetailDTO response = new ContactDetailDTO();
        response.setContactId(1L);
        response.setFirstName("John");
        response.setLastName("Doe Updated");

        when(contactService.updateContactInfo(
                eq(1L),
                any(UpdateContactDTO.class),
                any()
        )).thenReturn(response);



        mockMvc.perform(patch("/contact/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"));


        verify(contactService).updateContactInfo(
                eq(1L),
                any(UpdateContactDTO.class),
                any()
        );
    }

    @Test
    void deleteContact_shouldReturnNoContent() throws Exception {

        // -----------------------
        // Mock service (void method)
        // -----------------------
        doNothing().when(contactService).deleteById(
                eq(1L),
                any()
        );

        // -----------------------
        // Perform request
        // -----------------------
        mockMvc.perform(delete("/contact/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        // -----------------------
        // Verify service call
        // -----------------------
        verify(contactService).deleteById(
                eq(1L),
                any()
        );
    }





}
