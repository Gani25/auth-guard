package com.sprk.security_demo_project.configuration;

import com.sprk.security_demo_project.entity.Role;
import com.sprk.security_demo_project.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private Set<GrantedAuthority> grantedAuthorities;
    private String username;
    private String password;

    public CustomUserDetails(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();

//        System.out.println("Creating UserDetail Object");

        if(userInfo.getRoles()!=null){

            Set<GrantedAuthority> grantedAuthorities= userInfo
                    .getRoles()
                    .stream()
                    .map(role -> {
//                        System.out.println("ROle = " + role.getRoleName());
                        return new SimpleGrantedAuthority(role.getRoleName());
                    })
                    .collect(Collectors.toSet());

//            Set<Role> roles = userInfo.getRoles();
//            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//            for(Role role:roles){
//               grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()))
//            }
            // All roles will be set in granted authority
            this.grantedAuthorities = grantedAuthorities;
        }else{
            this.grantedAuthorities = new HashSet<>();
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
