package com.ums.repository;

import com.ums.entity.RolePrivilegeMap;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolePrivilegeMap extends JpaRepository<RolePrivilegeMap, String> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_privilege_map WHERE role_id = :roleId", nativeQuery = true)
    void deleteByRoleId(@Param("roleId") String roleId);
}
