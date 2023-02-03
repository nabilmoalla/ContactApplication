package com.givaudan.repository;

import com.givaudan.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Override
    Optional<Contact> findById(Long id);
}
