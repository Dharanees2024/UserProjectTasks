package com.ums.repository;

import com.ums.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRole extends JpaRepository<Role, String> {
    Role findByRoleNameAndIsDeleted(String roleName, Boolean isDeleted);
    @Query(value = "SELECT * FROM roles  WHERE is_deleted = false ORDER BY role_name ASC", nativeQuery = true)
    List<Role> findAllWithRequiredFields();

//    @Transactional
//    @Modifying
//    @Query("SELECT id, role_id " +
//            "FROM roles ROLE1 " +
//            "INNER JOIN roles ROLE2 ON ROLE1.role_id = ROLE2.reporting_to" +
//            "WHERE e1.isDeleted = false")
//    List<Role> performInnerJoin();

}
