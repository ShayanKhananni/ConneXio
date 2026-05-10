package com.shayankhanani.Connexio.controller;


import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.RegisterDTO;
import com.shayankhanani.Connexio.DTO.Auth.RespUserDTO;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private com.shayankhanani.Connexio.services.AuthService authService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepo userRepo;
    private ModelMapper modelMapper;


    @GetMapping("/user")
    public Userprincipal home()
    {
        return authService.getUser();
    }


    @PostMapping("/signup")
    public RespUserDTO signup(@RequestBody RegisterDTO registerDTO)
    {
        registerDTO.setPassword(encoder.encode(registerDTO.getPassword()));
        return authService.registerUser(registerDTO);
    }

    @PostMapping("/login")
    public String signup(@RequestBody LoginDTO loginDTO)
    {
        return authService.verify(loginDTO);
    }


}
