package com.shayankhanani.Connexio.service;

import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.LoignRespDTO;
import com.shayankhanani.Connexio.DTO.Auth.RegisterDTO;
import com.shayankhanani.Connexio.entity.User;
import com.shayankhanani.Connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.Connexio.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    public LoignRespDTO login(LoginDTO loginDTO)
    {
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));


        KeyGenerator keyGen = null;

        String token = jwtService.generateToken(loginDTO.getUsername());
        return new LoignRespDTO(token);
    }

    public void signup(RegisterDTO registerDTO)
    {
        if (userRepo.existsByEmailOrUsernameOrPhone(registerDTO.getEmail(),registerDTO.getUsername(),registerDTO.getPhone())) {
            throw new UserAlreadyExistException("User already exists");
        }
        User user = modelMapper.map(registerDTO, User.class);
        userRepo.save(user);
    }

}
