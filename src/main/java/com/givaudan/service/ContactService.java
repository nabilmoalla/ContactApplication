package com.givaudan.service;

import com.givaudan.dto.ContactDto;

import java.util.List;

public interface ContactService {

    ContactDto createContact(ContactDto contactDto);

    ContactDto updateContact(Long id, ContactDto contactDto);

    List<ContactDto> findAll();

    ContactDto findContactById(Long id);

    void deleteContactById(Long id);
}
