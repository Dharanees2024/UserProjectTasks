package com.ums.service;

import com.ums.dto.RoleDto;
import com.ums.entity.Role;
import com.ums.entity.RolePrivilege;
import com.ums.entity.RolePrivilegeMap;
import com.ums.exception.CustomException;
import com.ums.repository.IRole;
import com.ums.repository.IRolePrivilege;
import com.ums.repository.IRolePrivilegeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService {
    @Autowired
    private IRolePrivilege rolePrivilegeRepo;

    @Autowired
    private IRole roleRepo;

    @Autowired
    private IRolePrivilegeMap rolePrivilegeMapRepo;

    public RolePrivilege createRolePrivilege(RolePrivilege rolePrivilegePayload) {
        findByRolePrivilegeModuleName(rolePrivilegePayload.getModule());
        return rolePrivilegeRepo.save(rolePrivilegePayload);
    }

    public void findByRolePrivilegeModuleName(String module) {
        RolePrivilege moduleName = rolePrivilegeRepo.findByModuleAndIsDeleted(module.toLowerCase(), false);
        if (moduleName != null) {
            throw new CustomException(HttpStatus.CONFLICT, "Module name already exist");
        }
    }

    public List<RolePrivilege> getAllRolePrivilge() {
        return rolePrivilegeRepo.findAllByIsDeletedFalseOrderByModuleAsc();
    }

    public List<RolePrivilege> updateRolePrivilege(RolePrivilege rolePrivilegePayload, String rolePrivilegeId) {
        RolePrivilege rolePrivilegeDetails = rolePrivilegeRepo.findById(rolePrivilegeId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Role privilege not found"));
        if (rolePrivilegeDetails != null && !rolePrivilegeDetails.getModule().toLowerCase().equals(rolePrivilegePayload.getModule())) {
            findByRolePrivilegeModuleName(rolePrivilegePayload.getModule());
        }
        rolePrivilegeDetails.setModule(rolePrivilegePayload.getModule());
        rolePrivilegeDetails.setMethod(rolePrivilegePayload.getMethod());
        rolePrivilegeDetails.setRoute(rolePrivilegePayload.getRoute());
        rolePrivilegeRepo.save(rolePrivilegeDetails);
        return getAllRolePrivilge();
    }

    public RoleDto createRole(RoleDto rolePayload) {
        validateRolePrivilegeIds(rolePayload);
        Role roleData = null;
        if (!Objects.equals(rolePayload.getReportingTo(), "")) {
            roleData = roleRepo.findById(String.valueOf(rolePayload.getReportingTo())).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Reporting role not found"));
        }
        findByRoleName(rolePayload.getRoleName());
        String roleCode = generateRoleCode();
        Role role = new Role(rolePayload.getRoleName(), roleCode, rolePayload.getDescription(), roleData);
        Role roleDetails = roleRepo.save(role);
        for (String data : rolePayload.getRolePrivileges()) {
            Optional<RolePrivilege> rolePrivilegeDetails = rolePrivilegeRepo.findById(data);
            if (rolePrivilegeDetails.isPresent()) {
                rolePrivilegeMapRepo.save(new RolePrivilegeMap(roleDetails,rolePrivilegeDetails.get()));
            }
        }
        return new RoleDto().builder()
                .roleName(roleDetails.getRoleName())
                .description(roleDetails.getDescription())
                .build();
    }

    public List<Role> updateRole(RoleDto rolePayload, String roleId) {
        Role roleDetails = roleRepo.findById(roleId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Role not found"));
        validateRolePrivilegeIds(rolePayload);
        Role roleData = null;
        if (!Objects.equals(rolePayload.getReportingTo(), null)) {
            roleData = roleRepo.findById(String.valueOf(rolePayload.getReportingTo())).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Reporting role not found"));
        }
        if (roleDetails != null && !roleDetails.getRoleName().toLowerCase().equals(rolePayload.getRoleName())) {
            findByRoleName(rolePayload.getRoleName());
        }
        roleDetails.setRoleName(rolePayload.getRoleName());
        roleDetails.setDescription(rolePayload.getDescription());
        roleDetails.setReportingTo(roleData);
        roleRepo.save(roleDetails);
        rolePrivilegeMapRepo.deleteByRoleId(roleId);
        for (String data : rolePayload.getRolePrivileges()) {
            Optional<RolePrivilege> rolePrivilegeDetails = rolePrivilegeRepo.findById(data);
            if (rolePrivilegeDetails.isPresent()) {
                rolePrivilegeMapRepo.save(new RolePrivilegeMap(roleDetails,rolePrivilegeDetails.get()));
            }
        }
        return getAllRoles();
    }

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepo.findAllWithRequiredFields();
    }

    public List<Role> getAllRolesWithReportingTo() {
        return (List<Role>) roleRepo.findAllWithRequiredFields();
//        return roleRepo.performInnerJoin();
    }

    /** Function to generate random role code */
    public String generateRoleCode() {
        UUID uuid = UUID.randomUUID();
        String roleCode = "ROLE_" + uuid.toString().replace("-", "").substring(0, 4);
        return roleCode;
    }

    public void validateRolePrivilegeIds(RoleDto rolePayload) {
        for (String data : rolePayload.getRolePrivileges()) {
            rolePrivilegeRepo.findById(data).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Role privilege Id not found: Id"+ data));
        }
    }

    public void findByRoleName(String roleName) {
        Role role = roleRepo.findByRoleNameAndIsDeleted(roleName.toLowerCase(), false);
        if (role != null) {
            throw new CustomException(HttpStatus.CONFLICT, "Role already exist");
        }
    }


}
