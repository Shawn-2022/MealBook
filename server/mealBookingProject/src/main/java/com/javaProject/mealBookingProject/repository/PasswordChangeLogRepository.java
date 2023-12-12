package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.PasswordChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordChangeLogRepository extends JpaRepository<PasswordChangeLog,Long> {
}