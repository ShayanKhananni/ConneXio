package com.shayankhanani.connexio.controller;

import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoginRespDTO;
import com.shayankhanani.connexio.dto.auth.RegisterDTO;
import com.shayankhanani.connexio.service.auth.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid RegisterDTO registerDTO)
    {
        authService.signup(registerDTO);
                return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account Created Successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginRespDTO> login(@RequestBody @Valid LoginDTO loginDTO) {

        LoginRespDTO response = authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }


}
