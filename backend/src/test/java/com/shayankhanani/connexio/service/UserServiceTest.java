package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.user.UpdatePasswordDTO;
import com.shayankhanani.connexio.dto.user.UpdateUserDTO;
import com.shayankhanani.connexio.dto.user.UserDetailsDTO;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.InvalidValueException;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.repository.UserRepo;
import com.shayankhanani.connexio.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Userprincipal owner;
    @Mock
    private PasswordEncoder passwordEncoder;



    @Test
    void updateUserInfo_shouldUpdateAndReturnUserDetailsDTO() {

        when(owner.getUserId()).thenReturn(1L);

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setUsername("newUser");
        dto.setEmail("new@email.com");
        dto.setPhone("123456789");

        User user = new User();
        user.setUsername("oldUser");

        User savedUser = new User();
        savedUser.setUsername("newUser");

        UserDetailsDTO responseDTO = new UserDetailsDTO();
        responseDTO.setUsername("newUser");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        when(userRepo.existsByEmailOrUsernameOrPhone(
                "new@email.com",
                "newUser",
                "123456789"
        )).thenReturn(false);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(modelMapper.map(savedUser, UserDetailsDTO.class)).thenReturn(responseDTO);

        // when
        UserDetailsDTO result = userService.updateUserInfo(owner, dto);

        // then
        assertNotNull(result);
        assertEquals("newUser", result.getUsername());

        verify(userRepo).save(any(User.class));
    }

    @Test
    void updateUserInfo_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {

        // given
        when(owner.getUserId()).thenReturn(1L);

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setUsername("newUser");
        dto.setEmail("new@email.com");
        dto.setPhone("123456789");

        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserInfo(owner, dto));

        verify(userRepo).findById(1L);
        verify(userRepo, never()).save(any());
    }
    @Test
    void updateUserInfo_shouldThrowUserAlreadyExistException_whenUserExists() {

        when(owner.getUserId()).thenReturn(1L);

        User user = new User();

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPhone("123456789");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        when(userRepo.existsByEmailOrUsernameOrPhone(
                dto.getEmail(),
                dto.getUsername(),
                dto.getPhone()
        )).thenReturn(true);

        // when & then
        assertThrows(UserAlreadyExistException.class,
                () -> userService.updateUserInfo(owner, dto)
        );

        // verify save is never called
        verify(userRepo, never()).save(any(User.class));
    }


    @Test
    void updatePassword_shouldUpdatePassword_successfully() {

        UpdatePasswordDTO dto = new UpdatePasswordDTO();
        dto.setOldPassword("old123");
        dto.setNewPassword("new123");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(owner.getUser()).thenReturn(user);

        when(passwordEncoder.matches("old123", "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.matches("new123", "encodedOldPassword"))
                .thenReturn(false);

        when(passwordEncoder.encode("new123"))
                .thenReturn("encodedNewPassword");

        // when
        userService.updatePassword(owner, dto);

        // then
        assertEquals("encodedNewPassword", user.getPassword());

        verify(passwordEncoder).encode("new123");
        verify(userRepo).save(user);
    }

    @Test
    void updatePassword_shouldThrowException_whenOldPasswordIsIncorrect() {

        // given
        UpdatePasswordDTO dto = new UpdatePasswordDTO();
        dto.setOldPassword("wrongOld");
        dto.setNewPassword("new123");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(owner.getUser()).thenReturn(user);

        when(passwordEncoder.matches("wrongOld", "encodedOldPassword"))
                .thenReturn(false);

        // when + then
        assertThrows(InvalidValueException.class,
                () -> userService.updatePassword(owner, dto));

        verify(userRepo, never()).save(any());
    }

    @Test
    void updatePassword_shouldThrowException_whenNewPasswordIsSameAsOld() {

        // given
        UpdatePasswordDTO dto = new UpdatePasswordDTO();
        dto.setOldPassword("old123");
        dto.setNewPassword("same123");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(owner.getUser()).thenReturn(user);

        when(passwordEncoder.matches("old123", "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.matches("same123", "encodedOldPassword"))
                .thenReturn(true);

        // when + then
        assertThrows(InvalidValueException.class,
                () -> userService.updatePassword(owner, dto));

        verify(userRepo, never()).save(any());
    }


}