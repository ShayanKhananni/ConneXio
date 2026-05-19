package com.shayankhanani.Connexio.DTO.Contact;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Map;

@Data
public class UpdatePhoneDTO {
    private Long id;
    private String phone;
    private Boolean delete;
}
