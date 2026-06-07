package com.shayankhanani.connexio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoginRespDTO;
import com.shayankhanani.connexio.dto.auth.RegisterDTO;
import com.shayankhanani.connexio.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)

 class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthServiceImpl authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signup_shouldReturnCreated_whenRequestIsValid() throws Exception {

        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@mail.com");
        dto.setUsername("testuser");
        dto.setPhone("12345");
        dto.setPassword("rawPassword");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(authService).signup(any(RegisterDTO.class));

    }

    @Test
    void login_shouldReturnOk_andToken() throws Exception {

        LoginDTO dto = new LoginDTO();
        dto.setUsername("testuser");
        dto.setPassword("rawPassword");

        LoginRespDTO response = new LoginRespDTO("token");

        when(authService.login(any(LoginDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));

        verify(authService).login(any(LoginDTO.class));
    }


}
