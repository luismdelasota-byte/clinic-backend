package com.clinicapp.service;

import com.clinicapp.entity.MedicalHistory;
import com.clinicapp.repository.MedicalHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository repository;

    public MedicalHistoryService(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public MedicalHistory createHistory(MedicalHistory history) {
        return repository.save(history);
    }

    public List<MedicalHistory> getByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    public List<MedicalHistory> getByDoctor(Long doctorId) {
        return repository.findByDoctorId(doctorId);
    }
}