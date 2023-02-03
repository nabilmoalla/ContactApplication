package com.givaudan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import com.givaudan.exceptions.EntityNotFoundException;
import com.givaudan.service.ContactService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_create_contact() throws Exception {
        ContactDto contactDto = getContactDto("nabil");

        given(contactService.createContact(any(ContactDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactDto)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(contactDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(contactDto.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(contactDto.getBirthDate().toString()));
    }

    @Test
    public void should_update_contact_when_given_updated_contact() throws Exception{
        long contactId = 1L;
        ContactDto updatedContact = getContactDto("julien");
        given(contactService.updateContact(any(Long.class),any(ContactDto.class)))
                .willAnswer((invocation)-> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(put("/api/contacts/{id}", contactId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedContact.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedContact.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(updatedContact.getBirthDate().toString()));
    }

    @Test
    public void should_throw_exception_when_update_not_found_contact() throws Exception{
        long contactId = 1L;
        ContactDto updatedContact = getContactDto("julien");
        given(contactService.updateContact(any(Long.class),any(ContactDto.class)))
                .willThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(put("/api/contacts/{id}", contactId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_all_contacts() throws Exception{

        List<ContactDto> contactDtos  = new ArrayList<>();
        contactDtos.add(ContactDto.builder()
                .id(1L)
                .firstName("nabil")
                .lastName("moalla")
                .birthDate(LocalDate.of(2012, 01, 01))
                .build());
        contactDtos.add(ContactDto.builder()
                .id(1L)
                .firstName("julien")
                .lastName("moalla")
                .birthDate(LocalDate.of(2013, 01, 01))
                .build());
        given(contactService.findAll()).willReturn(contactDtos);

        ResultActions response = mockMvc.perform(get("/api/contacts"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(contactDtos.size()));
    }

    @Test
    public void should_return_contact_when_given_id() throws Exception{
        long contactId = 1L;
        ContactDto contact = getContactDto("julien");
        given(contactService.findContactById(any(Long.class)))
                .willReturn(contact);

        ResultActions response = mockMvc.perform(get("/api/contacts/{id}", contactId));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(contact.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(contact.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(contact.getBirthDate().toString()));
    }

    @Test
    public void should_throw_exception_when_trying_to_find_not_found_contact() throws Exception{
        long contactId = 1L;
        given(contactService.findContactById(any(Long.class)))
                .willThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/contacts/{id}", contactId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_delete_contact_when_given_valid_id() throws Exception{
        long contactId = 1L;
        willDoNothing().given(contactService).deleteContactById(contactId);

        ResultActions response = mockMvc.perform(delete("/api/contacts/{id}", contactId));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void should_throw_exception_when_trying_to_delete_not_found_contact() throws Exception{
        long contactId = 1L;
        willThrow(EntityNotFoundException.class).given(contactService).deleteContactById(contactId);

        ResultActions response = mockMvc.perform(delete("/api/contacts/{id}", contactId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    private static Contact getContact() {
        return Contact.builder()
                .id(1L)
                .firstName("nabil")
                .lastName("moalla")
                .birthDate(LocalDate.of(2012, 01, 01))
                .build();
    }

    private static ContactDto getContactDto(String firstName) {
        return ContactDto.builder()
                .id(1L)
                .firstName(firstName)
                .lastName("moalla")
                .birthDate(LocalDate.of(2012, 01, 01))
                .build();
    }
}
