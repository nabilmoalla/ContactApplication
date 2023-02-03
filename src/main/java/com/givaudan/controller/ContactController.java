package com.givaudan.controller;

import com.givaudan.dto.ContactDto;
import com.givaudan.service.ContactService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Slf4j
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @ApiOperation(value = "Create Contact")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created contact"),
            @ApiResponse(code = 400, message = "Contact couldn't be created")
    })
    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestBody @Valid ContactDto contactDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            log.error("There are field errors in contactDto");
            return ResponseEntity.badRequest().build();
        }
        ContactDto createdContact = contactService.createContact(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @ApiOperation(value = "Update Contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated contact"),
            @ApiResponse(code = 400, message = "Contact couldn't be updated"),
            @ApiResponse(code = 404, message = "Contact couldn't be found")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable("id") Long id,@RequestBody @Valid ContactDto contactDto, BindingResult bindingResult){
        log.debug("Request to update contact with id: {}",id);
        if (bindingResult.hasFieldErrors()) {
            log.error("There are field errors in contactDto");
            return ResponseEntity.badRequest().build();
        }
        ContactDto updatedContact = contactService.updateContact(id,contactDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedContact);
    }

    @ApiOperation(value = "find all Contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully find contacts")
    })
    @GetMapping
    public ResponseEntity<List<ContactDto>> findContacts(){
        List<ContactDto> contactDtos = contactService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(contactDtos);
    }

    @ApiOperation(value = "find Contact by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully find contact"),
            @ApiResponse(code = 404, message = "Contact couldn't be found")
    })
    @GetMapping(value="/{id}")
    public ResponseEntity<ContactDto> findContactById(@PathVariable("id") Long id){
        ContactDto contactDto = contactService.findContactById(id);
        return ResponseEntity.status(HttpStatus.OK).body(contactDto);
    }

    @ApiOperation(value = "delete Contact by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully delete contact"),
            @ApiResponse(code = 404, message = "Contact couldn't be found")
    })
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteContactById(@PathVariable("id") Long id){
        contactService.deleteContactById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
