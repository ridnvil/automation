package com.bank.database.automationdatabaseservices.repository.auth;

import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Transactional
    @Query(value = "select * from user us where us.username = :username", nativeQuery = true)
    UserModel login(@Param("username") String username);
}
