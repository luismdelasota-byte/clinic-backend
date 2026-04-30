package com.clinicapp.service;

import com.clinicapp.entity.Patient;
import com.clinicapp.entity.Appointment;
import com.clinicapp.entity.Doctor;
import com.clinicapp.repository.AppointmentRepository;
import com.clinicapp.repository.DoctorRepository;
import com.clinicapp.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    //Constructor
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /*Modulos*/

    //Crear
    public Appointment saveAppointment(Appointment appointment){

        // Validar doctor
        if (appointment.getDoctor() == null) {
            throw new RuntimeException("Debe asignarse un doctor a la cita.");
        }
        // Validar paciente
        if (appointment.getPatient() == null) {
            throw new RuntimeException("Debe asignarse un paciente a la cita.");
        }

        /*validar fecha*/

        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("La fecha de la cita es obligatoria.");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede crear citas en el pasado.");
        }
        //No permitir citas despues de las 18:00
        if(appointment.getAppointmentDate().getHour()>18){
            //throw -> se usa para lanzar una excepcion, un excepcion es un objeto
            //que representa un error o situacon inesperada
            throw new RuntimeException("No se permite citas desde las 18:00");
        }
        //No permitir citas en el pasado
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("No se puede crear citas en el pasado");
        }

        // Validar estado
        if (appointment.getStatus() == null || appointment.getStatus().isBlank()) {
            throw new RuntimeException("El estado de la cita es obligatorio.");
        }

        List<String> status  = List.of("SCHEDULED", "CANCELLED", "COMPLETED");
        //contains: se usa en listas -> Verifica o busca si la coleccion o cadena contiene un elemento especifico
        //Es decir busca dentro de la lista si existe exactamente ese valor
        //equals: se usa en objetos(String, Integet, etc ) -> Compara si dos objetos son exactamente iguales en contenido
        if (!status.contains(appointment.getStatus())){
            throw new RuntimeException("El estado debe ser SCHEDULED, CANCELLED o COMPLETED.");
        }
        return appointmentRepository.save(appointment);
    }

    //Listar todas las citas
    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    //Obtener cita por id
    public Appointment getAppointmentById(Long id){
        return appointmentRepository.findById(id).orElse(null);
    }

    //Eliminar cita
    public void deleteAppointment(Long id){
        appointmentRepository.deleteById(id);
    }

    //Actualizar
    public Appointment updateAppointment(Long id, Appointment appointment){
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Actualizar campos
        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setDoctor(appointment.getDoctor());
        existing.setPatient(appointment.getPatient());

        // Buscar doctor y paciente completos
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        existing.setDoctor(doctor);
        existing.setPatient(patient);


        return appointmentRepository.save(existing);
    }


}

