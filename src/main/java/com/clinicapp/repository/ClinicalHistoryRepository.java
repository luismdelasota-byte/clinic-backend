package com.clinicapp.repository;

import com.clinicapp.entity.ClinicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, Long> {
    List<ClinicalHistory> findByPatientId(Long patientId);
    List<ClinicalHistory> findByDoctorId(Long doctorId);
}
