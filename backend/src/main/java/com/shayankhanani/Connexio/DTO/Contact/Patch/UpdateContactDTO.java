package com.shayankhanani.Connexio.DTO.Contact;


import com.shayankhanani.Connexio.DTO.Contact.Patch.BatchEmailReqDTO;
import com.shayankhanani.Connexio.DTO.Contact.Patch.BatchPhoneReqDTO;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateContactInfoDTO {

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

    private BatchEmailReqDTO emailUpdates;
    private BatchPhoneReqDTO phoneUpdates;

}