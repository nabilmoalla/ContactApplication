package com.givaudan.mapper;

import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

public class ContactMapper {

    private static final ModelMapper modelMapper = new ModelMapper();



    public static Contact convertToEntity(ContactDto contactDto){
        return modelMapper.map(contactDto, Contact.class);
    }

    public static ContactDto convertToDto(Contact contact){
        return modelMapper.map(contact, ContactDto.class);
    }

    public static List<ContactDto> convertToDtos(List<Contact> contacts){
        return contacts.stream().map(contact -> modelMapper.map(contact, ContactDto.class)).collect(Collectors.toList());
    }

    public static Contact mapToContact(Contact contact, ContactDto contactDto) {
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setBirthDate(contactDto.getBirthDate());
        contact.setAddress(contactDto.getAddress());
        return contact;
    }
}
