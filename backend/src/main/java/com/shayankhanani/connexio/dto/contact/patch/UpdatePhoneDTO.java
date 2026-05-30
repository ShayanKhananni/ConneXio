package com.shayankhanani.connexio.dto.contact.patch;

import lombok.Data;


@Data
public class UpdatePhoneDTO {
    private Long id;
    private String phone;
    private String label;
}
