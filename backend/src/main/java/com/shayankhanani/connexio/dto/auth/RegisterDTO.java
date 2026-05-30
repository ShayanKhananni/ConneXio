package com.shayankhanani.connexio.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")   // ← also add this
    private String email;
    @NotBlank
    private String phone;
}
