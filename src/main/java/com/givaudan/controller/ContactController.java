package com.givaudan.controller;

import com.givaudan.dto.ContactDto;
import com.givaudan.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@Slf4j
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestBody ContactDto contactDto){
        ContactDto createdContact = contactService.createContact(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }
}
