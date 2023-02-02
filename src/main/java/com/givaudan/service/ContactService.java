package com.givaudan.service;

import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import com.givaudan.mapper.ContactMapper;
import com.givaudan.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ContactService {

    private final ContactRepository contactRepository;

    private final ModelMapper modelMapper;


    public ContactService(ContactRepository contactRepository, ModelMapper modelMapper) {
        this.contactRepository = contactRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ContactDto createContact(ContactDto contactDto){
        Contact contact = ContactMapper.convertToEntity(contactDto);
        Contact savedContact = contactRepository.save(contact);
        return ContactMapper.convertToDto(savedContact);
    }
}
