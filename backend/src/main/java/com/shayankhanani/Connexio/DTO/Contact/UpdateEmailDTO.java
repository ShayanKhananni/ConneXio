package com.shayankhanani.Connexio.DTO.Contact;

import lombok.Data;

@Data
public class UpdateEmailDTO {
    private Long id;
    private String email;
    private Boolean delete;
}
