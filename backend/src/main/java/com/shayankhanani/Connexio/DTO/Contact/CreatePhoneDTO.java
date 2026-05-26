package com.shayankhanani.Connexio.DTO.Contact;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddPhoneDTO {
    @NotEmpty(message = "At least one phone is required")
    private List<@NotBlank(message = "Phone cannot be blank") String> phones;
}
