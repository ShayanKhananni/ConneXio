package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoginRespDTO;
import com.shayankhanani.connexio.dto.auth.RegisterDTO;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.repository.UserRepo;
import com.shayankhanani.connexio.service.auth.AuthServiceImpl;
import com.shayankhanani.connexio.service.auth.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JWTService jwtService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private Userprincipal userPrincipal;
    @Mock
    private User user;
    @Mock
    private UserRepo userRepo;


    //  Success Test Case For Login
    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {

        // given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password");

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userPrincipal);

        when(userPrincipal.getUser())
                .thenReturn(user);

        when(user.getUserId())
                .thenReturn(1L);

        when(jwtService.generateToken(1L))
                .thenReturn("mock-jwt-token");

        // when
        LoginRespDTO response = authService.login(loginDTO);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
    }
    //  Success Test Case For Signup

    @Test
    void signup_shouldSaveUser_whenUserDoesNotExist() {

        // given
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@mail.com");
        dto.setUsername("testuser");
        dto.setPhone("12345");
        dto.setPassword("rawPassword");

        User mappedUser = new User();
        mappedUser.setEmail(dto.getEmail());
        mappedUser.setUsername(dto.getUsername());
        mappedUser.setPhone(dto.getPhone());
        mappedUser.setPassword(dto.getPassword());

        when(userRepo.existsByEmailOrUsernameOrPhone(
                dto.getEmail(), dto.getUsername(), dto.getPhone()
        )).thenReturn(false);

        when(modelMapper.map(dto, User.class)).thenReturn(mappedUser);

        when(encoder.encode("rawPassword")).thenReturn("encodedPassword");

        authService.signup(dto);

        verify(userRepo).existsByEmailOrUsernameOrPhone(
                dto.getEmail(), dto.getUsername(), dto.getPhone()
        );

        verify(encoder).encode("rawPassword");

        verify(userRepo).save(argThat(savedUSer ->
                "test@mail.com".equals(savedUSer.getEmail()) &&
                        "testuser".equals(savedUSer.getUsername()) &&
                        "12345".equals(savedUSer.getPhone()) &&
                        "encodedPassword".equals(savedUSer.getPassword())
        ));
    }

    // Fail Case For signup when user already exist
    @Test
    void signup_shouldThrowException_whenUserAlreadyExists() {

        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@mail.com");
        dto.setUsername("testuser");
        dto.setPhone("12345");
        dto.setPassword("rawPassword");

        when(userRepo.existsByEmailOrUsernameOrPhone(
                dto.getEmail(), dto.getUsername(), dto.getPhone()
        )).thenReturn(true);

        // when + then
        assertThrows(UserAlreadyExistException.class,
                () -> authService.signup(dto));

        // verify flow stopped immediately
        verify(userRepo).existsByEmailOrUsernameOrPhone(
                dto.getEmail(), dto.getUsername(), dto.getPhone()
        );

        verify(userRepo, never()).save(any());
        verifyNoInteractions(modelMapper, encoder);
    }

}
