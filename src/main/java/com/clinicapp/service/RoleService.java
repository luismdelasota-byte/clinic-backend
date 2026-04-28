package com.clinicapp.service;

import com.clinicapp.entity.Role;
import com.clinicapp.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    //Constructor
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    //Metodos
    public Role saveRole(Role role){

        //Validacion role
        if(role.getName() == null || role.getName().isBlank()){
            throw new IllegalArgumentException("Role obligatorio");
        }

        //Verificar duplicados
        if(roleRepository.existsByName(role.getName())){
            throw new IllegalArgumentException("Rol ya creado");
        }

        //Convertir todo a mayuscula
        String roleName = role.getName().toUpperCase();

        //Validar que sea uno de los permitidos
        List<String> validRole = List.of("ADMIN", "USER", "DOCTOR", "PATIENT");

        //Si la lista no contiene
        if(!validRole.contains(roleName)){
            throw new IllegalArgumentException("Rolee invalido...");
        }

        //Actualizamos
        role.setName(roleName);

        return  roleRepository.save(role);
    }

    public Role getRoleById(Long id){
        return  roleRepository.findById(id).orElse(null);
    }

    //Listar roles
    public List<Role> getAllRole(){
        return  roleRepository.findAll();
    }

    //Eliminar roles
    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }
}
