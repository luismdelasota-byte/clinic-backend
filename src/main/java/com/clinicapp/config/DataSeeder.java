package com.clinicapp.config;

import com.clinicapp.entity.*;
import com.clinicapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            seedRoles();
        }
        if (userRepository.count() == 0) {
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
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@clinic.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(adminRole);
        userRepository.save(admin);

        // 2. Create DOCTORS
        Doctor d1 = createDoctor("dr_garcia", "garcia@clinic.com", "Juan García", "Cardiología", 12345, doctorRole);
        Doctor d2 = createDoctor("dr_lopez", "lopez@clinic.com", "María López", "Pediatría", 67890, doctorRole);
        Doctor d3 = createDoctor("dr_perez", "perez@clinic.com", "Carlos Pérez", "Dermatología", 11223, doctorRole);

        // 3. Create PATIENTS
        Patient p1 = createPatient("paciente1", "paciente1@gmail.com", "Ana Martínez", "999888777", "1990-05-15", patientRole);
        Patient p2 = createPatient("paciente2", "paciente2@gmail.com", "Roberto Gómez", "999555444", "1985-10-20", patientRole);
        Patient p3 = createPatient("paciente3", "paciente3@gmail.com", "Lucía Torres", "999111222", "1995-02-28", patientRole);
        Patient p4 = createPatient("paciente4", "paciente4@gmail.com", "Diego Sosa", "999333222", "1978-12-10", patientRole);
        Patient p5 = createPatient("paciente5", "paciente5@gmail.com", "Elena Ruiz", "999444555", "2000-07-04", patientRole);

        // 4. Create APPOINTMENTS
        createAppointment(d1, p1, LocalDateTime.now().plusHours(2));
        createAppointment(d1, p2, LocalDateTime.now().plusDays(1).withHour(10));
        createAppointment(d2, p3, LocalDateTime.now().plusHours(4));
        createAppointment(d2, p4, LocalDateTime.now().minusDays(1).withHour(15));
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
