package com.javaProject.mealBookingProject.auth;

import com.javaProject.mealBookingProject.entity.UserTable;
import com.javaProject.mealBookingProject.repository.UserTableRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutoAdminCreation {

    private UserTableRepository userTableRepository;


    @Autowired
    public AutoAdminCreation(UserTableRepository userTableRepository) {
        this.userTableRepository = userTableRepository;
    }

    @PostConstruct
    public void createAdminAccount(){
        UserTable adminAccount = userTableRepository.findByRole(UserTable.UserRole.ROLE_ADMIN);
        if(adminAccount==null) {
            UserTable admin = new UserTable();
            admin.setEmail("admin@gmail.com");
            admin.setUserID(1l);
            admin.setUser_Name("Shyam");
            admin.setRole(UserTable.UserRole.ROLE_ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userTableRepository.save(admin);
        }
    }
}
