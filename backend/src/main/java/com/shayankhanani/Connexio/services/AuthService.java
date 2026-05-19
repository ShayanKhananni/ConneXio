package com.shayankhanani.Connexio.services;

import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.LoignRespDTO;

public interface AuthService {

    LoignRespDTO login(LoginDTO loginDTO);
}
