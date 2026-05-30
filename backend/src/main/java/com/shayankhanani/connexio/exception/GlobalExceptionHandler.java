package com.shayankhanani.connexio.exception;

import com.shayankhanani.connexio.dto.ErrorResponseDTO;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.exception.contact.DuplicateEmailException;
import com.shayankhanani.connexio.exception.contact.DuplicatePhoneException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    // Handles validation errors triggered by Bean Validation annotations
    // (e.g. @NotBlank, @Size, @Email) on request DTOs.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException e) {

        String firstError = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(firstError));
    }

    // Handles user registration conflicts when a user already exists
    // with the same email, username, or phone number.
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExist(UserAlreadyExistException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(e.getMessage()));
    }


    // Handles unauthorized access attempts to protected resources
    // when the user is authenticated but not permitted.
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(e.getMessage()));
    }



    // Handles business rule violations when a value already exists
    // and conflicts with existing database constraints.
    @ExceptionHandler(DuplicateValueException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateValues(DuplicateValueException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(e.getMessage()));
    }



    // Handles invalid business input that does not pass domain rules
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidValue(InvalidValueException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(e.getMessage()));
    }


    // Handles cases where a requested resource does not exist
    // in the database or cannot be found via the given identifier.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(e.getMessage()));
    }


    // Handles duplicate phone numbers detected in DTO validation
    // before performing any database operations.
    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatePhone(DuplicatePhoneException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(e.getMessage()));
    }

    // Handles duplicate email addresses detected in DTO validation
    // before performing any database operations.
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEmail(DuplicateEmailException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(e.getMessage()));
    }




    // Handles authentication failures triggered by Spring Security
    // (e.g. invalid username or password during login).
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO("Invalid Username or Password!!"));
    }


}
