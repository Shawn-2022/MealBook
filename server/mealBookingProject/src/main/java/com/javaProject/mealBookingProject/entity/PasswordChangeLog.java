package com.javaProject.mealBookingProject.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PasswordChangeLog")
public class PasswordChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordChangeId;

    private String oldPassword;

    private String newPassword;

    @CurrentTimestamp
    private Timestamp changeDate;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private UserTable userEmail;
}
