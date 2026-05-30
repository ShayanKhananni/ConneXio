package com.shayankhanani.connexio.dto.contact;

import lombok.Data;

import java.util.List;

@Data
public class UserContactListDTO {
    List<ContactDetailDTO> contacts;
}
