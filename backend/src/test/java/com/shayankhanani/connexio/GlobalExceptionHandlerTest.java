package com.shayankhanani.connexio;

import com.shayankhanani.connexio.dto.ErrorResponseDTO;
import com.shayankhanani.connexio.exception.DuplicateValueException;
import com.shayankhanani.connexio.exception.GlobalExceptionHandler;
import com.shayankhanani.connexio.exception.InvalidValueException;
import com.shayankhanani.connexio.exception.UnauthorizedException;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserAlreadyExist_shouldReturn409() {

        UserAlreadyExistException ex =
                new UserAlreadyExistException("User already exists");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleUserAlreadyExist(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody().message());
    }


    @Test
    void handleUnauthorized_shouldReturn401() {

        UnauthorizedException ex =
                new UnauthorizedException("Not allowed");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleUnauthorized(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Not allowed", response.getBody().message());
    }

    @Test
    void handleDuplicateValue_shouldReturn409() {

        DuplicateValueException ex =
                new DuplicateValueException("Duplicate value");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleDuplicateValues(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate value", response.getBody().message());
    }


    @Test
    void handleInvalidValue_shouldReturn400() {

        InvalidValueException ex =
                new InvalidValueException("Invalid value");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleInvalidValue(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid value", response.getBody().message());
    }

    @Test
    void handleDuplicatePhone_shouldReturn409() {

        DuplicatePhoneException ex =
                new DuplicatePhoneException("Duplicate phone");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleDuplicatePhone(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate phone", response.getBody().message());
    }

    @Test
    void handleDuplicateEmail_shouldReturn409() {

        DuplicateEmailException ex =
                new DuplicateEmailException("Duplicate email");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleDuplicateEmail(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate email", response.getBody().message());
    }

    @Test
    void handleBadCredentials_shouldReturn401() {

        BadCredentialsException ex =
                new BadCredentialsException("anything");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleBadCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Username or Password!!", response.getBody().message());
    }



}
