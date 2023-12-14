//package com.javaProject.mealBookingProject.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import lombok.*;
//
//
//import java.math.BigDecimal;
//import java.sql.Date;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "employeeTbl")
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long employee_id;
//    @Column(nullable = false)
//    private String firstName;
//    @Column(nullable = false)
//    private String lastName;
//    @Column(nullable = false, unique = true)
//    @Email
//    private String email;
//    private String password;
//    private String department;
//    private Date hireDate;
//    private Date birthDate;
//    private String address;
//    private String zipCode;
//    private String phoneNumber;
//    private BigDecimal salary;
//
//}