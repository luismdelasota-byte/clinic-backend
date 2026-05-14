package com.clinicapp.config;

import com.clinicapp.entity.*;
import com.clinicapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public DataSeeder(UserRepository userRepository, 
                      DoctorRepository doctorRepository, 
                      PatientRepository patientRepository, 
                      AppointmentRepository appointmentRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            seedRoles();
        }
        // Poblar si hay pocos datos (menos de 5)
        if (userRepository.count() <= 1) { 
            seedData();
        }
    }

    private void seedRoles() {
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("DOCTOR"));
        roleRepository.save(new Role("PATIENT"));
    }

    private void seedData() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role doctorRole = roleRepository.findByName("DOCTOR").orElseThrow();
        Role patientRole = roleRepository.findByName("PATIENT").orElseThrow();

        // 1. Create ADMIN
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@clinic.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }

        // 2. Create 10 DOCTORS
        String[] specialties = {"Cardiología", "Pediatría", "Dermatología", "Ginecología", "Neurología", "Oftalmología", "Psiquiatría", "Traumatología", "Urología", "Medicina General"};
        String[] docNames = {"Juan García", "María López", "Carlos Pérez", "Ana Torres", "Luis Sosa", "Elena Ruiz", "Roberto Gómez", "Lucía Martínez", "Diego Castro", "Sofía Vega"};
        
        Doctor[] doctors = new Doctor[10];
        for (int i = 0; i < 10; i++) {
            String username = "doctor" + (i + 1);
            if (userRepository.findByUsername(username).isEmpty()) {
                doctors[i] = createDoctor(username, username + "@clinic.com", docNames[i], specialties[i], 10000 + i, doctorRole);
            }
        }

        // 3. Create 10 PATIENTS
        String[] patNames = {"Pedro Alva", "Jimena Rios", "Marcos Diaz", "Silvia Luna", "Fernando Sol", "Paola Mar", "Victor Paz", "Clara Luz", "Hugo Rey", "Isabel Gil"};
        Patient[] patients = new Patient[10];
        for (int i = 0; i < 10; i++) {
            String username = "paciente" + (i + 1);
            if (userRepository.findByUsername(username).isEmpty()) {
                patients[i] = createPatient(username, username + "@gmail.com", patNames[i], "9" + (10000000 + random.nextInt(90000000)), "19" + (70 + random.nextInt(30)) + "-01-01", patientRole);
            }
        }

        // 4. Create some APPOINTMENTS
        for (int i = 0; i < 15; i++) {
            Doctor d = doctors[random.nextInt(10)];
            Patient p = patients[random.nextInt(10)];
            if (d != null && p != null) {
                createAppointment(d, p, LocalDateTime.now().plusDays(random.nextInt(5)).plusHours(random.nextInt(8)));
            }
        }
        
        System.out.println(">>> Base de datos poblada con 20+ registros exitosamente.");
    }

    private void createAppointment(Doctor doctor, Patient patient, LocalDateTime date) {
        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDate(date);
        appt.setStatus("SCHEDULED");
        appointmentRepository.save(appt);
    }

    private Doctor createDoctor(String username, String email, String name, String speciality, int cmp, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("doctor123"));
        user.setRole(role);
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

    private Patient createPatient(String username, String email, String name, String phone, String birthDate, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("paciente123"));
        user.setRole(role);
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
