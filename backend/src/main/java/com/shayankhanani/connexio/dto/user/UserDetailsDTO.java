package com.shayankhanani.connexio.dto.user;


import lombok.Data;

@Data
public class UserDetailsDTO {

    private String phone;
    private String email;
    private String username;
    private String linkedinUrl;
    private String instagramUrl;
    private String profImageUrl;
    private String facebookUrl;

}