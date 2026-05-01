package com.clinicapp.controller;

import com.clinicapp.entity.MedicalHistory;
import com.clinicapp.service.MedicalHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/histories")
public class MedicalHistoryController {

    private final MedicalHistoryService service;

    public MedicalHistoryController(MedicalHistoryService service) {
        this.service = service;
    }

    // Crear historial (solo DOCTOR)
    @PostMapping
    public ResponseEntity<MedicalHistory> create(@RequestBody MedicalHistory history) {
        return ResponseEntity.ok(service.createHistory(history));
    }

    // Ver historial por paciente
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<MedicalHistory>> getByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByPatient(id));
    }

    // Ver historial por doctor
    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<MedicalHistory>> getByDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByDoctor(id));
    }
}