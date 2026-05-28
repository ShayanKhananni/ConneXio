package com.shayankhanani.Connexio.DTO.Contact;
import lombok.Data;

import java.util.List;

@Data
public class ContactDetailDTO {

    private Long contactId;
    private String firstName;
    private String lastName;

    // Social links (optional)
    private String linkedinUrl;
    private String instagramUrl;
    private String profImageUrl;
    private String facebookUrl;
    private Long linkedUserId;

    private List<EmailDTO> emails;
    private List<PhoneDTO> phones;

}
