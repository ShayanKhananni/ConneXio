package com.shayankhanani.Connexio.controller;


import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.LoignRespDTO;
import com.shayankhanani.Connexio.DTO.Auth.RegisterDTO;
import com.shayankhanani.Connexio.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthServiceImpl authService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(studentService.addStudent(addStudDTO));


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid RegisterDTO registerDTO)
    {
        registerDTO.setPassword(encoder.encode(registerDTO.getPassword()));
        authService.signup(registerDTO);
                return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account Created Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoignRespDTO> login(@RequestBody LoginDTO loginDTO) {

        LoignRespDTO response = authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }

}
