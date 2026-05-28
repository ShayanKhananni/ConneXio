package com.shayankhanani.Connexio.config;


import com.shayankhanani.Connexio.DTO.Contact.ContactDetailDTO;
import com.shayankhanani.Connexio.entity.Contact;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Contact.class, ContactDetailDTO.class)
                .addMappings(mapper ->
                        mapper.map(src -> src.getLinkedUser().getUserId(),
                                ContactDetailDTO::setLinkedUserId)
                );

        return modelMapper;
    }

}
