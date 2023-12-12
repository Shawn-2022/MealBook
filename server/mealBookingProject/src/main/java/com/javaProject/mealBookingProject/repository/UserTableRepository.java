package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTableRepository extends JpaRepository<UserTable,Long> {


//    @Query("SELECT u FROM UserTable u WHERE u.role = :role") === @Param("role")
    UserTable findByRole(UserTable.UserRole role);

    Optional<UserTable> findByEmail(String Email);

//    UserTable getUserByUserId(Long userId);

//    Optional<UserTable> findFirstByEmail(String email);

//    UserTable getUserByUserId(Long userId);
}
