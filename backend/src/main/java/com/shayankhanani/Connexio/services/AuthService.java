package com.shayankhanani.Connexio.services;


import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.RegisterDTO;
import com.shayankhanani.Connexio.DTO.Auth.RespUserDTO;
import com.shayankhanani.Connexio.entity.User;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final AuthUtil authUtil;

    public RespUserDTO registerUser(RegisterDTO registerDTO) {

        User newUser = userRepo.save(modelMapper.map(registerDTO,User.class));
        return modelMapper.map(newUser, RespUserDTO.class);
    }




    public String verify(LoginDTO loginDTO)
    {
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));

        if(authentication.isAuthenticated())
        {
            return jwtService.generateToken(loginDTO.getUsername());
        }

        return "Unverified";
    }

    public Userprincipal getUser()
    {
        return authUtil.getUser();
    }

}
