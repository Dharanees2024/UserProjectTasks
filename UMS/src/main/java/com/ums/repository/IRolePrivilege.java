package com.ums.repository;

import com.ums.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRolePrivilege extends JpaRepository<RolePrivilege, String> {
    RolePrivilege findByModuleAndIsDeleted(String module, Boolean isDeleted);
    List<RolePrivilege> findAllByIsDeletedFalseOrderByModuleAsc();
}
