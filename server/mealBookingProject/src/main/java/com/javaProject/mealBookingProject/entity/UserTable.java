package com.javaProject.mealBookingProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "User_Table")
public class UserTable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UserID;

    private String firstName;
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Column(length = 255,unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(length = 255)
    private String password;

//    private Timestamp lastLogin;
//    future-update set in service to update on every successful authentication

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

            return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public enum UserRole {
        ROLE_ADMIN, ROLE_EMPLOYEE
    }
}