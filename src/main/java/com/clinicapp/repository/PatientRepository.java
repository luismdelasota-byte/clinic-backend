package com.clinicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinicapp.entity.Patient;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String email);
    List<Patient> findByNameContainingIgnoreCase(String name);
}