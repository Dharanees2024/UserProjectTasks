package com.ums.controller;

import com.ums.dto.RoleDto;
import com.ums.entity.Role;
import com.ums.entity.RolePrivilege;
import com.ums.response.ResponseModel;
import com.ums.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping("/privilege")
    public ResponseModel createRolePrivilege(@Valid @RequestBody RolePrivilege rolePrivilegePayload) {
        return ResponseModel.success(HttpStatus.OK, "Role privilege created successfully", roleService.createRolePrivilege(rolePrivilegePayload));
    }

    @GetMapping("/privilege")
    public ResponseModel getAllRolePrivilge() {
        return ResponseModel.success(HttpStatus.OK, "Success", roleService.getAllRolePrivilge());
    }

    @PutMapping("/privilege/{rolePrivilegeId}")
    public ResponseModel updateRolePrivilege(@Valid @RequestBody RolePrivilege rolePrivilegePayload, @PathVariable(name = "rolePrivilegeId", required = true) String rolePrivilegeId) {
        return ResponseModel.success(HttpStatus.OK, "Role privilege updated successfully", roleService.updateRolePrivilege(rolePrivilegePayload, rolePrivilegeId));
    }

    @PostMapping()
    public ResponseModel createRole(@Valid @RequestBody RoleDto rolePayload) {
        return ResponseModel.success(HttpStatus.OK, "Role created successfully", roleService.createRole(rolePayload));
    }

    @GetMapping()
    public ResponseModel getAllRoles() {
        return ResponseModel.success(HttpStatus.OK, "Success", roleService.getAllRoles());
    }

    @GetMapping("/reporting-to")
    public ResponseModel getAllRolesWithReportingTo() {
        return ResponseModel.success(HttpStatus.OK, "Success", roleService.getAllRolesWithReportingTo());
    }

    @PutMapping("/{roleId}")
    public ResponseModel updateRole(@Valid @RequestBody RoleDto rolePayload, @PathVariable(name = "roleId", required = true) String roleId) {
        return ResponseModel.success(HttpStatus.OK, "Role updated successfully", roleService.updateRole(rolePayload, roleId));
    }
}
