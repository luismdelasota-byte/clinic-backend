package com.clinicapp.controller;

import com.clinicapp.entity.MedicalReport;
import com.clinicapp.service.MedicalReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-reports")
public class MedicalReportController {

    private final MedicalReportService medicalReportService;

    public MedicalReportController(MedicalReportService medicalReportService) {
        this.medicalReportService = medicalReportService;
    }

    @PostMapping
    public MedicalReport saveMedicalReport(@RequestBody MedicalReport medicalReport) {
        return medicalReportService.saveMedicalReport(medicalReport);
    }

    @GetMapping
    public List<MedicalReport> getAllMedicalReports() {
        return medicalReportService.getAllMedicalReports();
    }

    @GetMapping("/{id}")
    public MedicalReport getMedicalReportById(@PathVariable Long id) {
        return medicalReportService.getMedicalReportById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalReport> getReportsByPatient(@PathVariable Long patientId) {
        return medicalReportService.getReportsByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<MedicalReport> getReportsByDoctor(@PathVariable Long doctorId) {
        return medicalReportService.getReportsByDoctorId(doctorId);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalReport(@PathVariable Long id) {
        medicalReportService.deleteMedicalReport(id);
    }
}
