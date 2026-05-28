package com.shayankhanani.Connexio.controller;

import com.shayankhanani.Connexio.DTO.User.AddUserDTO;
import com.shayankhanani.Connexio.DTO.User.UserDTO;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody AddUserDTO addUserDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(addUserDto));
    }

    @GetMapping("/me")
    public String me(
            @AuthenticationPrincipal Userprincipal user)
    {
        return user.getUsername();
    }


    }
