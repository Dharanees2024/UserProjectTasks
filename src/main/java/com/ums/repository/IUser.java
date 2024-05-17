package com.ums.repository;


import com.ums.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUser extends JpaRepository<User, String> {
    Optional<User> findById(String Id);

    User findByUserName(String userName);
    List<User> findByCompanyId(String companyId);



    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users WHERE user_name = :userName", nativeQuery = true)

     void deleteByUserName(String userName);


}
