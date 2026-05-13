package com.clinicapp.controller;

import com.clinicapp.entity.MedicalLeave;
import com.clinicapp.service.MedicalLeaveService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-leaves")
public class MedicalLeaveController {

    private final MedicalLeaveService medicalLeaveService;

    public MedicalLeaveController(MedicalLeaveService medicalLeaveService) {
        this.medicalLeaveService = medicalLeaveService;
    }

    @PostMapping
    public MedicalLeave saveMedicalLeave(@RequestBody MedicalLeave medicalLeave) {
        return medicalLeaveService.saveMedicalLeave(medicalLeave);
    }

    @GetMapping
    public List<MedicalLeave> getAllMedicalLeaves() {
        return medicalLeaveService.getAllMedicalLeaves();
    }

    @GetMapping("/{id}")
    public MedicalLeave getMedicalLeaveById(@PathVariable Long id) {
        return medicalLeaveService.getMedicalLeaveById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalLeave> getLeavesByPatient(@PathVariable Long patientId) {
        return medicalLeaveService.getLeavesByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<MedicalLeave> getLeavesByDoctor(@PathVariable Long doctorId) {
        return medicalLeaveService.getLeavesByDoctorId(doctorId);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalLeave(@PathVariable Long id) {
        medicalLeaveService.deleteMedicalLeave(id);
    }
}
