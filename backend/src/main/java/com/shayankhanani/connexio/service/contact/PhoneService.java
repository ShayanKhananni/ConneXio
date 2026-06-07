package com.shayankhanani.connexio.service.contact;


import com.shayankhanani.connexio.dto.contact.patch.UpdatePhoneDTO;
import com.shayankhanani.connexio.entity.Contact;

import java.util.List;

public interface PhoneService {

    public void patchPhones(Contact contact, List<UpdatePhoneDTO> phoneUpdates);
    public void createPhones(
            Contact contact,
            List<String> phones,
            List<UpdatePhoneDTO> phonesToAdd
    );
    public void updatePhones(
            Contact contact,
            List<UpdatePhoneDTO> updates,
            List<Long> updateIds
    );

}
