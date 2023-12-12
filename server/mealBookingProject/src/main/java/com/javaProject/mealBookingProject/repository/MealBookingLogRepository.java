package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.MealBookingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealBookingLogRepository extends JpaRepository<MealBookingLog,Long> {
}
