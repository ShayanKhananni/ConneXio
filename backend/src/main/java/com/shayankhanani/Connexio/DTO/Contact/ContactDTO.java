package com.shayankhanani.Connexio.DTO.Contact;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ContactDTO {

    private Long contactId;
    private String firstName;
    private String lastName;

    // Social links (optional)
    private String linkedinUrl;
    private String instagramUrl;
    private String profImageUrl;
    private String facebookUrl;

    private List<EmailDTO> emails;
    private List<PhoneDTO> phones;

}
