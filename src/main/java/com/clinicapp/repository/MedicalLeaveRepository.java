package com.clinicapp.repository;

import com.clinicapp.entity.MedicalLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalLeaveRepository extends JpaRepository<MedicalLeave, Long> {
    List<MedicalLeave> findByPatientId(Long patientId);
    List<MedicalLeave> findByDoctorId(Long doctorId);
}
