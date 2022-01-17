package com.example.demo.model;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataInit implements CommandLineRunner {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();

        UserEntity admin = new UserEntity();
        admin.setEmail("admin@gmail.com");
        admin.setFirstName("admin");
        admin.setLastName("ln_1");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEnabled(true);
        admin.setRoles("ADMIN");
        admin.setPermissions("ACCESS_TEST1,ACCESS_TEST2");

        UserEntity manager = new UserEntity();
        manager.setEmail("manager@gmail.com");
        manager.setFirstName("manager");
        manager.setLastName("ln_3");
        manager.setPassword(passwordEncoder.encode("manager"));
        manager.setEnabled(true);
        manager.setRoles("MANAGER");
        manager.setPermissions("ACCESS_TEST1");

        UserEntity user2 = new UserEntity();
        user2.setEmail("user2@gmail.com");
        user2.setFirstName("user2");
        user2.setLastName("ln_2");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setEnabled(true);
        user2.setRoles("USER");
        user2.setPermissions("");

        List<UserEntity> users = Arrays.asList(admin, manager,user2);
        userRepository.saveAll(users);

    }
}
