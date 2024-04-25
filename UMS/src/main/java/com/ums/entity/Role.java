package com.ums.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Table(name = "roles")
public class Role extends Base {

    @NotNull
    @Size(min = 2, max = 20, message = "Rolename length must be between 2 and 20 characters.")
    @NotEmpty(message = "Username is required")
    private String roleName;

    @NotNull
    @Column(unique = true)
    private String roleCode;

    private String description;

    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = true)
    private Role reportingTo;


    @OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePrivilegeMap> rolePrivileges = new HashSet<>();

    public Role (String roleName, String roleCode, String description, Role reportingTo) {
        this.roleName = roleName;
        this.roleCode = roleCode;
        this.description = description;
        this.reportingTo = reportingTo;
    }

    // Getter and setter for 'name' field
    public String getRoleName() {
        return toTitleCase(roleName);
    }

    public void setRoleName(String module) {
        this.roleName = module.toLowerCase();
    }

    private String toTitleCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
