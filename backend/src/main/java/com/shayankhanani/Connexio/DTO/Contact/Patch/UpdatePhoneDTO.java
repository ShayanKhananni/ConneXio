package com.shayankhanani.Connexio.DTO.Contact.Patch;

import lombok.Data;


@Data
public class UpdatePhoneDTO {
    private Long id;
    private String phone;
    private String label;
}
