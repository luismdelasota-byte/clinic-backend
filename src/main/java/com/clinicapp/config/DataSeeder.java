package com.clinicapp.config;

import com.clinicapp.entity.*;
import com.clinicapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final RoleRepository roleRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public DataSeeder(UserRepository userRepository, 
                      DoctorRepository doctorRepository, 
                      PatientRepository patientRepository, 
                      AppointmentRepository appointmentRepository,
                      RoleRepository roleRepository,
                      DoctorScheduleRepository scheduleRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.roleRepository = roleRepository;
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Asegurar Roles
        ensureRole("ADMIN");
        ensureRole("DOCTOR");
        ensureRole("PATIENT");

        Role adminRole = roleRepository.findByName("ADMIN").get();
        Role doctorRole = roleRepository.findByName("DOCTOR").get();
        Role patientRole = roleRepository.findByName("PATIENT").get();

        // 2. Asegurar ADMIN
        ensureAdmin(adminRole);

        // 3. Poblar Doctores (si hay menos de 5)
        if (doctorRepository.count() < 5) {
            seedFlow(doctorRole, patientRole);
        }
    }

    private void ensureRole(String name) {
        if (!roleRepository.existsByName(name)) {
            roleRepository.save(new Role(name));
        }
    }

    private void ensureAdmin(Role role) {
        userRepository.findByUsername("admin").ifPresentOrElse(
            user -> {
                if (user.getRole() == null) {
                    user.setRole(role);
                    userRepository.save(user);
                }
            },
            () -> {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@clinic.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(role);
                userRepository.save(admin);
            }
        );
    }

    private void seedFlow(Role doctorRole, Role patientRole) {
        String[] specialties = {"Cardiología", "Pediatría", "Dermatología", "Ginecología", "Neurología", "Medicina General"};
        String[] docNames = {"Dr. Juan García", "Dra. María López", "Dr. Carlos Pérez", "Dra. Ana Torres", "Dr. Luis Sosa", "Dra. Elena Ruiz"};
        String[] days = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

        List<Doctor> savedDoctors = new ArrayList<>();
        for (int i = 0; i < docNames.length; i++) {
            String username = "doctor" + (i + 1);
            Doctor d = createDoctor(username, username + "@clinic.com", docNames[i], specialties[i], 10000 + i, doctorRole);
            savedDoctors.add(d);
            
            // Crear horarios para cada doctor
            for (String day : days) {
                scheduleRepository.save(new DoctorSchedule(d, day, LocalTime.of(8, 0), LocalTime.of(13, 0)));
                scheduleRepository.save(new DoctorSchedule(d, day, LocalTime.of(15, 0), LocalTime.of(18, 0)));
            }
        }

        String[] patNames = {"Pedro Alva", "Jimena Rios", "Marcos Diaz", "Silvia Luna", "Fernando Sol", "Paola Mar"};
        List<Patient> savedPatients = new ArrayList<>();
        for (int i = 0; i < patNames.length; i++) {
            String username = "paciente" + (i + 1);
            Patient p = createPatient(username, username + "@gmail.com", patNames[i], "9" + (10000000 + random.nextInt(90000000)), "19" + (70 + random.nextInt(30)) + "-01-01", patientRole);
            savedPatients.add(p);
        }

        // Crear Citas para generar el flujo
        for (int i = 0; i < 20; i++) {
            Doctor d = savedDoctors.get(random.nextInt(savedDoctors.size()));
            Patient p = savedPatients.get(random.nextInt(savedPatients.size()));
            createAppointment(d, p, LocalDateTime.now().plusDays(random.nextInt(7)).withHour(9 + random.nextInt(8)).withMinute(0));
        }

        System.out.println(">>> Flujo clínico poblado exitosamente (Admin, Doctores, Pacientes, Horarios y Citas).");
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
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode("doctor123"));
            newUser.setRole(role);
            return userRepository.save(newUser);
        });

        return doctorRepository.findByUserId(user.getId()).orElseGet(() -> {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setName(name);
            doctor.setSpeciality(speciality);
            doctor.setCmp(cmp);
            doctor.setEmail(email);
            doctor.setPhone("987654321");
            return doctorRepository.save(doctor);
        });
    }

    private Patient createPatient(String username, String email, String name, String phone, String birthDate, Role role) {
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode("paciente123"));
            newUser.setRole(role);
            return userRepository.save(newUser);
        });

        return patientRepository.findByUserId(user.getId()).orElseGet(() -> {
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setName(name);
            patient.setEmail(email);
            patient.setPhone(phone);
            patient.setBirthDate(LocalDate.parse(birthDate));
            return patientRepository.save(patient);
        });
    }
}
