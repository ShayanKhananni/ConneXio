package com.shayankhanani.connexio.service.contact;

import com.shayankhanani.connexio.dto.contact.patch.UpdateEmailDTO;
import com.shayankhanani.connexio.entity.Contact;

import java.util.List;

public interface EmailService {


    public void patchEmails(Contact contact, List<UpdateEmailDTO> emailUpdates);
    public void createEmails(
            Contact contact,
            List<String> emails,
            List<UpdateEmailDTO> emailstoAdd
    );

    public void updateEmails(
            Contact contact,
            List<UpdateEmailDTO> updates,
            List<Long> updateIds
    );
}
