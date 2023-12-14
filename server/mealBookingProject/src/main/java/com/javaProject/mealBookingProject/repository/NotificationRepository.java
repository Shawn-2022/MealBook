package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationTable, Long> {

    List<NotificationTable> getByUserID(UserTable user);

    List<NotificationTable> getByUserIDAndUnread(UserTable user, boolean b);
}
