package com.shayankhanani.Connexio.DTO.Auth;


import lombok.Data;

@Data
public class RegisterDTO {

    private String username;
    private String password;
    private String email;
    private String phone;
}
