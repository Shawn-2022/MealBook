//package com.javaProject.mealBookingProject.entity;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "meal_table")
//public class MealMenu {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "meal_menu_id")
//    private Long mealMenuId;
//
//    @Enumerated(EnumType.STRING)
//    private DayOfWeek dayOfWeek;
//
//    // Element collection of MealMenuDetail objects
//    @ElementCollection
//    private List<MealMenuDetail> mealMenuDetails;
//
//    // Getters and setters
//
//    public enum DayOfWeek {
//        MONDAY,
//        TUESDAY,
//        WEDNESDAY,
//        THURSDAY,
//        FRIDAY,
//        SATURDAY,
//        SUNDAY
//    }
//
//}
//
