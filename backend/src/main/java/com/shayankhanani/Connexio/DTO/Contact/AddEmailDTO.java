package com.shayankhanani.Connexio.DTO.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddEmailDTO {
    @NotEmpty(message = "At least one email is required")
    private List<@NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format") String> emails;

}
