package com.shayankhanani.Connexio.DTO.User;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor

@Data
public class AddUserDTO {
    private String phone;
    private String email;
    private String username;
}
