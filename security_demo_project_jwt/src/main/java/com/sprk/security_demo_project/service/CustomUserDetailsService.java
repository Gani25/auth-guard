package com.sprk.security_demo_project.service;

import com.sprk.security_demo_project.configuration.CustomUserDetails;
import com.sprk.security_demo_project.entity.Role;
import com.sprk.security_demo_project.entity.UserInfo;
import com.sprk.security_demo_project.repository.UserInfoRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String credential ) throws UsernameNotFoundException {
//        System.out.println("Searching User: " + username);
        UserInfo userInfo = userInfoRepository.findByUsername(credential)
                        .or(() -> userInfoRepository.findByEmail(credential))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + credential));

        Set<Role> roles = userInfo.getRoles();
//        System.out.println("Roles = "+roles);
//        System.out.println("Found User: " + userInfo.getUsername());
        userInfo.setRoles(roles);
        return new CustomUserDetails(userInfo);
    }
}
