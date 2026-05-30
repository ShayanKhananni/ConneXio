package com.shayankhanani.connexio.dto.user;


import lombok.Data;
@Data
public class UpdatePasswordDTO {
    private String oldPassword;
    private String newPassword;
}