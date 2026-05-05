package com.clinicapp.repository;

import com.clinicapp.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    // Buscar horarios por doctor
    List<DoctorSchedule> findByDoctorId(Long doctorId);

    // Buscar horarios por día de la semana
    List<DoctorSchedule> findByDayOfWeek(String dayOfWeek);
}
