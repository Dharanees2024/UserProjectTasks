package com.ums.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role_privilege_map")
public class RolePrivilegeMap extends Base {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_privilege_id")
    private RolePrivilege rolePrivilegeId;

}
