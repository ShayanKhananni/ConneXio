package com.shayankhanani.connexio.dto.contact;


import lombok.Data;

@Data
public class ContactInfoDTO {

    private Long contactId;
    private String firstName;
    private String lastName;

    private String linkedinUrl;
    private String instagramUrl;
    private String profImageUrl;
    private String facebookUrl;
    private Long linkedUserId;
}
