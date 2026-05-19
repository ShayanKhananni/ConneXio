package com.shayankhanani.Connexio.DTO.Contact;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

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

    private List<UpdateEmailDTO> emailUpdates;
    private List<UpdatePhoneDTO> phoneUpdates;

}