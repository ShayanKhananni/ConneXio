package com.shayankhanani.Connexio.service;

import com.shayankhanani.Connexio.DTO.Auth.LoginDTO;
import com.shayankhanani.Connexio.DTO.Auth.LoignRespDTO;

public interface AuthService {

    LoignRespDTO login(LoginDTO loginDTO);
}
