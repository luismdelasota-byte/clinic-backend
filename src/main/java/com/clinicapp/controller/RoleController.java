package com.clinicapp.controller;

import com.clinicapp.entity.Role;
import com.clinicapp.service.RoleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private RoleService roleService;

    //Constructor
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    public Role saveRole(@RequestBody Role role){
        return  roleService.saveRole(role);
    }

    @GetMapping
    public List<Role> getAllRoles(){
        return roleService.getAllRole();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
    }
}
