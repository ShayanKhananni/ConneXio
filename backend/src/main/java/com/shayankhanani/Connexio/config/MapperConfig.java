package com.shayankhanani.Connexio.config;


import com.shayankhanani.Connexio.DTO.Contact.AddedContactDTO;
import com.shayankhanani.Connexio.entity.Contact;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(AddedContactDTO.class, Contact.class)
                .addMappings(mapper -> {
                    mapper.skip(Contact::setPhones);
                    mapper.skip(Contact::setEmails);
                });

        return modelMapper;
    }

}
