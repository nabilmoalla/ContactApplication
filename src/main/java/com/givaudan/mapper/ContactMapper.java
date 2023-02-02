package com.givaudan.mapper;

import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import org.modelmapper.ModelMapper;

public class ContactMapper {

    private static final ModelMapper modelMapper = new ModelMapper();



    public static Contact convertToEntity(ContactDto contactDto){
        return modelMapper.map(contactDto, Contact.class);
    }

    public static ContactDto convertToDto(Contact contact){
        return modelMapper.map(contact, ContactDto.class);
    }
}
