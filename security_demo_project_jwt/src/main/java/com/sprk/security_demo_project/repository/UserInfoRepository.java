package com.sprk.security_demo_project.repository;

import com.sprk.security_demo_project.entity.Role;
import com.sprk.security_demo_project.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUsername(String username);
    Optional<UserInfo> findByEmail(String email);
}
