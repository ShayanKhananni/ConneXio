package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoignRespDTO;
import com.shayankhanani.connexio.dto.auth.RegisterDTO;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    public LoignRespDTO login(LoginDTO loginDTO)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        Userprincipal userPrincipal = (Userprincipal) authentication.getPrincipal();

        Long userId = userPrincipal.getUser().getUserId();

        String token = jwtService.generateToken(userId);

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
