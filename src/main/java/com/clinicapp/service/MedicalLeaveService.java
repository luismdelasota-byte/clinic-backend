package com.clinicapp.service;

import com.clinicapp.entity.MedicalLeave;
import com.clinicapp.repository.MedicalLeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalLeaveService {

    private final MedicalLeaveRepository medicalLeaveRepository;

    public MedicalLeaveService(MedicalLeaveRepository medicalLeaveRepository) {
        this.medicalLeaveRepository = medicalLeaveRepository;
    }

    public MedicalLeave saveMedicalLeave(MedicalLeave medicalLeave) {
        return medicalLeaveRepository.save(medicalLeave);
    }

    public List<MedicalLeave> getAllMedicalLeaves() {
        return medicalLeaveRepository.findAll();
    }

    public MedicalLeave getMedicalLeaveById(Long id) {
        return medicalLeaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descanso médico no encontrado"));
    }

    public List<MedicalLeave> getLeavesByPatientId(Long patientId) {
        return medicalLeaveRepository.findByPatientId(patientId);
    }

    public List<MedicalLeave> getLeavesByDoctorId(Long doctorId) {
        return medicalLeaveRepository.findByDoctorId(doctorId);
    }

    public void deleteMedicalLeave(Long id) {
        medicalLeaveRepository.deleteById(id);
    }
}
