package com.clinicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinicapp.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    //Busca los datos y devuelve true o fals, no devuelve el valor
    boolean existsByCmp(int cmp);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}