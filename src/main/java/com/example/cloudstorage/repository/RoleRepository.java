package com.example.cloudstorage.repository;

import com.example.cloudstorage.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface RoleRepository extends JpaRepository<Role, Long> {
}

