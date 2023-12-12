package com.javaProject.mealBookingProject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "MealBookingTable")
public class MealBookingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long BookingID;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private UserTable userID;

    //set the date to the day the meal is booked
    @CurrentTimestamp
    @Column(name = "createdDate")
    private Timestamp createdDate;

    //for which date the meal is booked
    @Column(name = "BookingDate")
    private LocalDate bookingDate;


    @Column(name = "QrCode")
    private String qrCode;


}
