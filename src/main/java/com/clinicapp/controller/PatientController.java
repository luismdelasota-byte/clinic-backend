package com.clinicapp.controller;

import com.clinicapp.entity.Patient;
import com.clinicapp.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    //Variable patientService de tipo PatientService
    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    // Crear paciente
    @PostMapping
    public Patient savePatient(@RequestBody Patient patient){
        return patientService.savePatient(patient);
    }

    // Listar todos los pacientes
    @GetMapping
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }

    // Obtener paciente por id
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id){
        return patientService.getPatientById(id);
    }

    // Eliminar paciente
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
    }

    //Actualizar paciente
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient){
        return patientService.updatePatient(id, patient);
    }

    // Buscar paciente por email
    @GetMapping("/email/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    // Buscar paciente por nombre
    @GetMapping("/name/{name}")
    public List<Patient> getPatientsByName(@PathVariable String name) {
        return patientService.getPatientsByName(name);
    }


}
