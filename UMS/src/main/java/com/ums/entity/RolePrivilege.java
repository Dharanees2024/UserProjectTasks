package com.ums.entity;

import com.ums.utils.HelperFunctions;
import com.ums.utils.enums.MethodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "role_privileges")
public class RolePrivilege extends Base {

//    @Pattern(regexp = "^[a-zA-Z]+_[a-zA-Z]+$", message = "Invalid name_name format. It should be in the form of 'name_name'.")
    @Size(min = 2, max = 20, message = "Module length must be between 2 and 20 characters.")
    @NotEmpty(message = "Method is required")
    private String module;

    @NotNull(message = "Method is required")
    @Enumerated(EnumType.STRING)
    private MethodType method;

    @Size(min = 2, max = 20, message = "Route length must be between 2 and 20 characters.")
    @NotEmpty(message = "Method is required")
    private String route;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "rolePrivilegeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePrivilegeMap> roles = new HashSet<>();


    // Getter and setter for 'name' field
    public String getModule() {
        return toTitleCase(module);
    }

    public void setModule(String module) {
        this.module = module.toLowerCase();
    }

    private String toTitleCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
