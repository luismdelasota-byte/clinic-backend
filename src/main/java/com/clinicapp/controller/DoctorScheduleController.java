package com.clinicapp.controller;

import com.clinicapp.entity.DoctorSchedule;
import com.clinicapp.service.DoctorScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    // Constructor
    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    // Crear horario (POST)
    @PostMapping
    public DoctorSchedule saveSchedule(@RequestBody DoctorSchedule schedule) {
        return doctorScheduleService.saveSchedule(schedule);
    }

    // Listar todos los horarios (GET)
    @GetMapping
    public List<DoctorSchedule> getAllSchedules() {
        return doctorScheduleService.getAllSchedules();
    }

    // Obtener horario por ID (GET)
    @GetMapping("/{id}")
    public Optional<DoctorSchedule> getScheduleById(@PathVariable Long id) {
        return doctorScheduleService.getScheduleById(id);
    }

    // Listar horarios por doctor (GET)
    @GetMapping("/doctor/{doctorId}")
    public List<DoctorSchedule> getSchedulesByDoctor(@PathVariable Long doctorId) {
        return doctorScheduleService.getSchedulesByDoctor(doctorId);
    }

    // Listar horarios por día (GET)
    @GetMapping("/day/{dayOfWeek}")
    public List<DoctorSchedule> getSchedulesByDay(@PathVariable String dayOfWeek) {
        return doctorScheduleService.getSchedulesByDay(dayOfWeek);
    }

    // Actualizar horario (PUT)
    @PutMapping("/{id}")
    public DoctorSchedule updateSchedule(@PathVariable Long id, @RequestBody DoctorSchedule scheduleDetails) {
        return doctorScheduleService.updateSchedule(id, scheduleDetails);
    }

    // Eliminar horario (DELETE)
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        doctorScheduleService.deleteSchedule(id);
    }
}
