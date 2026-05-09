package com.clinicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinicapp.entity.Patient;
import org.springframework.web.service.annotation.PatchExchange;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String email);
    List<Patient> findByNameContainingIgnoreCase(String name);

    Optional<Patient> findByUserId(Long userID);
}