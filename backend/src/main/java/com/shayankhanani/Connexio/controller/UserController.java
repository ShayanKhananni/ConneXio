package com.shayankhanani.Connexio.controller;

import com.shayankhanani.Connexio.DTO.User.AddUserDTO;
import com.shayankhanani.Connexio.DTO.User.UserDTO;
import com.shayankhanani.Connexio.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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






}
