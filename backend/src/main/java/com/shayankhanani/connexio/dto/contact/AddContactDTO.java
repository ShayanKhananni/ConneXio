package com.shayankhanani.connexio.dto.contact;

import com.shayankhanani.connexio.dto.contact.patch.AddEmailDTO;
import com.shayankhanani.connexio.dto.contact.patch.AddPhoneDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class AddContactDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @URL(message = "Invalid LinkedIn URL")
    private String linkedinUrl;

    @URL(message = "Invalid Instagram URL")
    private String instagramUrl;

    @URL(message = "Invalid profile image URL")
    private String profImageUrl;

    @URL(message = "Invalid Facebook URL")
    private String facebookUrl;

    @NotEmpty
    private List<AddEmailDTO> emails;
    @NotEmpty
    private List<AddPhoneDTO> phones;
}


