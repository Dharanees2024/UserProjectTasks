package com.ums.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    @Size(min = 2, max = 20, message = "Rolename length must be between 2 and 20 characters.")
    @NotEmpty(message = "Username is required")
    private String roleName;
    private String description;
    private String reportingTo;
    @NotEmpty(message = "Role privileges required")
    private List<String> rolePrivileges;
}
