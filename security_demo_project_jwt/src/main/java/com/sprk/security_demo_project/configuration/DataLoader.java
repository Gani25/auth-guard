package com.sprk.security_demo_project.configuration;

import com.sprk.security_demo_project.entity.Role;
import com.sprk.security_demo_project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(roleRepository.findByRoleName("ROLE_USER").isEmpty()){
            Role role = new Role();

            role.setRoleName("ROLE_USER");
            roleRepository.save(role);
        }

    }
}
