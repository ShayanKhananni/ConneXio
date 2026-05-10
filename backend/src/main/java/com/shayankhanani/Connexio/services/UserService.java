package com.shayankhanani.Connexio.services;

import com.shayankhanani.Connexio.DTO.User.AddUserDTO;
import com.shayankhanani.Connexio.DTO.User.UserDTO;

public interface UserService {

    UserDTO addUser(AddUserDTO addUserDTO);

}
