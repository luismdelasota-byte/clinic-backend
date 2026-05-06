package com.clinicapp.service;

import com.clinicapp.entity.Patient;
import com.clinicapp.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    // Constructor con inyección de dependencias
    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    // Crear o actualizar paciente
    public Patient savePatient(Patient patient){

        /*Validacion nombre*/

        if(patient.getName() == null || patient.getName().isBlank()){
            throw new IllegalArgumentException("Nombre oligatorio....");
        }

        if(patient.getName().length() <2){
            throw new IllegalArgumentException(("El nombre dene tener al menos 2 caracteres"));
        }

        if (patient.getName().length() > 100) {
            throw new IllegalArgumentException("El nombre no debe superar los 100 caracteres.");
        }

        //Validar caracteres permitidos(solo letras y espacios)
        if(!patient.getName().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")){
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
        }

        /*Validacion email*/

        if (patient.getEmail() == null || patient.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (!patient.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("El email no tiene un formato válido.");
        }

        /*Validar telefono*/

        if (patient.getPhone() == null || patient.getPhone().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        if (!patient.getPhone().matches("^[0-9]{7,15}$")) {
            throw new IllegalArgumentException("El teléfono debe contener solo números y entre 7 y 15 dígitos.");
        }


        /*Validar fecha de nacimiento*/
        if (patient.getBirthDate() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (patient.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento debe estar en el pasado.");
        }


        return patientRepository.save(patient);
    }

    // Listar todos los pacientes
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    // Obtener paciente por id
    public Patient getPatientById(Long id){
        return patientRepository.findById(id).orElse(null);
    }

    // Eliminar paciente
    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Actualizar campos
        patient.setName(updatedPatient.getName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhone(updatedPatient.getPhone());
        patient.setBirthDate(updatedPatient.getBirthDate());

        return patientRepository.save(patient);
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }


}