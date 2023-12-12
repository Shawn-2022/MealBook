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
public class MealBookingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MealBookingLogID")
    private long Id;

    private long mealBookingID;

    @Enumerated(EnumType.STRING)
    @Column(name = "CouponStatus")
    private Status status;

    @CurrentTimestamp
    @Column(name = "TimeStamp")
    private Timestamp TimeStamp;

    public enum Status {
        CANCELLED, BOOKED, REDEEMED, EXPIRED
    }

}

