package com.shayankhanani.connexio.service.user;

import com.shayankhanani.connexio.dto.user.UpdatePasswordDTO;
import com.shayankhanani.connexio.dto.user.UpdateUserDTO;
import com.shayankhanani.connexio.dto.user.UserDetailsDTO;
import com.shayankhanani.connexio.entity.Userprincipal;

public interface UserService {


   UserDetailsDTO updateUserInfo(Userprincipal owner, UpdateUserDTO update);
   void updatePassword(Userprincipal owner, UpdatePasswordDTO dto);

}
