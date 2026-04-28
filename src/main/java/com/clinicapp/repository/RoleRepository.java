package com.clinicapp.repository;

import com.clinicapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);
}
