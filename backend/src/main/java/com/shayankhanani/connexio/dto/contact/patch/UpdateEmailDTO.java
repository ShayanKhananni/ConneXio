package com.shayankhanani.connexio.dto.contact.patch;

import lombok.Data;

@Data
public class UpdateEmailDTO {
    private Long id;
    private String email;
    private String label;
}
