package com.sprk.security_demo_project.service;

import com.sprk.security_demo_project.entity.Role;
import com.sprk.security_demo_project.entity.UserInfo;
import com.sprk.security_demo_project.repository.RoleRepository;
import com.sprk.security_demo_project.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserInfo signUpUser(UserInfo userInfo) {

        // Encode Password from UserInfo
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        Set<Role> finalRoles = new HashSet<>();

        // If client did NOT send any roles → default ROLE_USER
        if (userInfo.getRoles() == null || userInfo.getRoles().isEmpty()) {

            Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));

            finalRoles.add(defaultRole);

        }else{
            for (Role role : userInfo.getRoles()) {

                Role existingRole = roleRepository.findByRoleName(role.getRoleName())
                        .orElseThrow(() ->
                                new RuntimeException("Role not found: " + role.getRoleName())
                        );

                finalRoles.add(existingRole);
            }
        }

        userInfo.setRoles(finalRoles);

        UserInfo signUpUser = userInfoRepository.save(userInfo);

        return signUpUser;



    }

}
