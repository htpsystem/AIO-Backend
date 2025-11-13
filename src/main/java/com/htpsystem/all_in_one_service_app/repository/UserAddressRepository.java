package com.htpsystem.all_in_one_service_app.repository;

import com.htpsystem.all_in_one_service_app.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {}
