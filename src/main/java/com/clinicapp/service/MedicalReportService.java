package com.clinicapp.service;

import com.clinicapp.entity.MedicalReport;
import com.clinicapp.repository.MedicalReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalReportService {

    private final MedicalReportRepository medicalReportRepository;

    public MedicalReportService(MedicalReportRepository medicalReportRepository) {
        this.medicalReportRepository = medicalReportRepository;
    }

    public MedicalReport saveMedicalReport(MedicalReport medicalReport) {
        return medicalReportRepository.save(medicalReport);
    }

    public List<MedicalReport> getAllMedicalReports() {
        return medicalReportRepository.findAll();
    }

    public MedicalReport getMedicalReportById(Long id) {
        return medicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Informe médico no encontrado"));
    }

    public List<MedicalReport> getReportsByPatientId(Long patientId) {
        return medicalReportRepository.findByPatientId(patientId);
    }

    public List<MedicalReport> getReportsByDoctorId(Long doctorId) {
        return medicalReportRepository.findByDoctorId(doctorId);
    }

    public void deleteMedicalReport(Long id) {
        medicalReportRepository.deleteById(id);
    }
}
