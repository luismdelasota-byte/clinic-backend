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
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final MedicalLeaveRepository medicalLeaveRepository;
    private final MedicalReportRepository medicalReportRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public DataSeeder(UserRepository userRepository, 
                      DoctorRepository doctorRepository, 
                      PatientRepository patientRepository, 
                      AppointmentRepository appointmentRepository,
                      RoleRepository roleRepository,
                      DoctorScheduleRepository scheduleRepository,
                      ClinicalHistoryRepository clinicalHistoryRepository,
                      MedicalLeaveRepository medicalLeaveRepository,
                      MedicalReportRepository medicalReportRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.roleRepository = roleRepository;
        this.scheduleRepository = scheduleRepository;
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.medicalLeaveRepository = medicalLeaveRepository;
        this.medicalReportRepository = medicalReportRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        ensureRole("ADMIN");
        ensureRole("DOCTOR");
        ensureRole("PATIENT");

        Role adminRole = roleRepository.findByName("ADMIN").get();
        Role doctorRole = roleRepository.findByName("DOCTOR").get();
        Role patientRole = roleRepository.findByName("PATIENT").get();

        ensureAdmin(adminRole);

        if (doctorRepository.count() < 5) {
            seedFlow(doctorRole, patientRole);
        }
        
        // 4. Asegurar agenda para doctor1 específicamente
        seedAgendaForDoctor1();
    }

    private void seedAgendaForDoctor1() {
        doctorRepository.findAll().stream()
            .filter(d -> d.getUser().getUsername().equals("doctor1"))
            .findFirst()
            .ifPresent(doc1 -> {
                if (appointmentRepository.findByDoctor_Id(doc1.getId()).size() < 5) {
                    List<Patient> patients = patientRepository.findAll();
                    if (!patients.isEmpty()) {
                        for (int i = 0; i < 8; i++) {
                            Patient p = patients.get(random.nextInt(patients.size()));
                            // Algunas hoy, otras mañana
                            int daysOffset = (i < 3) ? 0 : i; 
                            createAppointment(doc1, p, LocalDateTime.now().plusDays(daysOffset).withHour(9 + (i % 6)).withMinute(0), "SCHEDULED");
                        }
                        System.out.println(">>> Agenda de doctor1 poblada con 8 citas nuevas.");
                    }
                }
            });
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

        // Crear Citas y Documentos Clínicos
        for (int i = 0; i < 15; i++) {
            Doctor d = savedDoctors.get(random.nextInt(savedDoctors.size()));
            Patient p = savedPatients.get(random.nextInt(savedPatients.size()));
            Appointment appt = createAppointment(d, p, LocalDateTime.now().minusDays(random.nextInt(10)).withHour(9 + random.nextInt(8)).withMinute(0), "COMPLETED");
            
            // Crear Diario Clínico
            ClinicalHistory history = new ClinicalHistory();
            history.setDoctor(d);
            history.setPatient(p);
            history.setAppointment(appt);
            history.setDiagnosis("Diagnóstico preventivo para " + p.getName() + " realizado por " + d.getName());
            history.setTreatment("Tratamiento de seguimiento indicado: reposo y medicación estándar.");
            clinicalHistoryRepository.save(history);

            // Crear Descanso Médico para algunos
            if (i % 3 == 0) {
                MedicalLeave leave = new MedicalLeave();
                leave.setDoctor(d);
                leave.setPatient(p);
                leave.setReason("Reposo absoluto por complicación en " + d.getSpeciality());
                leave.setStartDate(LocalDate.now());
                leave.setEndDate(LocalDate.now().plusDays(3));
                medicalLeaveRepository.save(leave);
            }

            // Crear Informe para otros
            if (i % 5 == 0) {
                MedicalReport report = new MedicalReport();
                report.setDoctor(d);
                report.setPatient(p);
                report.setDescription("Informe detallado sobre evolución post-tratamiento en el área de " + d.getSpeciality());
                report.setRequiresHospitalization(false);
            }
        }

        // Asegurar que doctor1 tenga una agenda activa con citas próximas
        Doctor doc1 = savedDoctors.stream()
                .filter(d -> d.getUser().getUsername().equals("doctor1"))
                .findFirst()
                .orElse(savedDoctors.get(0));
                
        for (int i = 0; i < 8; i++) {
            Patient p = savedPatients.get(random.nextInt(savedPatients.size()));
            createAppointment(doc1, p, LocalDateTime.now().plusDays(i + 1).withHour(9 + (i % 4)).withMinute(0), "SCHEDULED");
        }

        System.out.println(">>> Flujo clínico completo generado exitosamente.");
    }

    private Appointment createAppointment(Doctor doctor, Patient patient, LocalDateTime date, String status) {
        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDate(date);
        appt.setStatus(status);
        return appointmentRepository.save(appt);
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
