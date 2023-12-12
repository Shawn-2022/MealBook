package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.MealBookingTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealBookingTableRepository extends JpaRepository<MealBookingTable,Long> {

    Optional<MealBookingTable> findByUserIDAndBookingDate(UserTable user, LocalDate bookingDate);

    List<MealBookingTable> findByUserID(UserTable user);
}
