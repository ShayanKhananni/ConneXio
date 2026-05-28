package com.shayankhanani.Connexio.DTO.Contact.Patch;


import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class UpdateContactDTO {

    private String firstName;
    private String lastName;

    @URL(message = "Invalid LinkedIn URL")
    private String linkedinUrl;

    @URL(message = "Invalid Instagram URL")
    private String instagramUrl;

    @URL(message = "Invalid profile URL")
    private String profImageUrl;

    @URL(message = "Invalid Facebook URL")
    private String facebookUrl;

    private List<UpdateEmailDTO> emailUpdates;
    private List<UpdatePhoneDTO> phoneUpdates;

}