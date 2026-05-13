package com.clinicapp.controller;

import com.clinicapp.entity.ClinicalHistory;
import com.clinicapp.service.ClinicalHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinical-histories")
public class ClinicalHistoryController {

    private final ClinicalHistoryService clinicalHistoryService;

    public ClinicalHistoryController(ClinicalHistoryService clinicalHistoryService) {
        this.clinicalHistoryService = clinicalHistoryService;
    }

    @PostMapping
    public ClinicalHistory saveClinicalHistory(@RequestBody ClinicalHistory clinicalHistory) {
        return clinicalHistoryService.saveClinicalHistory(clinicalHistory);
    }

    @GetMapping
    public List<ClinicalHistory> getAllClinicalHistories() {
        return clinicalHistoryService.getAllClinicalHistories();
    }

    @GetMapping("/{id}")
    public ClinicalHistory getClinicalHistoryById(@PathVariable Long id) {
        return clinicalHistoryService.getClinicalHistoryById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<ClinicalHistory> getHistoriesByPatient(@PathVariable Long patientId) {
        return clinicalHistoryService.getHistoriesByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<ClinicalHistory> getHistoriesByDoctor(@PathVariable Long doctorId) {
        return clinicalHistoryService.getHistoriesByDoctorId(doctorId);
    }

    @DeleteMapping("/{id}")
    public void deleteClinicalHistory(@PathVariable Long id) {
        clinicalHistoryService.deleteClinicalHistory(id);
    }
}
