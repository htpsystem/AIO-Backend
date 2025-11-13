package com.htpsystem.all_in_one_service_app.repository;

import com.htpsystem.all_in_one_service_app.entity.UserAddress;
import com.htpsystem.all_in_one_service_app.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {}
