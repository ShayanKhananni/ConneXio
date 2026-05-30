package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.auth.LoginDTO;
import com.shayankhanani.connexio.dto.auth.LoignRespDTO;

public interface AuthService {

    LoignRespDTO login(LoginDTO loginDTO);
}
