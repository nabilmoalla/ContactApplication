package com.givaudan.service;

import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import com.givaudan.exceptions.EntityNotFoundException;
import com.givaudan.mapper.ContactMapper;
import com.givaudan.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService{

    private final ContactRepository contactRepository;


    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional
    public ContactDto createContact(ContactDto contactDto) {
        log.debug("Request to create a new contact {}", contactDto.toString());
        Contact contact = ContactMapper.convertToEntity(contactDto);
        Contact savedContact = contactRepository.save(contact);
        return ContactMapper.convertToDto(savedContact);
    }

    @Override
    @Transactional
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        log.debug("Request to update contact id: {} with the new informations : {}", id, contactDto.toString());
        Optional<Contact> optionalContact = contactRepository.findById(id);
        return optionalContact.map(contact ->
                ContactMapper.convertToDto(contactRepository.save(ContactMapper.mapToContact(contact, contactDto)))
        ).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<ContactDto> findAll() {
        log.debug("Request to find all contact");
        List<Contact> contacts = contactRepository.findAll();
        return ContactMapper.convertToDtos(contacts);
    }

    @Override
    public ContactDto findContactById(Long id) {
        log.debug("Request to find  contact by id :{}", id);
        Optional<Contact> optionalContact = contactRepository.findById(id);
        return optionalContact.map(ContactMapper::convertToDto).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteContactById(Long id) {
        log.debug("Request to delete contact by id :{}", id);
        contactRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        contactRepository.deleteById(id);
    }
}
