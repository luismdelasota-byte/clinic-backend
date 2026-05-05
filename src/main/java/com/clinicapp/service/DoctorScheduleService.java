package com.clinicapp.service;

import com.clinicapp.entity.DoctorSchedule;
import com.clinicapp.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    // Constructor
    public DoctorScheduleService(DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    // Crear horario
    public DoctorSchedule saveSchedule(DoctorSchedule schedule) {
        return doctorScheduleRepository.save(schedule);
    }

    // Listar todos los horarios
    public List<DoctorSchedule> getAllSchedules() {
        return doctorScheduleRepository.findAll();
    }

    // Listar horarios por doctor
    public List<DoctorSchedule> getSchedulesByDoctor(Long doctorId) {
        return doctorScheduleRepository.findByDoctorId(doctorId);
    }

    // Listar horarios por día
    public List<DoctorSchedule> getSchedulesByDay(String dayOfWeek) {
        return doctorScheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    // Obtener horario por ID
    public Optional<DoctorSchedule> getScheduleById(Long id) {
        return doctorScheduleRepository.findById(id);
    }

    // Actualizar horario
    public DoctorSchedule updateSchedule(Long id, DoctorSchedule scheduleDetails) {
        return doctorScheduleRepository.findById(id)
                .map(schedule -> {
                    schedule.setDoctor(scheduleDetails.getDoctor());
                    schedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
                    schedule.setStartTime(scheduleDetails.getStartTime());
                    schedule.setEndTime(scheduleDetails.getEndTime());
                    return doctorScheduleRepository.save(schedule);
                })
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con id " + id));
    }

    // Eliminar horario
    public void deleteSchedule(Long id) {
        doctorScheduleRepository.deleteById(id);
    }
}
