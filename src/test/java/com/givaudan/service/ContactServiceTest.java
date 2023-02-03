package com.givaudan.service;


import com.givaudan.domain.Contact;
import com.givaudan.dto.ContactDto;
import com.givaudan.exceptions.EntityNotFoundException;
import com.givaudan.repository.ContactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;


    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    public void should_return_contact_when_saving() {
        ContactDto contactDto = getContactDto("nabil");
        Contact contact = getContact();
        when(contactRepository.save(ArgumentMatchers.any(Contact.class))).thenReturn(contact);
        ContactDto created = contactService.createContact(contactDto);
        assertThat(created.getFirstName()).isSameAs(contactDto.getFirstName());
    }

    @Test
    public void should_update_contact_when_given_id_if_found() {
        Contact contact = getContact();
        ContactDto contactDTO = getContactDto("julien");
        given(contactRepository.findById(contact.getId())).willReturn(Optional.of(contact));
        given(contactRepository.save(contact)).willReturn(contact);
        ContactDto updatedContact = contactService.updateContact(contact.getId(), contactDTO);
        assertThat(updatedContact.getFirstName()).isSameAs(contactDTO.getFirstName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_throw_exception_when_contact_doesnt_exist() {
        Contact contact = getContact();
        ContactDto contactDTO = getContactDto("julien");
        given(contactRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        contactService.updateContact(contact.getId(), contactDTO);
    }

    @Test
    public void should_return_all_contacts() {
        ContactDto contactDTO = getContactDto("nabil");
        Contact contact = getContact();
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        List<ContactDto> contactDtos = new ArrayList<>();
        contactDtos.add(contactDTO);

        given(contactRepository.findAll()).willReturn(contacts);
        List<ContactDto> expected = contactService.findAll();
        assertEquals(expected, contactDtos);
        verify(contactRepository).findAll();
    }

    @Test
    public void should_return_contact_when_given_id_if_found() {
        Contact contact = getContact();
        ContactDto contactDTO = getContactDto("nabil");
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        ContactDto expected = contactService.findContactById(contact.getId());
        assertThat(expected).isEqualTo(contactDTO);
        verify(contactRepository).findById(contact.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_throw_exception_when_given_id_not_found() {
        Contact contact = getContact();
        given(contactRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        contactService.findContactById(contact.getId());
    }

    @Test
    public void should_delete_contact_when_given_id_if_found(){
        Contact contact = getContact();
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        contactService.deleteContactById(contact.getId());
        verify(contactRepository).deleteById(contact.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_throw_exception_when_delete_not_found_contact() {
        Contact contact = getContact();
        given(contactRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        contactService.deleteContactById(contact.getId());
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
