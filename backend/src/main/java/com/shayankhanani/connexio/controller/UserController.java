package com.shayankhanani.connexio.controller;

import com.shayankhanani.connexio.dto.user.UpdatePasswordDTO;
import com.shayankhanani.connexio.dto.user.UpdateUserDTO;
import com.shayankhanani.connexio.dto.user.UserDetailsDTO;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<UserDetailsDTO> me(
            @AuthenticationPrincipal Userprincipal user)
    {
        return ResponseEntity.ok(modelMapper.map(user.getUser(), UserDetailsDTO.class));
    }


    @PatchMapping()
    public ResponseEntity<UserDetailsDTO> updateUser(@AuthenticationPrincipal Userprincipal user, @RequestBody UpdateUserDTO
                                     dto)
    {
        return  ResponseEntity.ok(userService.updateUserInfo(user,dto));
    }


    @PutMapping("/password")
    public ResponseEntity<Map<String,String>> updatePassword(@AuthenticationPrincipal Userprincipal user, @RequestBody UpdatePasswordDTO dto)
    {
        userService.updatePassword(user,dto);
        return ResponseEntity.ok(
                Map.of("message", "Password updated successfully")
        );
    }



    }
