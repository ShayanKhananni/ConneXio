package com.shayankhanani.Connexio.service;

import com.shayankhanani.Connexio.DTO.User.AddUserDTO;
import com.shayankhanani.Connexio.DTO.User.UserDTO;

public interface UserService {


    UserDTO addUser(AddUserDTO addUserDTO);
}
