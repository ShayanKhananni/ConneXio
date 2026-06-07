package com.shayankhanani.connexio.service.auth;

import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoginRespDTO;
import com.shayankhanani.connexio.dto.auth.RegisterDTO;

public interface AuthService {

    LoginRespDTO login(LoginDTO loginDTO);
    void signup(RegisterDTO registerDTO);
}
