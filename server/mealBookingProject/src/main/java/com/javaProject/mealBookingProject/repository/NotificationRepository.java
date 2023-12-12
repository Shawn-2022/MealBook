package com.javaProject.mealBookingProject.repository;

import com.javaProject.mealBookingProject.entity.NotificationTable;
import com.javaProject.mealBookingProject.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationTable, Long> {

    List<NotificationTable> getByUserID(UserTable user);

    @Query("select n from NotificationTable n where n.userID = ?1 and n.unread = ?2")
    List<NotificationTable> getByUserIDAndUnread(UserTable user, boolean b);
}
