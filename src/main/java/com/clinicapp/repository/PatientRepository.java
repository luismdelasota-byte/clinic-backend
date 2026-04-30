package com.clinicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinicapp.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}