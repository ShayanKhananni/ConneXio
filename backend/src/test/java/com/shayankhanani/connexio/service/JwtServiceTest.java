package com.shayankhanani.connexio.service;


import com.shayankhanani.connexio.service.auth.JWTService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JWTService jwtService;

    private String secret;

    @BeforeEach
    void setUp() {

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        secret = Base64.getEncoder().encodeToString(key.getEncoded());

        jwtService = new JWTService(secret);
    }

    @Test
    void generateToken_shouldCreateValidToken() {

        Long userId = 1L;

        String token = jwtService.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Long extracted = jwtService.extractUserId(token);

        assertEquals(userId, extracted);
    }

    @Test
    void extractUserId_shouldReturnCorrectUserId() {

        Long userId = 42L;

        String token = jwtService.generateToken(userId);

        Long result = jwtService.extractUserId(token);

        assertEquals(userId, result);
    }

    @Test
    void validateToken_shouldReturnTrue_whenValid() {

        Long userId = 10L;

        String token = jwtService.generateToken(userId);

        boolean result = jwtService.validateToken(token, userId);

        assertTrue(result);
    }


    @Test
    void validateToken_shouldReturnFalse_whenUserIdMismatch() {

        String token = jwtService.generateToken(1L);

        boolean result = jwtService.validateToken(token, 2L);

        assertFalse(result);
    }



}