package com.example.cloudstorage.init;

import com.example.cloudstorage.entity.Role;
import com.example.cloudstorage.entity.UserEntity;
import com.example.cloudstorage.properties.StorageProperties;
import com.example.cloudstorage.repository.RoleRepository;
import com.example.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InsertUser implements ApplicationRunner {

    private static final String USER1LOGIN = "user";
    private static final String USER1PASSWORD = "user";
    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Path rootLocation;

    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public InsertUser(UserRepository userRepository,
                      RoleRepository roleRepository, StorageProperties storageProperties, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role roleUser = new Role(1L, ROLE_USER);
        roleRepository.save(roleUser);

        UserEntity user = new UserEntity();
        user.setLogin(USER1LOGIN);
        user.setPassword(bcryptEncoder.encode(USER1PASSWORD));
        user.setRole(roleUser);

        userRepository.save(user);

        userRepository.findAll().forEach(userPDO -> {
            try {
                Files.createDirectories(rootLocation.resolve(userPDO.getLogin()));
            } catch (IOException e) {
                throw new RuntimeException("Could not create user directories " + e.getMessage());
            }
            System.out.println(userPDO);
        });
    }
}
