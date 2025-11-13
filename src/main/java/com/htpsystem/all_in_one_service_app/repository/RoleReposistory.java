package com.htpsystem.all_in_one_service_app.repository;

import com.htpsystem.all_in_one_service_app.entity.Gender;
import com.htpsystem.all_in_one_service_app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleReposistory extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
