package com.shayankhanani.Connexio.DTO.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class AddedContactDTO {

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
    private List<@Email(message = "Invalid email format") String> emails;

    @NotEmpty
    private List<@NotBlank(message = "Phone cannot be blank") String> phones;
}


