package com.clinicapp.config;

import com.clinicapp.entity.*;
import com.clinicapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        // 1. Create ADMIN
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@clinic.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        // 2. Create DOCTORS
        Doctor d1 = createDoctor("dr_garcia", "garcia@clinic.com", "Juan García", "Cardiología", 12345);
        Doctor d2 = createDoctor("dr_lopez", "lopez@clinic.com", "María López", "Pediatría", 67890);
        Doctor d3 = createDoctor("dr_perez", "perez@clinic.com", "Carlos Pérez", "Dermatología", 11223);

        // 3. Create PATIENTS
        Patient p1 = createPatient("paciente1", "paciente1@gmail.com", "Ana Martínez", "999888777", "1990-05-15");
        Patient p2 = createPatient("paciente2", "paciente2@gmail.com", "Roberto Gómez", "999555444", "1985-10-20");
        Patient p3 = createPatient("paciente3", "paciente3@gmail.com", "Lucía Torres", "999111222", "1995-02-28");
        Patient p4 = createPatient("paciente4", "paciente4@gmail.com", "Diego Sosa", "999333222", "1978-12-10");
        Patient p5 = createPatient("paciente5", "paciente5@gmail.com", "Elena Ruiz", "999444555", "2000-07-04");

        // 4. Create APPOINTMENTS (Mis citas)
        createAppointment(d1, p1, LocalDateTime.now().plusHours(2));
        createAppointment(d1, p2, LocalDateTime.now().plusDays(1).withHour(10));
        createAppointment(d2, p3, LocalDateTime.now().plusHours(4));
        createAppointment(d2, p4, LocalDateTime.now().minusDays(1).withHour(15)); // Pasada
        createAppointment(d3, p5, LocalDateTime.now().plusDays(2).withHour(11));
        
        System.out.println(">>> Base de datos poblada con éxito.");
    }

    private void createAppointment(Doctor doctor, Patient patient, LocalDateTime date) {
        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDate(date);
        appt.setStatus("SCHEDULED");
        appointmentRepository.save(appt);
    }

    private Doctor createDoctor(String username, String email, String name, String speciality, int cmp) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("doctor123"));
        user.setRole("DOCTOR");
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(name);
        doctor.setSpeciality(speciality);
        doctor.setCmp(cmp);
        doctor.setEmail(email);
        doctor.setPhone("987654321");
        return doctorRepository.save(doctor);
    }

    private Patient createPatient(String username, String email, String name, String phone, String birthDate) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("paciente123"));
        user.setRole("PATIENT");
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setName(name);
        patient.setEmail(email);
        patient.setPhone(phone);
        patient.setBirthDate(LocalDate.parse(birthDate));
        return patientRepository.save(patient);
    }
}
