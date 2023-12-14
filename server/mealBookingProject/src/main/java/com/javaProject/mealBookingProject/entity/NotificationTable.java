package com.javaProject.mealBookingProject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @CurrentTimestamp
    private LocalDateTime date;

    private String role;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private UserTable userID;

    private boolean unread;

    private String message;

}
