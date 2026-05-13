package com.clinicapp.service;

import com.clinicapp.entity.ClinicalHistory;
import com.clinicapp.repository.ClinicalHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicalHistoryService {

    private final ClinicalHistoryRepository clinicalHistoryRepository;

    public ClinicalHistoryService(ClinicalHistoryRepository clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    public ClinicalHistory saveClinicalHistory(ClinicalHistory clinicalHistory) {
        return clinicalHistoryRepository.save(clinicalHistory);
    }

    public List<ClinicalHistory> getAllClinicalHistories() {
        return clinicalHistoryRepository.findAll();
    }

    public ClinicalHistory getClinicalHistoryById(Long id) {
        return clinicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial clínico no encontrado"));
    }

    public List<ClinicalHistory> getHistoriesByPatientId(Long patientId) {
        return clinicalHistoryRepository.findByPatientId(patientId);
    }

    public List<ClinicalHistory> getHistoriesByDoctorId(Long doctorId) {
        return clinicalHistoryRepository.findByDoctorId(doctorId);
    }

    public void deleteClinicalHistory(Long id) {
        clinicalHistoryRepository.deleteById(id);
    }
}
