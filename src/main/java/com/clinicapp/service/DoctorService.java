package com.clinicapp.service;

import com.clinicapp.entity.Doctor;
import com.clinicapp.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // Constructor con inyección de dependencias
    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    // Crear o actualizar doctor
    public Doctor saveDoctor(Doctor doctor){

        // Validar CMP (número de colegiatura)
        if (doctor.getCmp() <= 0) {
            throw new IllegalArgumentException("El CMP debe ser un número positivo.");
        }
        if (doctorRepository.existsByCmp(doctor.getCmp())) {
            throw new IllegalArgumentException("Ya existe un doctor con ese CMP.");
        }

        // Validar nombre
        if (doctor.getName() == null || doctor.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (doctor.getName().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        }
        if (doctor.getName().length() > 100) {
            throw new IllegalArgumentException("El nombre no debe superar los 100 caracteres.");
        }
        if (!doctor.getName().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }

        // Validar especialidad
        if (doctor.getSpeciality() == null || doctor.getSpeciality().isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }

        // Validar email
        if (doctor.getEmail() == null || doctor.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (!doctor.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("El email no tiene un formato válido.");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("Ya existe un doctor con ese correo.");
        }

        // Validar teléfono
        if (doctor.getPhone() != null && !doctor.getPhone().isBlank()) {
            if (!doctor.getPhone().matches("^[0-9]{7,15}$")) {
                throw new IllegalArgumentException("El teléfono debe contener solo números y entre 7 y 15 dígitos.");
            }
            if (doctorRepository.existsByPhone(doctor.getPhone())) {
                throw new IllegalArgumentException("Ya existe un doctor con ese teléfono.");
            }
        }

        return doctorRepository.save(doctor);
    }

    // Listar todos los doctores
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    // Obtener doctor por id
    public Doctor getDoctorById(Long id){
        return doctorRepository.findById(id).orElse(null);
    }

    // Eliminar doctor
    public void deleteDoctor(Long id){
        doctorRepository.deleteById(id);
    }

    public Doctor updateDoctor(Long id, Doctor doctor){
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        existingDoctor.setCmp(doctor.getCmp());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setName(doctor.getName());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setSpeciality(doctor.getSpeciality());

        return doctorRepository.save(existingDoctor);
    }

}